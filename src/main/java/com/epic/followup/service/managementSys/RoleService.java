package com.epic.followup.service.managementSys;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public interface RoleService {

    // 查询全部角色
    JSONObject findAllRoles(HttpSession session);

    // 删除
    JSONObject deleteRole(Long id);

    // 新增
    JSONObject insertRole(JSONObject params);

    // 编辑
    JSONObject editRole(JSONObject params);

}
