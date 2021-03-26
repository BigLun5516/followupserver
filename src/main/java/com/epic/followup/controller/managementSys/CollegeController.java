package com.epic.followup.controller.managementSys;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.managementSys.CollegeModel;
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

    @Autowired
    private CollegeRepository collegeRepository;

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

    // 根据学校id查找学院
    @PostMapping("/findbyUid")
    @ResponseBody
    public JSONObject findbyUid(@RequestBody JSONObject params){

        JSONObject res=new JSONObject();
        Integer uid=params.getInteger("uid");
        List<CollegeModel> data = collegeRepository.findByUniversityId(uid);
        res.put("errorCode", 200);
        res.put("errorMsg", "查找成功");
        res.put("data", data);
        return res;
    }
}
