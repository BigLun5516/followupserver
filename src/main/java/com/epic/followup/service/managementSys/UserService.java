package com.epic.followup.service.managementSys;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

@Service
public interface UserService {

    // 登录
    JSONObject loginByTel(@RequestBody JSONObject loginParams, HttpServletRequest req);



    // 查询全部用户
    JSONObject findAllUsers();

    // 删除
    JSONObject deleteUser(Long id);

    // 新增
    JSONObject insertUser(JSONObject params);

    // 编辑
    JSONObject editUser(JSONObject params);
}