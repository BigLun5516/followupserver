package com.epic.followup.service.managementSys;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.temporary.wechat.patient.diary.getAllMoodsResponse;
import org.springframework.stereotype.Service;

@Service
public interface StudentService {

    // 根据条件查询
    JSONObject findStudent(JSONObject params);

    // 删除
    JSONObject deleteStudent(JSONObject params);

    // 新增
    JSONObject insertStudent(JSONObject params);

    // 编辑
    JSONObject editStudent(JSONObject params);

    // 查询心情
    getAllMoodsResponse getAllMoods(JSONObject params);

    // 获取7天的身体状况
    JSONObject getBodyInfo(JSONObject params);
}
