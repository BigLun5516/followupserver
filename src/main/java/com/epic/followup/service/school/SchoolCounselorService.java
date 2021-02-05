package com.epic.followup.service.school;

import com.alibaba.fastjson.JSONObject;

public interface SchoolCounselorService {

    //获取辅导员对应的学院和班级列表
    JSONObject getCollegeAndClasses(Long id);

    //获取班级下的学生列表（带有id)
    JSONObject getStuList(JSONObject req);

    //新增一个紧急事件（辅导员id，学生id，紧急事件）
    JSONObject addEmergency(JSONObject req);

    //查询辅导员对应的所有紧急事件
    JSONObject getAllEmergency(Long id);

}
