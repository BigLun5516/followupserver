package com.epic.followup.service.managementSys.impl.pic;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.managementSys.pic.PicModel;
import com.epic.followup.repository.managementSys.pic.PicRepository;
import com.epic.followup.service.managementSys.pic.PicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PicServiceImpl implements PicService {

    @Autowired
    private PicRepository picRepository;

    @Override
    public JSONObject searchPic(JSONObject params) {

        JSONObject res = new JSONObject();

        String picName = params.getString("picName");
        String picType = params.getString("picType");
        String picStatus = params.getString("picStatus");
        JSONArray filterDates = params.getJSONArray("filterDates");
        String minDate = (String)filterDates.get(0);
        String maxDate = (String)filterDates.get(1);

        Integer pageNum = params.getInteger("pageNum");
        Integer pageSize = params.getInteger("pageSize");

        if(picName == null ){
            picName = "%%";
        }
        if(picType == null ){
            picType = "%%";
        }
        if(picStatus == null ){
            picStatus = "%%";
        }
        if(minDate == null ){
            minDate = "";
        }
        if(maxDate == null ){
            maxDate = "";
        }

        List<PicModel> picByQuery = picRepository.findPicByQuery(picName, picType, picStatus, minDate, maxDate);

        res.put("picByQuery", picByQuery);

        return res;
    }
}
