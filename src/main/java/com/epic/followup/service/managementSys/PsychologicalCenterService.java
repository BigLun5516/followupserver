package com.epic.followup.service.managementSys;

import com.alibaba.fastjson.JSONObject;

public interface PsychologicalCenterService {
    // 根据条件查询
    JSONObject findCenter(JSONObject params);

    // 删除
    JSONObject deleteCenter(JSONObject params);

    // 新增
    JSONObject insertCenter(JSONObject params);

    // 编辑
    JSONObject editCenter(JSONObject params);
}
