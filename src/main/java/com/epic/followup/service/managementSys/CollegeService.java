package com.epic.followup.service.managementSys;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

@Service
public interface CollegeService {

    JSONObject getCollegeData(String universityName, String collegeName);
}
