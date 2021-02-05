package com.epic.followup.service.managementSys;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

@Service
public interface OperationService{

    // 运营数据模块初始界面
    JSONObject getInitialInfo();

    // 心理咨询中心运营数据界面
    JSONObject getPsychologicalConsultData(String universityName);

    // 院系运营数据界面
    JSONObject getCollegeData(String universityName, String collegeName);

    // 高校运营数据界面
    JSONObject getUniversityData(JSONObject params);
}
