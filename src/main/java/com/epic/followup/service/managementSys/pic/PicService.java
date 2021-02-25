package com.epic.followup.service.managementSys.pic;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

@Service
public interface PicService {

    JSONObject searchPic(JSONObject params);
}
