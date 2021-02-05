package com.epic.followup.service.followup2.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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
import com.epic.followup.model.followup2.BaseUserModel;
import com.epic.followup.model.followup2.WechatAppUserModel;
import com.epic.followup.model.followup2.student.StudentInfo;
import com.epic.followup.repository.followup2.BaseUserRepository;
import com.epic.followup.repository.followup2.WechatAppUserRepository;
import com.epic.followup.repository.followup2.student.StudentInfoRepository;
import com.epic.followup.service.NLPService;
import com.epic.followup.service.followup2.BaseUserService;
import com.epic.followup.temporary.DealMessageResponse;
import com.epic.followup.temporary.followup2.LoginRequest;
import com.epic.followup.temporary.followup2.LoginResponse;
import com.epic.followup.temporary.followup2.RegistRequest;
import com.epic.followup.temporary.followup2.ResetPasswordRequest;
import com.epic.followup.temporary.followup2.session.BaseUserSession;
import com.epic.followup.util.DateTimeUtils;

import org.apache.xmlrpc.XmlRpcException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;

/**
 * @author : zx
 * @version V1.0
 */
@Service
public class BaseUserServiceImpl implements BaseUserService {

    private ExpiringMap<String, String> codeMap;
    private ExpiringMap<String, String> resetCodeMap; // 重置密码
    private ExpiringMap<String, BaseUserSession> userMap;
    private BaseUserRepository baseUserRepository;
    private WechatAppUserRepository wechatAppUserRepository;
    private StudentInfoRepository studentInfoRepository;
    private WeChatConfig weChatConfig;
    private NLPService nlpService;
    private org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public BaseUserServiceImpl(BaseUserRepository baseUserRepository, WeChatConfig weChatConfig,
                               WechatAppUserRepository wechatAppUserRepository,
                               StudentInfoRepository studentInfoRepository,
                               NLPService nlpService){
        this.codeMap = ExpiringMap.builder()
                .maxSize(FollowupStaticConfig.MAX_USERNUM)
                .expiration(1, TimeUnit.MINUTES) // 1分钟有效
                .expirationPolicy(ExpirationPolicy.ACCESSED)
                .variableExpiration()
                .build();
        this.resetCodeMap = ExpiringMap.builder()
                .maxSize(FollowupStaticConfig.MAX_USERNUM)
                .expiration(1, TimeUnit.MINUTES) // 1分钟有效
                .expirationPolicy(ExpirationPolicy.ACCESSED)
                .variableExpiration()
                .build();
        this.baseUserRepository = baseUserRepository;
        this.weChatConfig = weChatConfig;
        this.wechatAppUserRepository = wechatAppUserRepository;
        this.studentInfoRepository = studentInfoRepository;
        this.nlpService = nlpService;
        this.userMap = ExpiringMap.builder()
                .maxSize(FollowupStaticConfig.MAX_USERNUM)
                .expiration(1, TimeUnit.DAYS)
                .expirationPolicy(ExpirationPolicy.ACCESSED)
                .variableExpiration()
                .build();
    }

    @Override
    public String getCode(String tel) {
        return codeMap.get(tel);
    }

    @Override
    public StudentInfo getStudentInfoByUserID(Long userId) {
        return this.studentInfoRepository.findByUserId(userId).get();
    }

    @Override
    public DealMessageResponse setCode(String tel) {
        DealMessageResponse res = new DealMessageResponse();
        if (codeMap.get(tel) != null){
            res.setErrorMsg("1分钟内已发送过验证码，请查收.");
            res.setErrorCode(502);
            return res;
        }

        // 测试代码
        String code = DateTimeUtils.generateCode();
        codeMap.put(tel, code);
        if (!this.sendCode(tel, "SMS_183763062", code)){
            res.setErrorMsg("短信发送失败，请联系网站管理员.");
            res.setErrorCode(502);
            return res;
        }

        res.setErrorCode(200);
        res.setErrorMsg("发送成功");
        return res;
    }

    @Override
    public DealMessageResponse setPasswordCode(String tel) {
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
    @Transactional
    /*
     * 添加学生用户
     */
    public LoginResponse addUser(RegistRequest req) {
        LoginResponse res = new LoginResponse();

        // 验证码校验
        if (this.codeMap.get(req.getTel()) != null){
            if (!req.getCode().equals(this.codeMap.get(req.getTel()))){
                res.setErrorCode(503);
                res.setErrorMsg("验证码不正确.");
                return res;
            }
        }else {
            res.setErrorCode(503);
            res.setErrorMsg("验证码不存在或已过期.");
            return res;
        }

        // 部门、学号校验
        Optional<StudentInfo>oStudentInfo = studentInfoRepository.findByStidAndDepartment(req.getStid(), req.getDepartment());
        if (!oStudentInfo.isPresent()){
            res.setErrorCode(503);
            res.setErrorMsg("学号不存在或所属学校未注册.");
            return res;
        }

        StudentInfo si = oStudentInfo.get();
        if (si.getUserId() != null){
            if (si.getUserId() != -1){
                res.setErrorCode(503);
                res.setErrorMsg("此学号已注册.");
                return res;
            }
        }

        // 密码校验
        // !req.getPassword().equals(req.getStid().substring(req.getStid().length()-6))
        if (req.getPassword().equals("")){
            res.setErrorCode(503);
            res.setErrorMsg("密码不能为空.");
            return res;
        }

        // 手机号是否已存在
        if (this.baseUserRepository.findByTel(req.getTel()).isPresent()){
            res.setErrorCode(505);
            res.setErrorMsg("该手机号已注册.");
            return res;
        }

        // 入库
        BaseUserModel user = new BaseUserModel();
        user.setCreateTime(new Date());
        user.setDepartment(req.getDepartment());
        user.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes())); // md5 加密
        user.setStid(req.getStid());
        user.setTel(req.getTel());
        user.setType(3);
        user = baseUserRepository.save(user);
        // 更新学生信息表
        si.setUserId(user.getUserId());
        studentInfoRepository.save(si);

        // 生成sessionid
        BaseUserSession s = new BaseUserSession();
        s.setTel(user.getTel());
        s.setUserId(user.getUserId());
        s.setType(user.getType());
        s.setTime(new Date().getTime());
        String md = DigestUtils.md5DigestAsHex((s.getTel()+s.getUserId()+s.getType()+s.getTime()).getBytes());
        userMap.put(md, s);

        // 成功返回
        res.setErrorMsg("注册成功.");
        res.setErrorCode(200);
        res.setSessionId(md);
        return res;
    }

    @Override
    public DealMessageResponse compareFace(String base64Img, BaseUserSession user) {
        DealMessageResponse res = new DealMessageResponse();
        Optional<StudentInfo> ost = studentInfoRepository.findByUserId(user.getUserId());
        if (!ost.isPresent()){
            res.setErrorMsg("无该学生信息.");
            res.setErrorCode(204);
            return res;
        }
        StudentInfo stui = ost.get();
        if (stui.getImgPath() == null){
            res.setErrorMsg("数据库无原始图像.");
            res.setErrorCode(204);
            return res;
        }
        byte[] data = null;

        // 跳过验证
        if(stui.getImgPath().equals("na")){
            res.setErrorMsg("对比成功");
            res.setErrorCode(200);
            return res;
        }
        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(stui.getImgPath());
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            log.error("图片读取失败");
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        Base64.Encoder encoder = Base64.getEncoder();
        int r = -1;
        try {
            r = nlpService.baiduFaceIden_base64(encoder.encodeToString(Objects.requireNonNull(data)), base64Img);
        }catch (XmlRpcException e){
            log.error(e.getMessage());
            res.setErrorMsg("服务器异常.");
            res.setErrorCode(503);
            return res;
        }

        if (r == 2){
            res.setErrorMsg("对比成功");
            res.setErrorCode(200);
            return res;
        }else if (r == 0){
            res.setErrorMsg("无人脸");
            res.setErrorCode(403);
            return res;
        }else if (r == 1){
            res.setErrorMsg("不是同一个人");
            res.setErrorCode(403);
            return res;
        }else {
            res.setErrorMsg("？？？");
            res.setErrorCode(403);
            return res;
        }
    }

    @Override
    public LoginResponse login(LoginRequest req) {
        LoginResponse res = new LoginResponse();
        Optional<BaseUserModel> ob = baseUserRepository.findByTel(req.getTel());
        if (ob.isPresent()){
            if (!req.getPassword().equals(ob.get().getPassword())){
                res.setErrorCode(401);
                res.setErrorMsg("密码错误或账户不存在.");
                return res;
            }
        }else {
            res.setErrorCode(401);
            res.setErrorMsg("密码错误或账户不存在.");
            return res;
        }
        BaseUserSession s = new BaseUserSession();
        BaseUserModel user = ob.get();
        s.setTel(user.getTel());
        s.setUserId(user.getUserId());
        s.setType(user.getType());
        s.setTime(new Date().getTime());
        String md = DigestUtils.md5DigestAsHex((s.getTel()+s.getUserId()+s.getType()+s.getTime()).getBytes());
        userMap.put(md, s);
        // 成功返回
        res.setErrorMsg("登录成功.");
        res.setErrorCode(200);
        res.setSessionId(md);
        return res;
    }

    @Override
    public BaseUserSession findBySessionId(String sessionId) {
        return userMap.get(sessionId);
    }

    @Override
    public WechatAppUserModel findWechatUserByOpenId(String OpenId) {
        Optional<WechatAppUserModel> ou = wechatAppUserRepository.findByOpenId(OpenId);
        if (ou.isPresent()){
            return ou.get();
        }else {
            return null;
        }
    }

    @Override
    public DealMessageResponse ResetPassword(ResetPasswordRequest req) {
        DealMessageResponse res = new DealMessageResponse();
        Optional<BaseUserModel> ou = baseUserRepository.findByTel(req.getTel());
        if (!ou.isPresent()){
            res.setErrorMsg("用户不存在.");
            res.setErrorCode(505);
            return res;
        }
        BaseUserModel u = ou.get();
        if (resetCodeMap.get(u.getTel())!= null && resetCodeMap.get(u.getTel()).equals(req.getCode())){
            res.setErrorMsg("修改成功");
            res.setErrorCode(200);
            u.setPassword(req.getPassword());
            baseUserRepository.save(u);
            return res;
        }else {
            res.setErrorMsg("验证码错误或已失效.");
            res.setErrorCode(505);
            return res;
        }
    }

    /*
     * 注册tempid : SMS_183763062
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
