package com.epic.followup.service.managementSys.impl.scale;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.managementSys.scale.ScaleModel;
import com.epic.followup.repository.managementSys.scale.ScaleRepository;
import com.epic.followup.service.managementSys.scale.ScaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ScaleServiceImpl implements ScaleService {

    @Autowired
    private ScaleRepository scaleRepository;

    @Override
    public JSONObject getScaleList(JSONObject params) {

        String scaleName = params.getString("scalename");
        String schoolName = params.getString("schoolname");
        String classify = params.getString("classify");
        Integer isnz = params.getInteger("isnz");
        Integer status = params.getInteger("status");
        Integer pageNum = params.getInteger("pageNum");
        Integer pageSize = params.getInteger("pageSize");

//         查询条件处理
        if(scaleName == null) {
            scaleName = "";
        }
        if((schoolName == null)||(schoolName == "")) {
            schoolName = "%%";
        }
        if((classify == null)||(classify == "")) {
            classify = "%%";
        }
        if (isnz == null) {
            isnz = -1;
        }
        if (status == null) {
            status = -1;
        }

        List<Object> scaleByQuery = scaleRepository.findScaleByQuery(scaleName, schoolName, classify, isnz, status);

        JSONObject res = new JSONObject();

        if (scaleByQuery == null) {
            res.put("errorCode", 500);
            res.put("errorMsg", "没有数据");
            res.put("page", pageNum);
            res.put("totalNum", 0);
            return res;
        }
        List<Map<String, Object>> scaleManagementTable = new ArrayList<>();
        int i;
        for (i = (pageNum - 1) * pageSize; i < pageNum * pageSize && i < scaleByQuery.size(); i++) {
            Object[] o = (Object[])scaleByQuery.get(i);
            Map<String, Object> item = new HashMap<>();
            item.put("id", o[0]);
            item.put("scaleName", o[1]);
            item.put("schoolName", o[2]);
            item.put("classify", o[3]);
            item.put("isnz", o[4]);
            item.put("status", o[5]);
            item.put("num", o[6]);
            item.put("organization", o[7]);
            scaleManagementTable.add(item);
        }
        res.put("errorCode",200);
        res.put("errorMsg","查询成功");
        res.put("page", pageNum);
        res.put("totalNum", scaleByQuery.size());
        res.put("scaleManagementTable",scaleManagementTable);
        return res;

    }

    @Override
    public JSONObject putScaleDisable(JSONObject params) {
        JSONObject res = new JSONObject();

        Integer scaleId = params.getInteger("id");
        Integer status = params.getInteger("status");
        Optional<ScaleModel> byScaleId = scaleRepository.findByScaleId(Long.valueOf(scaleId));
        if(!byScaleId.isPresent()){
            res.put("errorCode", 500);
            res.put("errorMsg", "没有这个量表");
            return res;
        }
        ScaleModel scaleModel = byScaleId.get();
        scaleModel.setStatus(status);
        ScaleModel save = scaleRepository.save(scaleModel);
        if(save == null){
            res.put("errorCode", 500);
            res.put("errorMsg", "修改量表状态失败");
        } else {
            res.put("errorCode", 200);
            res.put("errorMsg", "修改量表状态成功");
        }
        return res;
    }

    @Override
    public JSONObject DeleteScale(JSONObject params) {
        JSONObject res = new JSONObject();
        Integer scaleId = params.getInteger("id");

        Optional<ScaleModel> byScaleId = scaleRepository.findByScaleId(Long.valueOf(scaleId));
        scaleRepository.delete(byScaleId.get());
        Optional<ScaleModel> scale = scaleRepository.findByScaleId(Long.valueOf(scaleId));
        if(scale.isPresent()){
            res.put("errorCode", 500);
            res.put("errorMsg", "删除量表失败");
        } else {
            res.put("errorCode", 200);
            res.put("errorMsg", "删除量表成功");
        }
        return res;
    }
}
