package com.epic.followup.controller.school;

import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.service.school.SchoolUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
@RequestMapping("/school/user")
public class SchoolUserController {

    @Autowired
    private SchoolUserService schoolUserService;

    // 用户登录（登录采用教师编号和密码）
    @RequestMapping(value = "/login/loginByUserName", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject loginByUsername(@RequestBody JSONObject req, HttpSession session) {
        JSONObject res = schoolUserService.loginByUsername(req);

        Long id = res.getLong("userId");
        if (id == null) {
            // 登陆失败
            return res;
        } else {
            // 登陆成功，保存记录到session并返回登陆结果
            session.setAttribute("userId", id);
            session.setAttribute("userType", res.getInteger("userType"));
            session.setAttribute("contactId", res.getLong("contactId"));
            res.remove("userId");
            res.remove("contactId");
            res.put("sessionId",session.getId());
            return res;//返回包括code，msg，userType，sessionId
        }
    }

//    // 发送重置密码验证码
//    @RequestMapping(value = "/getIdentifyCode", method = RequestMethod.POST)
//    @ResponseBody
//    public JSONObject getIdentifyCode(@RequestParam String tel) {
//        return schoolUserService.sendResetPasswordCode(tel);
//    }

//    // 重置密码
//    @RequestMapping(value = "/login/modifyPasswd", method = RequestMethod.POST)
//    @ResponseBody
//    public JSONObject modifyPasswd(@RequestBody JSONObject req) {
//        return schoolUserService.ResetPassword(req);
//    }


    //--------管理员对用户的操作----------

    //列出所有的用户(心理咨询师，辅导员）
    @RequestMapping(value = "/userManage/listAllUser", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject listAllUser(){
        return schoolUserService.listAllUser();
    }

    //列出某个用户(心理咨询师，辅导员）
    @RequestMapping(value = "/userManage/listUser/{id}", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject listUser(@PathVariable("id") Long id){
        return schoolUserService.listUser(id);
    }

    //修改用户密码(心理咨询师，辅导员）
    @RequestMapping(value = "/userManage/modifyUserPsw", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject modifyUserPsw(@RequestBody JSONObject req){
        return schoolUserService.modifyUserPsw(req);
    }

}
