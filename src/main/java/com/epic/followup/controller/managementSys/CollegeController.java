package com.epic.followup.controller.managementSys;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.repository.managementSys.CollegeRepository;
import com.epic.followup.service.managementSys.CollegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin
@RequestMapping("/managementSystem/college")
public class CollegeController {

    @Autowired
    CollegeService collegeService;

    // 根据条件查询
    @PostMapping("/find")
    @ResponseBody
    public JSONObject findCollege(@RequestBody JSONObject params){

        return collegeService.findCollege(params);
    }

    // 删除
    @PostMapping("/delete")
    @ResponseBody
    public JSONObject deleteCollege(@RequestBody JSONObject params){

        return collegeService.deleteCollege(params);
    }

    // 新增
    @PostMapping("/insert")
    @ResponseBody
    public JSONObject insertCollege(@RequestBody JSONObject params){

        return collegeService.insertCollege(params);
    }

    // 编辑
    @PostMapping("/edit")
    @ResponseBody
    public JSONObject editCollege(@RequestBody JSONObject params){

        return collegeService.editCollege(params);
    }
}
