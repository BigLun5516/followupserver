package com.epic.followup.service.school;

import com.alibaba.fastjson.JSONObject;

public interface SchoolUserService {

    // 用户名、密码登录
    JSONObject loginByUsername(JSONObject req);

    //重置密码时发送验证码
    JSONObject sendResetPasswordCode(String tel);

    // 重置密码
    JSONObject ResetPassword(JSONObject req);

    //管理员展示所有用户
    JSONObject listAllUser();

    //管理员展示某个用户(编辑）
    JSONObject listUser(Long id);

    //管理员修改某个用户的密码
    JSONObject modifyUserPsw(JSONObject req);

}
