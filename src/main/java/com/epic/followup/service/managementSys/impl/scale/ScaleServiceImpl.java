package com.epic.followup.service.managementSys.impl.scale;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.managementSys.UniversityModel;
import com.epic.followup.model.managementSys.scale.ScaleForbiddenModel;
import com.epic.followup.model.managementSys.scale.ScaleModel;
import com.epic.followup.repository.managementSys.UniversityRepository;
import com.epic.followup.repository.managementSys.UserRepository;
import com.epic.followup.repository.managementSys.scale.ScaleForbiddenRepository;
import com.epic.followup.repository.managementSys.scale.ScaleRepository;
import com.epic.followup.service.managementSys.scale.ScaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class ScaleServiceImpl implements ScaleService {

    @Autowired
    private ScaleRepository scaleRepository;

    @Autowired
    private ScaleForbiddenRepository scaleForbiddenRepository;

    @Autowired
    private UniversityRepository universityRepository;

    /**
     * 根据条件查询量表
     * @param params
     * @return
     */
    @Override
    public JSONObject getScaleList(JSONObject params) {

        String scaleName = params.getString("scalename");
        String schoolName = params.getString("schoolname");
        Integer isnz = params.getInteger("isnz");
        Integer status = params.getInteger("status"); // 启用：1  禁用：0
        Integer pageNum = params.getInteger("pageNum");
        Integer pageSize = params.getInteger("pageSize");
        Integer userUniversityId = params.getInteger("userUniversityId");

//         查询条件处理
        if(scaleName == null) {
            scaleName = "";
        }
        if((schoolName == null)||(schoolName == "")) {
            schoolName = "";
        }
        if (isnz == null) {
            isnz = -1;
        }
        if (status == null) {
            status = -1;
        }


        List<Object> scaleByQuery = scaleRepository.findScaleByQuery(scaleName, schoolName, isnz, status, userUniversityId);

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
            item.put("isnz", o[2]);
            item.put("status", o[3]);
            item.put("schoolName", o[4]);
            scaleManagementTable.add(item);
        }
        res.put("errorCode",200);
        res.put("errorMsg","查询成功");
        res.put("page", pageNum);
        res.put("totalNum", scaleByQuery.size());
        res.put("scaleManagementTable",scaleManagementTable);
        return res;

    }

    /**
     * 修改量表的状态
     * @param params
     * @param status  0:禁用  1：启用
     * @return
     */
    @Transactional
    @Override
    public JSONObject editScaleStatus(JSONObject params) {
        JSONObject res = new JSONObject();

        Long scaleId = params.getLong("id");
        Integer status = params.getInteger("status");
        Integer userUniversityId = params.getInteger("userUniversityId");

        Optional<UniversityModel> universityOptional = universityRepository.findById(userUniversityId);
        if (!universityOptional.isPresent()) {
            res.put("errorCode", 500);
            res.put("errorMsg", "此用户无权操作");
            return res;
        }

        UniversityModel universityModel = universityOptional.get();

        // 获取量表
        Optional<ScaleModel> byScaleId = scaleRepository.findByScaleId(scaleId);
        if(!byScaleId.isPresent()){
            res.put("errorCode", 500);
            res.put("errorMsg", "没有这个量表");
            return res;
        }
        ScaleModel scaleModel = byScaleId.get();

        // 修改量表状态
        if (status == 0) {
            // 改为启用
            try {
                scaleForbiddenRepository.deleteByScaleIdAndUniversityName(scaleId, universityModel.getUniversityName());
            } catch (EmptyResultDataAccessException e) {
                res.put("errorCode", 500);
                res.put("errorMsg", "状态本来就是已启用");
            }
            res.put("errorCode", 200);
            res.put("errorMsg", "启用成功");

        } else {
            // 改为禁用
            Optional<ScaleForbiddenModel> optional = scaleForbiddenRepository.getScaleForbiddenModelByScaleIdAndUniversityName(scaleId, scaleModel.getSchoolName());
            if (optional.isPresent()) {
                res.put("errorCode", 500);
                res.put("errorMsg", "状态本来就是禁用中");
            } else {
                ScaleForbiddenModel scaleForbiddenModel = new ScaleForbiddenModel();
                scaleForbiddenModel.setScaleId(scaleId);
                scaleForbiddenModel.setUniversityName(universityModel.getUniversityName());
                scaleForbiddenRepository.save(scaleForbiddenModel);
                res.put("errorCode", 200);
                res.put("errorMsg", "禁用成功");
            }

        }
        return res;
    }

    /**
     * 删除量表
     * @param params
     * @return
     */
    @Transactional
    @Override
    public JSONObject DeleteScale(JSONObject params) {
        JSONObject res = new JSONObject();
        Long scaleId = params.getLong("id");

        // 判断是否是内置表
        Optional<ScaleModel> optional = scaleRepository.findById(scaleId);
        if (!optional.isPresent()) {
            res.put("errorCode", 500);
            res.put("errorMsg", "没有这个量表");
            return res;
        }
        ScaleModel scaleModel = optional.get();
        if (scaleModel.getIsnz().equals(Integer.valueOf(1))) {
            res.put("errorCode", 500);
            res.put("errorMsg", "内置量表无法删除");
            return res;
        }


        // 删除量表
        try {
            scaleRepository.deleteById(scaleId);
        } catch (EmptyResultDataAccessException e) {
            res.put("errorCode", 500);
            res.put("errorMsg", "没有这个量表");
            return res;
        }

        // 删除量表禁用表
        scaleForbiddenRepository.deleteByScaleId(scaleId);

        res.put("errorCode", 200);
        res.put("errorMsg", "删除量表成功");

        return res;
    }

    @Override
    public JSONObject findScaleName(Integer uid){
        JSONObject res = new JSONObject();
        List<Object> scaleList=scaleRepository.findByUid(uid);
        List<Map<String, Object>> data = new ArrayList<>();
        for (Object o : scaleList) {
            Map<String, Object> item = new HashMap<>();
            Object[] obj = (Object[]) o;
            item.put("scaleId", obj[0]);
            item.put("name", obj[2]+"("+obj[1]+")");
            data.add(item);
        }
        res.put("errorCode", 200);
        res.put("errorMsg", "查询成功");
        res.put("data",data);
        return res;
    }

}
