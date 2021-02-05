package com.epic.followup.controller.school;


import com.alibaba.fastjson.JSONObject;
import com.epic.followup.service.school.SchoolCounselorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@CrossOrigin
@RequestMapping("/school/counselor")
public class SchoolCounselorController {

    @Autowired
    private SchoolCounselorService schoolCounselorService;

    //获取辅导员对应的学院和班级列表
    @RequestMapping(value = "/getCollegeAndClasses", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getCollegeAndClasses(HttpSession session){
        Long cId= (Long) session.getAttribute("contactId");
        return schoolCounselorService.getCollegeAndClasses(cId);
    }

    //获取班级下的学生列表（带有id)
    @RequestMapping(value = "/getStuList", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getStuList(@RequestBody JSONObject req){
        return schoolCounselorService.getStuList(req);
    }

    //新增一个紧急事件（辅导员id，学生id，紧急事件）
    @RequestMapping(value = "/addEmergency", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject addEmergency(HttpSession session,@RequestBody JSONObject req){
        Long cId= (Long) session.getAttribute("contactId");
        req.put("cId",cId);
        return schoolCounselorService.addEmergency(req);
    }

    //查询辅导员对应的所有紧急事件
    @RequestMapping(value = "/getAllEmergency", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getAllEmergency(HttpSession session){
        Long cId= (Long) session.getAttribute("contactId");
        return schoolCounselorService.getAllEmergency(cId);
    }
}
