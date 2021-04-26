package com.epic.followup.service.managementSys.scale;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

@Service
public interface ScaleService {


    JSONObject getScaleList(JSONObject params);

    JSONObject editScaleStatus(JSONObject params);

    JSONObject DeleteScale(JSONObject params);

    JSONObject findScaleName(Integer uid);


}
