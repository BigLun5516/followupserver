package com.epic.followup.service.managementSys;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.managementSys.UniversityModel;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

@Service
public interface UniversityService {

    // 查询所有高校（高校表里的信息，不涉及其他表）
    JSONObject findAllUniversity(@RequestBody JSONObject Params);

    // 高校运营数据界面 所需的所有数据
    JSONObject getUniversityData(JSONObject params);
}
