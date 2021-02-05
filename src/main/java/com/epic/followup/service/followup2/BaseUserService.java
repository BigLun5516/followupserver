package com.epic.followup.service.followup2;

import com.epic.followup.model.followup2.WechatAppUserModel;
import com.epic.followup.model.followup2.student.StudentInfo;
import com.epic.followup.temporary.DealMessageResponse;
import com.epic.followup.temporary.followup2.LoginRequest;
import com.epic.followup.temporary.followup2.LoginResponse;
import com.epic.followup.temporary.followup2.RegistRequest;
import com.epic.followup.temporary.followup2.ResetPasswordRequest;
import com.epic.followup.temporary.followup2.session.BaseUserSession;

/**
 * @author : zx
 * @version V1.0
 */
public interface BaseUserService {

    // 获取短信验证码
    String getCode(String tel);
    // 设置注册短信验证码，并使用阿里云服务
    DealMessageResponse setCode(String tel);
    // 设置密码重置短信验证码，并使用阿里云服务
    DealMessageResponse setPasswordCode(String tel);
    // 添加绑定账户
    LoginResponse addUser(RegistRequest req);
    // 登录
    LoginResponse login(LoginRequest req);
    // 查询缓存
    BaseUserSession findBySessionId(String sessionId);
    // 重置密码
    DealMessageResponse ResetPassword(ResetPasswordRequest req);

    /*
     * 查询微信用户的sessionid
     */
    WechatAppUserModel findWechatUserByOpenId(String OpenId);

    // 人脸图像检测
    DealMessageResponse compareFace(String base64Img, BaseUserSession user);

    // 获取学生信息
    StudentInfo getStudentInfoByUserID(Long userId);

    // 按照学校获取新注册用户数量
    // continue

}
