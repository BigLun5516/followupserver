package com.epic.followup.service.managementSys.impl;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.managementSys.UserModel;
import com.epic.followup.repository.managementSys.UserRepository;
import com.epic.followup.service.managementSys.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public JSONObject loginByTel(JSONObject loginParams, HttpServletRequest req) {
        String tel = loginParams.getString("tel");
        String password = loginParams.getString("password");

        JSONObject res = new JSONObject();

        HttpSession session = req.getSession();
        Optional<UserModel> ou = userRepository.findByTel(tel);
        if(!ou.isPresent()){
            res.put("errorCode", 502);
            res.put("errorMsg", "手机号有误");
        }
        else {
            UserModel user = ou.get();
            if(!user.getPassword().equals(password)){
                res.put("errorCode", 502);
                res.put("errorMsg", "密码有误");
            } else {
                session.setAttribute("id", user.getUserId());
                session.setAttribute("tel", tel);
                res.put("sessionId", session.getId());
                res.put("errorCode", 200);
                res.put("errorMsg", "登录成功");
                res.put("imageUrl", user.getImageUrl());
                res.put("userName", user.getUserName());
                res.put("userType", user.getUserType());
            }
        }
        return res;
    }

    @Override//个人资料
    public JSONObject personalInfo(JSONObject personalParams, HttpServletRequest req) {
        JSONObject res = new JSONObject();
        String tel = personalParams.getString("tel");
        //HttpSession session = req.getSession();
        Optional<UserModel> ou = userRepository.findByTel(tel);
        UserModel user = ou.get();
        res.put("img",user.getImageUrl());
        res.put("name",user.getUserName());
        res.put("userqx",user.getUserType());
        res.put("phone",user.getTel());
        res.put("password",user.getPassword());

        return res;
    }
}
