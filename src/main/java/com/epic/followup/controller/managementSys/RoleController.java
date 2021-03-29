package com.epic.followup.controller.managementSys;


import com.alibaba.fastjson.JSONObject;
import com.epic.followup.service.managementSys.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@CrossOrigin    //允许跨域
@RequestMapping("/managementSystem/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    // 查询
    @PostMapping("/findAll")
    @ResponseBody
    public JSONObject findAllRoles(HttpSession session){

        return roleService.findAllRoles(session);
    }

    // 删除
    @PostMapping("/delete")
    @ResponseBody
    public JSONObject deleteRole(@RequestBody JSONObject params){

        Long id=params.getLong("id");
        return roleService.deleteRole(id);
    }

    // 新增
    @PostMapping("/insert")
    @ResponseBody
    public JSONObject insertRole(@RequestBody JSONObject params){

        return roleService.insertRole(params);
    }

    // 编辑
    @PostMapping("/edit")
    @ResponseBody
    public JSONObject editRole(@RequestBody JSONObject params){

        return roleService.editRole(params);
    }

}
