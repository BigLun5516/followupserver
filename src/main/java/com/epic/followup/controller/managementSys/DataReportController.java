package com.epic.followup.controller.managementSys;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.service.managementSys.DataReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * 数据报表部分
 */
@RestController
@RequestMapping("/managementSystem/report")
public class DataReportController {

    @Autowired
    DataReportService dataReportService;

    /**
     * 根据当前登录用户的学校的身份查看该学校生源地的信息
     * @return
     */
    @PostMapping("/universitySrc")
    public JSONObject universitySourceDistribution(HttpSession session, @RequestBody JSONObject param) {

        param.put("userUniversityId", session.getAttribute("universityId"));
        JSONObject res = dataReportService.universitySourceDistribution(param);
        return res;
    }

    /**
     * 根据当前登录用户的学校的身份查看该学校不同院系学生人数
     * @return
     */
    @PostMapping("/collegeStuNum")
    public JSONObject collegeStuNumDistribution(HttpSession session, @RequestBody JSONObject param) {

        param.put("userUniversityId", session.getAttribute("universityId"));
        JSONObject res = dataReportService.collegeStuNumDistribution(param);
        return res;
    }

    /**
     * 根据发送的学院信息来统计该学院中不同省份的人数
     * @param param
     * @return
     */
    @PostMapping("/getCollegeSrc")
    public JSONObject getCollegeSourceDistribution(@RequestBody JSONObject param) {

        JSONObject res = dataReportService.getCollegeSourceDistribution(param);
        return res;
    }

    /**
     * 根据当前登录用户的学校的身份获取评测数据统计
     * @return
     */
    @PostMapping("/evaluation")
    public JSONObject getEvaluationReport(HttpSession session, @RequestBody JSONObject param) {

        param.put("userUniversityId", session.getAttribute("universityId"));
        JSONObject res = dataReportService.getEvaluationReport(param);
        return res;
    }
}
