package com.epic.followup.service.managementSys.task;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public interface TaskService {
    // 查询该学生已完成的任务
    JSONObject findTask1(Long userId);

    // 查询该学生未完成的任务
    JSONObject findTask2(Long userId);

    // 保存该学生的完成任务
    JSONObject saveTask(JSONObject params);

    //创建任务
    JSONObject addTask(JSONObject params);

    //查询任务
    JSONObject findAllTask() throws ParseException;

    //编辑任务状态
    JSONObject editTask(JSONObject params);


}
