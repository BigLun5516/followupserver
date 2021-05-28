package com.epic.followup.service.managementSys;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface DataReportService {

    /**
     * 根据当前登录用户的学校的身份查看该学校生源地的信息
     * @return
     */
    public JSONObject universitySourceDistribution(JSONObject param);

    /**
     * 根据当前登录用户的学校的身份查看该学校不同院系学生人数
     * @return
     */
    public JSONObject collegeStuNumDistribution(JSONObject param);

    /**
     * 根据发送的学院信息来统计该学院中不同省份的人数
     * @param param
     * @return
     */
    public JSONObject getCollegeSourceDistribution(JSONObject param);

    /**
     * 根据当前登录用户的学校的身份获取评测数据统计
     * @return
     */
    public JSONObject getEvaluationReport(JSONObject param);
}
