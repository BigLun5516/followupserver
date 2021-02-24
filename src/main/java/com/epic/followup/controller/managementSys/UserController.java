package com.epic.followup.controller.managementSys;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.service.managementSys.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@CrossOrigin    //允许跨域
@RequestMapping("/managementSystem/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/loginByTel", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject loginByTel(@RequestBody JSONObject loginParams, HttpServletRequest req) {
        JSONObject result = new JSONObject();
        return userService.loginByTel(loginParams,req);
    }

    // 查询
    @PostMapping("/findAll")
    @ResponseBody
    public JSONObject findAllUsers(){

        return userService.findAllUsers();
    }

    // 删除
    @PostMapping("/delete")
    @ResponseBody
    public JSONObject deleteUser(@RequestBody JSONObject params){

        Long id=params.getLong("id");
        return userService.deleteUser(id);
    }

    // 新增
    @PostMapping("/insert")
    @ResponseBody
    public JSONObject insertUser(@RequestBody JSONObject params){

        return userService.insertUser(params);
    }

    // 编辑
    @PostMapping("/edit")
    @ResponseBody
    public JSONObject editUser(@RequestBody JSONObject params){

        return userService.editUser(params);
    }
}
