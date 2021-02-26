package com.epic.followup.service.managementSys.impl.pic;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.managementSys.pic.PicModel;
import com.epic.followup.repository.managementSys.pic.PicRepository;
import com.epic.followup.service.managementSys.pic.PicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PicServiceImpl implements PicService {

    @Autowired
    private PicRepository picRepository;

    @Override
    public JSONObject findPic(JSONObject params) {

        JSONObject res = new JSONObject();

        String picName = params.getString("picName");
        String picType = params.getString("picType");
        String picStatus = params.getString("picStatus");
        JSONArray filterDates = params.getJSONArray("filterDates");
        String minDate, maxDate;

        Integer pageNum = params.getInteger("pageNum");
        Integer pageSize = params.getInteger("pageSize");

        if(picName == "" || picName == null ){
            picName = "%%";
        } else {
            picName = "%" + picName + "%";
        }
        if(picType == "" || picName == null ){
            picType = "%%";
        }
        if(picStatus == "" || picName == null ){
            picStatus = "%%";
        }
        if(filterDates == null ){
            minDate = "1900-1-1";
            maxDate = "3000-1-1";
        } else {
            minDate = (String) filterDates.get(0);
            maxDate = (String) filterDates.get(1);
        }

        List<PicModel> picByQuery = picRepository.findPicByQuery(
                picName, picType, picStatus, minDate, maxDate, PageRequest.of(pageNum - 1, pageSize)
        );

        // 返回参数：totalNum
        res.put("totalNum", picRepository.countPicByQuery(
                picName, picType, picStatus, minDate, maxDate
        ));

        // 返回参数：data
        List<Map<String, Object>> data = new ArrayList<>();
        for (PicModel picModel : picByQuery) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", picModel.getPicId());
            item.put("picName", picModel.getPicName());
            item.put("picType", picModel.getPicType());
            item.put("picStatus", picModel.getPicStatus());
            item.put("createTime", picModel.getCreateTime());
            String picDetailsStr = null;
            if (picModel.getPicDetails() != null){
                picDetailsStr = new String(picModel.getPicDetails());
            }
            item.put("picDetails", picDetailsStr);
            data.add(item);
        }
        res.put("data", data);

        res.put("errorCode", 200);
        res.put("errorMsg", "查询成功");
        return res;
    }

    @Override
    public JSONObject insertPic(JSONObject params) {

        // 获取请求参数
        String picName = params.getString("picName");
        String picType = params.getString("picType");
        String picStatus = params.getString("picStatus");
        String picDetailsStr = params.getString("picDetails");
        String createTime = params.getString("createTime");

        JSONObject res = new JSONObject();

        PicModel picModel = new PicModel();
        picModel.setPicName(picName);
        picModel.setPicType(picType);
        picModel.setPicStatus(picStatus);
        if (picDetailsStr != null && picDetailsStr != ""){
            picModel.setPicDetails(picDetailsStr.getBytes());
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (createTime != "") {
            try {
                picModel.setCreateTime(dateFormat.parse(createTime));
            } catch (ParseException e) {
                e.printStackTrace();
                res.put("errorCode", 500);
                res.put("errorMsg", "日期格式错误");
                return res;
            }
        }

        picRepository.save(picModel);

        res.put("errorCode", 200);
        res.put("errorMsg", "插入成功");
        return res;

    }

    @Override
    public JSONObject deletePic(JSONObject params) {

        // 获取参数
        Long id = params.getLong("id");

        JSONObject res = new JSONObject();

        try {
            picRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            res.put("errorCode", 502);
            res.put("errorMsg", "id错误，没有这个图文");
            return res;
        }

        res.put("errorCode", 200);
        res.put("errorMsg", "删除成功");
        return res;
    }

    @Override
    public JSONObject editPic(JSONObject params) {

        // 获取请求参数
        Long id = params.getLong("id");
        String picName = params.getString("picName");
        String picType = params.getString("picType");
        String picStatus = params.getString("picStatus");
        String picDetailsStr = params.getString("picDetails");

        JSONObject res = new JSONObject();

        Optional<PicModel> optional = picRepository.findById(id);
        if (!optional.isPresent()){
            res.put("errorCode", 502);
            res.put("errorMsg", "没有这个图文");
            return res;
        }
        PicModel picModel = optional.get();
        picModel.setPicName(picName);
        picModel.setPicType(picType);
        picModel.setPicStatus(picStatus);
        if (picDetailsStr != null && picDetailsStr != ""){
            picModel.setPicDetails(picDetailsStr.getBytes());
        } else {
            picModel.setPicDetails(null);
        }

        picRepository.save(picModel);

        res.put("errorCode", 200);
        res.put("errorMsg", "编辑成功");
        return res;
    }
}
