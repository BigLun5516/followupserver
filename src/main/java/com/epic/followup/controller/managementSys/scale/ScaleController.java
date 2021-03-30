package com.epic.followup.controller.managementSys.scale;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.service.managementSys.scale.ScaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@CrossOrigin
@RequestMapping("/managementSystem/scale")
public class ScaleController {

    @Autowired
    private ScaleService scaleService;

    /**
    * 量表管理
    */

    // 获取量表列表
    @PostMapping("/scaleList")
    @ResponseBody
    public JSONObject getScaleList(@RequestBody JSONObject params, HttpSession session){

        params.put("userUniversityId", session.getAttribute("universityId"));

        return scaleService.getScaleList(params);
    }

    // 量表禁用接口
    @PutMapping("/scaleDisable")
    @ResponseBody
    public JSONObject putScaleDisable(@RequestBody JSONObject params){
        return scaleService.putScaleDisable(params);
    }

    // 量表删除接口
    @DeleteMapping("/scaleDelete")
    @ResponseBody
    public JSONObject DeleteScale(@RequestBody JSONObject params) {
        return scaleService.DeleteScale(params);
    }

    // 后台任务管理获取学校对应的所有量表用于新增
    @PostMapping("/findScaleName")
    @ResponseBody
    public JSONObject findScaleName(HttpSession session){
        return scaleService.findScaleName((Integer)session.getAttribute("universityId"));
    }
}
