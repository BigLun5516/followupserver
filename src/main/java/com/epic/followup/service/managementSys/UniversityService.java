package com.epic.followup.service.managementSys;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.managementSys.UniversityModel;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
public interface UniversityService {

    // 查询所有高校（高校表里的信息，不涉及其他表）
    JSONObject findAllUniversity(@RequestBody JSONObject Params);

    // 根据条件查询
    JSONObject findUniversity(JSONObject params);

    // 删除
    JSONObject deleteUniversity(JSONObject params);

    // 新增
    JSONObject insertUniversity(JSONObject params);

    // 编辑
    JSONObject editUniversity(JSONObject params);

    /*
    * 量表
    */
    // 获取所有学校名称
    JSONObject getAllUniversityName();

    // 后台根据登录的用户获取所有学校名称
    JSONObject getAllUniversityNameByUid(HttpSession session);


}
