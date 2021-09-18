package com.epic.followup.service.managementSys.pic;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

@Service
public interface PicService {

    // 查找图文
    JSONObject findPic(JSONObject params);

    // 新增图文
    JSONObject insertPic(JSONObject params);

    // 删除图文
    JSONObject deletePic(JSONObject params);

    // 编辑图文
    JSONObject editPic(JSONObject params);

    // 获取token接口
    JSONObject getToken(JSONObject params) throws Exception;




}
