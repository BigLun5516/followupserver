package com.epic.followup.service.school;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.school.SchoolScheduleModel;

public interface SchoolScheduleService {

    //获取一周时间的排班表
    JSONObject getWeekSchedule(JSONObject weekTime);
    //获取所有心理咨询师名字
    JSONObject getPsychologistNames();
    //获取某一条排班记录
    SchoolScheduleModel getScheduleById(Long id);
    //保存排班记录
    void saveSchedule(SchoolScheduleModel ds);
    //心理咨询师获取一周时间的排班表
    JSONObject getMyWeekSchedule(JSONObject weekTime);
}
