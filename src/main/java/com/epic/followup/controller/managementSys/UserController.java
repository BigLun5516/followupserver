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

        // 判断是否已经登录
        HttpSession session = req.getSession();
        if (session.getAttribute("tel") != null) {
            // session没过期，直接返回
            result.put("errorCode", 200);
            result.put("errorMsg", "已经登录");
            result.put("sessionId", session.getId());
            return result;
        }
        else {
            return userService.loginByTel(loginParams, req);
        }
    }
}
