package com.epic.followup.controller.managementSys;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.service.managementSys.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    //个人资料
    @PostMapping("/personalInfo")
    @ResponseBody
    public JSONObject personalInfo(HttpServletRequest req) {
        return userService.personalInfo(req);
    }

    //上传个人头像
    @RequestMapping(value = "/uploadDiaryImg", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject uploadDiaryImg(@RequestParam MultipartFile file){

        return userService.uploadDiaryImg(file);
    }

    //个人资料更新
    @PostMapping("/personalInfoEdit")
    @ResponseBody
    public JSONObject personalInfoEdit(HttpServletRequest req,@RequestBody JSONObject params) {
        return userService.personalInfoEdit(req,params);
    }

    //获取Mini量表接口
    @PostMapping("/miniResult")
    @ResponseBody
    public JSONObject miniResult(HttpSession session) {

        return userService.getMiniResult((Integer) session.getAttribute("universityId"));
    }

}
