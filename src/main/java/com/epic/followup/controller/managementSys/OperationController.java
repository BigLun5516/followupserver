package com.epic.followup.controller.managementSys;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.managementSys.CollegeModel;
import com.epic.followup.model.managementSys.UniversityModel;
import com.epic.followup.repository.managementSys.CollegeRepository;
import com.epic.followup.repository.managementSys.UniversityRepository;
import com.epic.followup.service.managementSys.OperationService;
import com.epic.followup.service.managementSys.PublicityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@CrossOrigin
@RequestMapping("/managementSystem/operation")
public class OperationController {

    @Autowired
    private OperationService operationService;

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private CollegeRepository collegeRepository;

    // 运营数据初始化
    @RequestMapping(value = "/initial", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getInitialInfo() {
        return operationService.getInitialInfo();
    }

    // 高校推广分布界面
    @Autowired
    private PublicityService publicityService;

    @RequestMapping(value = "/publicity", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject publicityData() {
        return publicityService.publicityUniversity();
    }

    // 心理咨询中心运营数据接口
    @RequestMapping(value = "/psychologicalConsult", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getPsychologicalConsultData(@RequestBody JSONObject operationParams) {
        String universityName = operationParams.getString("universityName");
        return operationService.getPsychologicalConsultData(universityName);
    }

    // 高校运营数据界面接口
    @PostMapping("/university")
    @ResponseBody
    public JSONObject operationUniversity(@RequestBody JSONObject params) {

        return operationService.getUniversityData(params);
    }

    // 院系运营数据接口
    @PostMapping("/college")
    @ResponseBody
    public JSONObject operationCollege(@RequestBody JSONObject params) {

        String universityName = params.getString("universityName");
        String collegeName = params.getString("collegeName");

        return operationService.getCollegeData(universityName, collegeName);
    }

    //学校报表
    @PostMapping("/schoolReport")
    @ResponseBody
    public JSONObject schoolReport(HttpSession session) {
        Integer i = (Integer) session.getAttribute("universityId");
        UniversityModel u = universityRepository.findByUniversityId(i);
        JSONObject q = new JSONObject();
        q.put("universityName", u.getUniversityName());
        return operationService.getUniversityData(q);
    }

    // 院系运营数据接口
    @PostMapping("/collegeReport")
    @ResponseBody
    public JSONObject collegeReport(HttpSession session) {

        Integer uid = (Integer) session.getAttribute("universityId");
        UniversityModel u = universityRepository.findByUniversityId(uid);
        List<Integer> list=(List<Integer>)session.getAttribute("collegeId");
        CollegeModel c = collegeRepository.findByCollegeId(list.get(0));
        return operationService.getCollegeData(u.getUniversityName(), c.getCollegeName());
    }
}
