package com.epic.followup.service.managementSys.task;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.temporary.followup2.session.BaseUserSession;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.text.ParseException;

@Service
public interface TaskService {
    // 查询该学生已完成的任务
    JSONObject findTask1(BaseUserSession bus);

    // 查询该学生未完成的任务
    JSONObject findTask2(BaseUserSession bus);

    // 保存该学生的完成任务
    JSONObject saveTask(JSONObject params);

    //创建任务
    JSONObject addTask(JSONObject params, HttpSession session);

    //查询任务
    JSONObject findAllTask(Integer universityId, HttpSession session) throws ParseException;

    //编辑任务状态
//    JSONObject editTask(JSONObject params);


}
