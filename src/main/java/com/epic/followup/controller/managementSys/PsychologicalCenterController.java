package com.epic.followup.controller.managementSys;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.service.managementSys.PsychologicalCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@CrossOrigin
@RequestMapping("/managementSystem/center")
public class PsychologicalCenterController {

    @Autowired
    PsychologicalCenterService centerService;

    // 根据条件查询
    @PostMapping("/find")
    @ResponseBody
    public JSONObject findCenter(@RequestBody JSONObject params, HttpSession session){

        params.put("userUniversityId", session.getAttribute("universityId"));

        return centerService.findCenter(params);
    }

    // 删除
    @PostMapping("/delete")
    @ResponseBody
    public JSONObject deleteCenter(@RequestBody JSONObject params){

        return centerService.deleteCenter(params);
    }

    // 新增
    @PostMapping("/insert")
    @ResponseBody
    public JSONObject insertCenter(@RequestBody JSONObject params){

        return centerService.insertCenter(params);
    }

    // 编辑
    @PostMapping("/edit")
    @ResponseBody
    public JSONObject editCenter(@RequestBody JSONObject params){

        return centerService.editCenter(params);
    }
}
