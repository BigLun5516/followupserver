package com.epic.followup.service.managementSys.impl;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.managementSys.UserModel;
import com.epic.followup.repository.managementSys.UserRepository;
import com.epic.followup.service.managementSys.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

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
        Object ou = userRepository.getUserByTel(tel);
        if(ou==null){
            res.put("errorCode", 502);
            res.put("errorMsg", "手机号有误");
        }
        else {
            Object[] user = (Object[]) ou;
            if(!user[2].equals(password)){
                res.put("errorCode", 502);
                res.put("errorMsg", "密码有误");
            } else {
                session.setAttribute("id", user[0]);
                session.setAttribute("tel", tel);
                res.put("sessionId", session.getId());
                res.put("errorCode", 200);
                res.put("errorMsg", "登录成功");
                res.put("imageUrl", user[1]);
                res.put("userName", user[4]);
                res.put("userType", user[7]);
                res.put("limit", user[8]);
            }
        }
        return res;
    }

    /**
     * 查询全部用户
     * @param
     * @return JSONObject
     */
    @Override
    public JSONObject findAllUsers(){
        JSONObject res = new JSONObject();
        List<Map<String, Object>> data = new ArrayList<>();
        List<Object> userlist=userRepository.getAllUser();
        for (Object o : userlist) {
            Map<String, Object> item = new HashMap<>();
            Object[] obj = (Object[]) o;
            item.put("id", obj[0]);
            item.put("imageUrl", obj[1]);
            item.put("password", obj[2]);
            item.put("tel", obj[3]);
            item.put("userName", obj[4]);
            item.put("universityId", obj[5]);
            item.put("userType", obj[6]);
            item.put("university", obj[7]);
            item.put("role", obj[8]);
            data.add(item);
        }
        res.put("errorCode", 200);
        res.put("errorMsg", "查询成功");
        res.put("data",data);
        return res;
    }

    /**
     * 删除用户
     * @param id
     * @return JSONObject
     */
    @Override
    public JSONObject deleteUser(Long id){
        JSONObject res = new JSONObject();

        try {
            userRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            res.put("errorCode", 500);
            res.put("errorMsg", "删除失败");
            return res;
        }

        res.put("errorCode", 200);
        res.put("errorMsg", "删除成功");
        return res;

    }


    /**
     * 新增用户
     * @param params
     * @return JSONObject
     */
    @Override
    public JSONObject insertUser(JSONObject params){

        JSONObject res = new JSONObject();

        UserModel userModel = new UserModel();
        userModel.setImageUrl("http://follwup.cmas2020.cn/img/diaryImg/1608689964159-tmp_05359ec4032c588ac2c397854ff6ce60e4ceab585b864509.jpg");
        userModel.setPassword(params.getString("password"));
        userModel.setTel(params.getString("tel"));
        userModel.setUserName(params.getString("name"));
        userModel.setUniversityId(params.getInteger("uid"));
        userModel.setUserType(params.getLong("rid"));

        userRepository.save(userModel);
        res.put("errorCode", 200);
        res.put("errorMsg", "插入成功");
        return res;
    }

    /**
     * 编辑用户
     * @param params
     * @return JSONObject
     */
    @Override
    public JSONObject editUser(JSONObject params){

        JSONObject res = new JSONObject();

        Long id=params.getLong("id");
        Optional<UserModel> u = userRepository.findById(id);
        if (!u.isPresent()){
            res.put("errorCode", 500);
            res.put("errorMsg", "编辑失败");
            return res;
        }
        UserModel userModel=u.get();
        userModel.setPassword(params.getString("password"));
        userModel.setTel(params.getString("tel"));
        userModel.setUserName(params.getString("name"));
        userModel.setUniversityId(params.getInteger("uid"));
        userModel.setUserType(params.getLong("rid"));

        userRepository.save(userModel);
        res.put("errorCode", 200);
        res.put("errorMsg", "编辑成功");
        return res;
    }

    @Override//个人资料
    public JSONObject personalInfo(JSONObject personalParams, HttpServletRequest req) {
        JSONObject res = new JSONObject();

        Long id=personalParams.getLong("id");
        Optional<UserModel> ou = userRepository.findById(id);
        UserModel user = ou.get();
        res.put("img",user.getImageUrl());
        res.put("name",user.getUserName());
        res.put("userqx",user.getUserType());
        res.put("phone",user.getTel());
        res.put("password",user.getPassword());

        return res;
    }
}
