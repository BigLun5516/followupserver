package com.epic.followup.controller.managementSys;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.service.managementSys.OperationService;
import com.epic.followup.service.managementSys.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin    //允许跨域
@RequestMapping("/managementSystem/university")
public class UniversityController {

    @Autowired
    UniversityService universityService;

    @Autowired
    OperationService operationService;

    // 更多高校接口
    @PostMapping("/moreUniversity")
    @ResponseBody
    public JSONObject moreUniversity(@RequestBody JSONObject params){

        return universityService.findAllUniversity(params);
    }

}
