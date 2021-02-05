package com.epic.followup.service.school.impl;

import com.alibaba.fastjson.JSONObject;
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
import com.epic.followup.model.school.SchoolUserModel;
import com.epic.followup.repository.school.SchoolUserRepository;
import com.epic.followup.service.school.SchoolUserService;
import com.epic.followup.temporary.followup2.session.BaseSchoolUserSession;
import com.epic.followup.util.DateTimeUtils;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class SchoolUserServiceImpl implements SchoolUserService {

    @Autowired
    private WeChatConfig weChatConfig;

    private SchoolUserRepository schoolUserRepository;
    private ExpiringMap<String, String> resetCodeMap; // 重置密码
    private ExpiringMap<String, BaseSchoolUserSession> userMap;
    private org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public SchoolUserServiceImpl(SchoolUserRepository SchoolUserRepository) {
        this.schoolUserRepository = SchoolUserRepository;
        this.resetCodeMap = ExpiringMap.builder().maxSize(FollowupStaticConfig.MAX_USERNUM)
                .expiration(1, TimeUnit.MINUTES) // 1分钟有效
                .expirationPolicy(ExpirationPolicy.ACCESSED).variableExpiration().build();
        this.userMap = ExpiringMap.builder().maxSize(FollowupStaticConfig.MAX_USERNUM).expiration(1, TimeUnit.DAYS)
                .expirationPolicy(ExpirationPolicy.ACCESSED).variableExpiration().build();
    }

    // 教师编号，密码登录（教师编号不是电话）（tch_number，password)
    @Override
    public JSONObject loginByUsername(JSONObject req) {
        JSONObject res = new JSONObject();
        List l= schoolUserRepository.findBytNumber(req.getString("username"));
        Object[] cells;
        if (l.size() == 0) {
            res.put("errorCode", 401);
            res.put("errorMsg", "用户不存在");
            return res;
        } else {
            Object row = l.get(0);
            cells = (Object[]) row;
            if (!cells[1].equals(req.getString("password"))) {
                res.put("errorCode", 401);
                res.put("errorMsg", "密码错误");
                return res;
            }
        }

//        // 生成sessionId
//        BaseSchoolUserSession s = new BaseSchoolUserSession();
//        s.setUserId(schoolUserModel.getContact_id());
//        s.setType(schoolUserModel.getType());
//        String md = DigestUtils.md5DigestAsHex((s.getUserId().toString() + s.getType()).getBytes());
//        userMap.put(md, s);

        // 成功返回
        res.put("errorCode", 200);
        res.put("errorMsg", "登录成功");
        res.put("userId", cells[0]);
        res.put("userType", cells[2]);// 代表登录用户类型
        res.put("contactId", cells[3]);// 代表登录用户类型
        return res;
    }

    // 发送重置密码的验证码
    @Override
    public JSONObject sendResetPasswordCode(String tel) {
        JSONObject res = new JSONObject();
        if (resetCodeMap.get(tel) != null) {
            res.put("errorCode", 502);
            res.put("errorMsg", "1分钟内已发送过验证码，请查收.");
            return res;
        }

        // 测试代码
        String code = DateTimeUtils.generateCode();
        resetCodeMap.put(tel, code);
        if (!this.sendCode(tel, "SMS_183770245", code)) {
            res.put("errorCode", 502);
            res.put("errorMsg", "短信发送失败，请联系网站管理员.");
            return res;
        }
        res.put("errorCode", 200);
        res.put("errorMsg", "发送成功");
        return res;
    }

    // 重置密码，请求参数（phone,code,password)
    @Override
    public JSONObject ResetPassword(JSONObject req) {
        JSONObject res = new JSONObject();
        Long uid = schoolUserRepository.findByTel(req.getString("phone"));
        if (uid == null) {
            res.put("errorCode", 505);
            res.put("errorMsg", "用户不存在");
            return res;
        } else if (resetCodeMap.get(req.getString("phone")) != null
                && resetCodeMap.get(req.getString("phone")).equals(req.getString("code"))) {
            Optional<SchoolUserModel> ou = schoolUserRepository.findById(uid);
            SchoolUserModel s = ou.get();
            s.setPassword(req.getString("password"));
            if (schoolUserRepository.save(s) != null) {
                res.put("errorCode", 200);
                res.put("errorMsg", "修改成功");
                return res;
            } else {
                res.put("errorCode", 506);
                res.put("errorMsg", "修改失败");
                return res;
            }
        } else {
            res.put("errorCode", 503);
            res.put("errorMsg", "验证码不存在或已过期");
            return res;
        }
    }

    //管理员展示所有用户
    @Override
    public JSONObject listAllUser(){
        JSONObject res = new JSONObject();
        List<JSONObject> ml = new ArrayList<>();
        List l= schoolUserRepository.findAllUser();
        if(l.size()==0){
            res.put("errorCode", 502);
            res.put("errorMsg", "查询失败");
            return res;
        }
        for (Object row : l){
            Object[] cells = (Object[]) row;
            JSONObject m =new JSONObject();
            m.put("id", cells[0]);
            m.put("name", cells[1]);
            m.put("tch_number", cells[2]);
            m.put("password", cells[3]);
            ml.add(m);
        }
        res.put("errorCode", 200);
        res.put("errorMsg", "查询成功");
        res.put("data", ml);
        return res;
    }

    //管理员展示某个用户
    @Override
    public JSONObject listUser(Long id){
        JSONObject res = new JSONObject();
        List l= schoolUserRepository.findUser(id);
        Object[] cells;
        if(l.size()==0){
            res.put("errorCode", 502);
            res.put("errorMsg", "查询失败");
        }else {
            res.put("errorCode", 200);
            res.put("errorMsg", "查询成功");
            Object row = l.get(0);
            cells = (Object[]) row;
            res.put("id", cells[0]);
            res.put("name", cells[1]);
            res.put("tch_number", cells[2]);
            res.put("password", cells[3]);
        }
        return res;
    }

    //管理员修改某个用户的密码(id,密码）
    @Override
    public JSONObject modifyUserPsw(JSONObject req){
        JSONObject res = new JSONObject();
        SchoolUserModel ou = schoolUserRepository.findById(req.getLong("id")).get();
        ou.setPassword(req.getString("password"));
        schoolUserRepository.save(ou);
        res.put("errorCode", 200);
        res.put("errorMsg", "修改成功");
        return res;
    }

    /*
     * 绑定手机号: SMS_204117086 验证码登录：SMS_204106817 修改密码： SMS_183770245
     */
    public Boolean sendCode(String tel, String templateCode, String code) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", weChatConfig.getAli_access_key_id(),
                weChatConfig.getAli_access_key_secret());
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
