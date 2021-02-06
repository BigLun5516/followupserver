package com.epic.followup.service.managementSys;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

@Service
public interface CollegeService {

    // 根据条件查询
    JSONObject findCollege(JSONObject params);

    // 删除
    JSONObject deleteCollege(JSONObject params);

    // 新增
    JSONObject insertCollege(JSONObject params);

    // 编辑
    JSONObject editCollege(JSONObject params);
}
