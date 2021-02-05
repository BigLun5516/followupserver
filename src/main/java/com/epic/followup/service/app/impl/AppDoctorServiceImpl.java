package com.epic.followup.service.app.impl;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.epic.followup.conf.FollowupStaticConfig;
import com.epic.followup.conf.WeChatConfig;
import com.epic.followup.model.app.AppDoctorModel;
import com.epic.followup.repository.app.AppDoctorRepository;
import com.epic.followup.service.app.AppDoctorService;
import com.epic.followup.temporary.DealMessageResponse;
import com.epic.followup.temporary.app.patient.AppLoginbyCodeRequest;
import com.epic.followup.temporary.app.patient.AppLoginbyCodeResponse;
import com.epic.followup.temporary.app.doctor.*;
import com.epic.followup.util.DateTimeUtils;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Optional;
import java.util.concurrent.TimeUnit;


@Service
public class AppDoctorServiceImpl implements AppDoctorService {

    @Autowired
    private AppDoctorRepository appDoctorRepository;

    @Autowired
    private WeChatConfig weChatConfig;

    private ExpiringMap<String, BaseDoctorSession> doctorMap; // 医生信息
    private ExpiringMap<String, String> bindCodeMap; // 绑定手机号验证码
    private ExpiringMap<String, String> resetCodeMap; // 重置密码
    private ExpiringMap<String, String> loginCodeMap; //登录验证码
    private org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());

    public AppDoctorServiceImpl(){
        this.doctorMap = ExpiringMap.builder()
                .maxSize(FollowupStaticConfig.MAX_USERNUM)
                .expiration(1, TimeUnit.DAYS)
                .expirationPolicy(ExpirationPolicy.ACCESSED)
                .variableExpiration()
                .build();
        this.bindCodeMap = ExpiringMap.builder()
                .maxSize(FollowupStaticConfig.MAX_USERNUM)
                .expiration(1, TimeUnit.MINUTES)
                .expirationPolicy(ExpirationPolicy.ACCESSED)
                .variableExpiration()
                .build();
        this.resetCodeMap = ExpiringMap.builder()
                .maxSize(FollowupStaticConfig.MAX_USERNUM)
                .expiration(1, TimeUnit.MINUTES) // 1分钟有效
                .expirationPolicy(ExpirationPolicy.ACCESSED)
                .variableExpiration()
                .build();
        this.loginCodeMap = ExpiringMap.builder()
                .maxSize(FollowupStaticConfig.MAX_USERNUM)
                .expiration(1, TimeUnit.MINUTES) // 1分钟有效
                .expirationPolicy(ExpirationPolicy.CREATED)
                .variableExpiration()
                .build();
    }

    @Override
    public LoginResponse loginByEmployeeNum(LoginByEmployeeNumRequest req) {
        LoginResponse res = new LoginResponse();

        AppDoctorModel appDoctorModel = appDoctorRepository.findByEmployeeNum(req.getEmployeeNum());
        if(appDoctorModel == null){
            res.setErrorCode(401);
            res.setErrorMsg("职工号不存在");
            return res;
        }
        else {
            AppDoctorModel appDoctorModel1 = appDoctorRepository.findByEmployeeNumAndPassword(req.getEmployeeNum(), req.getPassword());
            if (appDoctorModel1 == null){
                res.setErrorCode(401);
                res.setErrorMsg("密码错误");
                return res;
            }
        }

        // 生成sessionId
        BaseDoctorSession s = new BaseDoctorSession();
        s.setDoctorId(appDoctorModel.getDoctorId());
        s.setEmployeeNum(appDoctorModel.getEmployeeNum());
        String md = DigestUtils.md5DigestAsHex((s.getEmployeeNum()+s.getDoctorId()).getBytes());
        doctorMap.put(md, s);

        // 成功返回
        res.setErrorMsg("登录成功.");
        res.setErrorCode(200);
        res.setSessionId(md);
        return res;
    }

    @Override
    public AppLoginbyCodeResponse loginbyCode(AppLoginbyCodeRequest req) {
        AppLoginbyCodeResponse res=new AppLoginbyCodeResponse();
        Optional<AppDoctorModel> ou = appDoctorRepository.findByTel(req.getTel());
        if (!ou.isPresent()){
            res.setErrorMsg("用户不存在.");
            res.setErrorCode(504);
            return res;
        }
        AppDoctorModel d = ou.get();
        if (loginCodeMap.get(d.getTel())!= null && loginCodeMap.get(d.getTel()).equals(req.getCode())){
            res.setErrorMsg("登录成功");
            res.setErrorCode(200);
            res.setPassword(d.getPassword());
            return res;
        }else {
            res.setErrorMsg("验证码错误或已失效.");
            res.setErrorCode(505);
            return res;
        }
    }

    @Override
    public DealMessageResponse sendBindCode(String tel){
        DealMessageResponse res = new DealMessageResponse();
        if (this.bindCodeMap.get(tel) != null){
            res.setErrorMsg("1分钟内已发送过验证码，请查收.");
            res.setErrorCode(502);
            return res;
        }

        // 测试代码
        String code = DateTimeUtils.generateCode();
        this.bindCodeMap.put(tel, code);
        if (!this.sendCode(tel, "SMS_204117086", code)){
            res.setErrorMsg("短信发送失败，请联系网站管理员.");
            res.setErrorCode(502);
            return res;
        }

        res.setErrorCode(200);
        res.setErrorMsg("发送成功");
        return res;
    }

    @Override
    public DealMessageResponse sendResetPasswordCode(String tel) {
        DealMessageResponse res = new DealMessageResponse();
        if (resetCodeMap.get(tel) != null){
            res.setErrorMsg("1分钟内已发送过验证码，请查收.");
            res.setErrorCode(502);
            return res;
        }

        // 测试代码
        String code = DateTimeUtils.generateCode();
        resetCodeMap.put(tel, code);
        if (!this.sendCode(tel, "SMS_183770245", code)){
            res.setErrorMsg("短信发送失败，请联系网站管理员.");
            res.setErrorCode(502);
            return res;
        }

        res.setErrorCode(200);
        res.setErrorMsg("发送成功");
        return res;
    }

    @Override
    public DealMessageResponse sendLoginCode(String tel) {
        DealMessageResponse res = new DealMessageResponse();
        if (loginCodeMap.get(tel) != null){
            res.setErrorMsg("1分钟内已发送过验证码，请查收.");
            res.setErrorCode(502);
            return res;
        }

        // 测试代码
        String code = DateTimeUtils.generateCode();
        loginCodeMap.put(tel, code);
        if (!this.sendCode(tel, "SMS_204106817", code)){
            res.setErrorMsg("短信发送失败，请联系网站管理员.");
            res.setErrorCode(502);
            return res;
        }

        res.setErrorCode(200);
        res.setErrorMsg("发送成功");
        return res;
    }

    @Override
    public DealMessageResponse bindTelWithCode(BindTelRequest req) {
        DealMessageResponse res = new DealMessageResponse();
        if (this.bindCodeMap.get(req.getTel())!= null){
            if (!req.getCode().equals(this.bindCodeMap.get(req.getTel()))){
                res.setErrorCode(501);
                res.setErrorMsg("验证码不正确.");
                return res;
            }
        }
        else {
            res.setErrorCode(503);
            res.setErrorMsg("验证码不存在或已过期.");
            return res;
        }

        // 判断手机号是否存在
        if (appDoctorRepository.findByTel(req.getTel()).isPresent()){
            res.setErrorCode(505);
            res.setErrorMsg("该手机号已注册.");
            return res;
        }

        AppDoctorModel doctor = appDoctorRepository.findByEmployeeNum(req.getEmployeeNum());
        if (doctor == null){
            res.setErrorMsg("用户不存在.");
            res.setErrorCode(504);
            return res;
        }
        else {
            doctor.setTel(req.getTel());
            if (appDoctorRepository.save(doctor).getTel() != null){
                res.setErrorMsg("绑定手机号成功");
                res.setErrorCode(200);
                return res;
            }
            else {
                res.setErrorMsg("绑定手机号失败");
                res.setErrorCode(506);
                return res;
            }
        }
    }

    @Override
    public DealMessageResponse editDoctorInfo(EditDoctorRequest req) {
        DealMessageResponse res = new DealMessageResponse();
        AppDoctorModel byEmployeeNum = appDoctorRepository.findByEmployeeNum(req.getEmployeeNum());
        if (byEmployeeNum == null){
            res.setErrorMsg("用户不存在.");
            res.setErrorCode(505);
            return res;
        }

        byEmployeeNum.setPhoto(req.getPhoto());
        byEmployeeNum.setSpeciality(req.getSpeciality());
        if (appDoctorRepository.save(byEmployeeNum)!=null){
            res.setErrorCode(200);
            res.setErrorMsg("修改医生信息成功");
        } else {
            res.setErrorCode(506);
            res.setErrorMsg("修改医生信息失败");
        }

        return res;
    }

    @Override
    public DealMessageResponse ResetPassword(ResetPasswdRequest req) {
        DealMessageResponse res = new DealMessageResponse();
        AppDoctorModel byEmployeeNum = appDoctorRepository.findByEmployeeNum(req.getEmployeeNum());
        if (byEmployeeNum == null){
            res.setErrorMsg("用户不存在.");
            res.setErrorCode(505);
            return res;
        }else if (resetCodeMap.get(req.getTel())!= null && resetCodeMap.get(req.getTel()).equals(req.getCode())){
            byEmployeeNum.setPassword(req.getPassword());
            if (appDoctorRepository.save(byEmployeeNum) != null){
                res.setErrorMsg("修改成功");
                res.setErrorCode(200);
                return res;
            }else {
                res.setErrorMsg("修改失败");
                res.setErrorCode(506);
                return res;
            }
        }else {
            res.setErrorMsg("验证码不存在或已过期.");
            res.setErrorCode(503);
            return res;
        }
    }

    @Override
    public DoctorInfoResponse showDoctorInfo(String employeeNum) {
        DoctorInfoResponse res = new DoctorInfoResponse();
        AppDoctorModel byEmployeeNum = appDoctorRepository.findByEmployeeNum(employeeNum);
        DoctorInfo doctorInfo = new DoctorInfo();
        if (byEmployeeNum == null){
            res.setErrorMsg("用户不存在.");
            res.setErrorCode(505);
            return res;
        }
        else {
            doctorInfo.setDoctorId(byEmployeeNum.getDoctorId());
            doctorInfo.setEmployeeNum(byEmployeeNum.getEmployeeNum());
            doctorInfo.setName(byEmployeeNum.getName());
            doctorInfo.setDepartment(byEmployeeNum.getDepartment());
            doctorInfo.setTel(byEmployeeNum.getTel());
            doctorInfo.setPhoto(byEmployeeNum.getPhoto());
            doctorInfo.setSpeciality(byEmployeeNum.getSpeciality());
            res.setErrorMsg("查询用户信息成功");
            res.setErrorCode(200);
            res.setData(doctorInfo);
            return res;
        }
    }

    @Override
    public AppDoctorModel getByEmployeeNum(String employeeNum) {
        return appDoctorRepository.findByEmployeeNum(employeeNum);
    }

    @Override
    public <S extends AppDoctorModel> boolean addDoctor(S d) {
        return (appDoctorRepository.save(d).getEmployeeNum() != null);
    }

    @Override
    public AppDoctorModel getById(Long id) {
        return appDoctorRepository.findByDoctorId(id);
    }

    @Override
    public void deleteById(Long id) {
        AppDoctorModel byDoctorId = appDoctorRepository.findByDoctorId(id);
        appDoctorRepository.delete(byDoctorId);
    }


    /*
     * 绑定手机号: SMS_204117086
     * 验证码登录：SMS_204106817
     * 修改密码： SMS_183770245
     */
    public Boolean sendCode(String tel, String templateCode, String code) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", weChatConfig.getAli_access_key_id(), weChatConfig.getAli_access_key_secret());
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", tel);
        request.putQueryParameter("SignName", "迈思智能");
        request.putQueryParameter("TemplateCode", templateCode);
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            log.info(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
            return false;
        } catch (ClientException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
