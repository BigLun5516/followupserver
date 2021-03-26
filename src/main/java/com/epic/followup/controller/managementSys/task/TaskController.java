package com.epic.followup.controller.managementSys.task;


import com.alibaba.fastjson.JSONObject;
import com.epic.followup.service.followup2.BaseUserService;
import com.epic.followup.service.followup2.WechatPatientService;
import com.epic.followup.service.managementSys.task.TaskService;
import com.epic.followup.temporary.DealMessageResponse;
import com.epic.followup.temporary.followup2.session.BaseUserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;

@Controller
@CrossOrigin
@RequestMapping("/managementSystem/task")
public class TaskController {

    @Autowired
    TaskService taskService;

    @Autowired
    private BaseUserService baseUserService;

    //-------------------小程序接口--------------------------

    // 查询该学生已完成的任务
    @RequestMapping(value = "/student/find1", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject find1(HttpServletRequest request ){
        BaseUserSession bus = baseUserService.findBySessionId(request.getHeader("sessionId"));
        return taskService.findTask1(bus);
    }

    // 查询该学生未完成的任务
    @RequestMapping(value = "/student/find2", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject find2(HttpServletRequest request ){
        BaseUserSession bus = baseUserService.findBySessionId(request.getHeader("sessionId"));
        return taskService.findTask2(bus);
    }

    // 保存该学生的完成任务
    @RequestMapping(value = "/student/save", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject saveTask(@RequestBody JSONObject params,HttpServletRequest request){
        BaseUserSession bus = baseUserService.findBySessionId(request.getHeader("sessionId"));
        params.put("user_id",bus.getUserId());
        return taskService.saveTask(params);
    }


    //-------------------后台管理系统接口--------------------------

    //创建任务（超级管理员不应该创建任务，因为没有对应学校）
    @RequestMapping(value = "/addTask", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject addTask(@RequestBody JSONObject params,HttpServletRequest req){
        HttpSession session = req.getSession();
        Integer universityId= (Integer) session.getAttribute("universityId");
        params.put("universityId",universityId);
        return taskService.addTask(params);
    }

    //查询任务
    @RequestMapping(value = "/findAll", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject findAll(HttpServletRequest req) throws ParseException {
        HttpSession session = req.getSession();
        Integer universityId= (Integer) session.getAttribute("universityId");
        System.out.println("用户学校id："+universityId);
        return taskService.findAllTask(universityId);
    }

//    //编辑任务状态
//    @RequestMapping(value = "/editTask", method = RequestMethod.POST)
//    @ResponseBody
//    public JSONObject editTask(@RequestBody JSONObject params){
//        return taskService.editTask(params);
//    }
}
