package com.epic.followup.controller.school;


import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.school.SchoolScheduleModel;
import com.epic.followup.service.school.SchoolScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@CrossOrigin    //允许跨域
@RequestMapping("/school/schedule")
public class SchoolScheduleController {

    @Autowired
    private SchoolScheduleService schoolScheduleService;

    //-------管理员操作心理咨询师排班----------

    //列出一周排班表
    @RequestMapping(value = "/listSchedule", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getWeekSchedule(@RequestBody JSONObject weekRequest){
        return schoolScheduleService.getWeekSchedule(weekRequest);
    }

    //获取心理咨询师姓名
    @RequestMapping(value = "/psyNames", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getPsychologistNames(){
        return schoolScheduleService.getPsychologistNames();
    }

    //获取某一条排班信息
    @RequestMapping(value = "/scheduleInfo/{id}", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getScheduleInfo(@PathVariable("id") Long id){
        JSONObject res=new JSONObject();
        SchoolScheduleModel ds = schoolScheduleService.getScheduleById(id);
        if(ds == null) {
            res.put("errorCode",502);
            res.put("errorMsg","获取排班信息失败");
        }
        else {
            res.put("data",ds);
            res.put("errorCode", 200);
            res.put("errorMsg","获取排班信息成功");
        }
        return res;
    }

    //编辑某一条排班信息
    @RequestMapping(value = "/editSchedule", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject editSchedule(@RequestBody JSONObject editScheduleRequest){
        JSONObject res=new JSONObject();
        SchoolScheduleModel ds = schoolScheduleService.getScheduleById(editScheduleRequest.getLong("id"));
        ds.setMorning(editScheduleRequest.getString("morning"));
        ds.setAfternoon(editScheduleRequest.getString("afternoon"));
        ds.setEvening(editScheduleRequest.getString("evening"));
        schoolScheduleService.saveSchedule(ds);
        res.put("errorCode", 200);
        res.put("errorMsg","更新排班信息成功");
        return res;
    }

    //-----------心理咨询师自己查看排班信息-----------

    //心理咨询师查看自己的排班信息
    @RequestMapping(value = "/my/listSchedule", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getMyWeekSchedule(HttpSession session, @RequestBody JSONObject weekRequest){
        weekRequest.put("contactId",session.getAttribute("contactId"));
        return schoolScheduleService.getMyWeekSchedule(weekRequest);
    }
}
