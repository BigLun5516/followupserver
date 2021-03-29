package com.epic.followup.controller.managementSys;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.repository.managementSys.UniversityRepository;
import com.epic.followup.service.managementSys.OperationService;
import com.epic.followup.service.managementSys.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

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

    // 根据条件查询
    @PostMapping("/find")
    @ResponseBody
    public JSONObject findUniversity(@RequestBody JSONObject params, HttpSession session){

        params.put("userUniversityId", session.getAttribute("universityId"));

        return universityService.findUniversity(params);
    }

    // 删除
    @PostMapping("/delete")
    @ResponseBody
    public JSONObject deleteUniversity(@RequestBody JSONObject params){

        return universityService.deleteUniversity(params);
    }

    // 新增
    @PostMapping("/insert")
    @ResponseBody
    public JSONObject insertUniversity(@RequestBody JSONObject params){

        return universityService.insertUniversity(params);
    }

    // 编辑
    @PostMapping("/edit")
    @ResponseBody
    public JSONObject editUniversity(@RequestBody JSONObject params){

        return universityService.editUniversity(params);
    }

    /*
    * 量表
    */
    // 获取高校名称（勿改，小程序前端也调用了此接口）
    @PostMapping("/getAllName")
    @ResponseBody
    public JSONObject getAllUniversityName(){
        return universityService.getAllUniversityName();
    }

    // 后台根据登录的用户决定能看到哪些学校
    @PostMapping("/getAllNameByUid")
    @ResponseBody
    public JSONObject getAllUniversityNameByUid(HttpSession session){
        return universityService.getAllUniversityNameByUid(session);
    }

}
