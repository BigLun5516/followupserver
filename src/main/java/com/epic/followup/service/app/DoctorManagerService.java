package com.epic.followup.service.app;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.app.AppDoctorModel;
import com.epic.followup.model.app.DoctorScheduleModel;
import com.epic.followup.temporary.app.manager.LoginManagerRequest;
import com.epic.followup.temporary.app.manager.LoginManagerResponse;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface DoctorManagerService {

    LoginManagerResponse loginManager(LoginManagerRequest req);

    List<AppDoctorModel> getByQuery(String department, String title, String name);
    //获取一周时间的排班表
    JSONObject getWeekSchedule(JSONObject weekTime);
    //获取所有医生名字
    JSONObject getDoctorNames();
    //获取某一条排班记录
    DoctorScheduleModel getScheduleById(Long id);
    //保存排班记录
    void saveSchedule(DoctorScheduleModel ds);
}
