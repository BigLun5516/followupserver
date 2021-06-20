package com.epic.followup.service.managementSys;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public interface UserService {

    // 登录
    JSONObject loginByTel(@RequestBody JSONObject loginParams, HttpServletRequest req);

    //个人资料
    JSONObject personalInfo(HttpServletRequest req);

    // 查询全部用户
    JSONObject findAllUsers(HttpSession session);

    // 删除
    JSONObject deleteUser(Long id);

    // 新增
    JSONObject insertUser(JSONObject params);

    // 编辑
    JSONObject editUser(JSONObject params);

    //上传图片
    JSONObject uploadDiaryImg(MultipartFile file);

    //个人信息编辑
    JSONObject personalInfoEdit(HttpServletRequest req, JSONObject params);

    //Mini结果展示（包括不重复的展示和个人的展示）
    JSONObject getMiniResult(Integer userUniversityId, List<Integer> userCollegeIdList,String stid);

    //获取当前登录后台用户的学校的所有注册账号
    JSONObject getRegisterUser(Integer universityId);
}