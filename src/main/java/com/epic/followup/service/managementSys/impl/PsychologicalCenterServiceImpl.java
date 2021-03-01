package com.epic.followup.service.managementSys.impl;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.managementSys.PsychologicalCenterModel;
import com.epic.followup.model.managementSys.UniversityModel;
import com.epic.followup.repository.managementSys.PsychologicalCenterRepository;
import com.epic.followup.repository.managementSys.UniversityRepository;
import com.epic.followup.service.managementSys.PsychologicalCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PsychologicalCenterServiceImpl implements PsychologicalCenterService {

    @Autowired
    PsychologicalCenterRepository centerRepository;

    @Autowired
    UniversityRepository universityRepository;

    @Override
    public JSONObject findCenter(JSONObject params) {

        // 获取请求参数
        String universityName = params.getString("universityName");
        String centralName = params.getString("centralName");
        Integer centralStatus = params.getInteger("centralStatus");
        List<String> filterDates = params.getObject("filterDates", List.class);
        Integer pageNum = params.getInteger("pageNum");
        Integer pageSize = params.getInteger("pageSize");

        // 处理请求参数
        if (universityName == null){
            universityName = "";
        }
        if (centralName == null){
            centralName = "";
        } else {
            centralName = "%" + centralName + "%";
        }
        if (centralStatus == null){
            centralStatus = -1;
        }
        if (filterDates.get(0) == ""){
            filterDates.set(0, "1900-1-1");
        }
        if (filterDates.get(1) == ""){
            filterDates.set(1, "3000-1-1");
        }

        JSONObject res = new JSONObject();

        // 返回参数：中心总数量
        res.put("totalNum", centerRepository.countPsychologicalCenterModel(
                universityName, centralName, centralStatus, filterDates.get(0), filterDates.get(1)
        ));

        // 返回参数：中心管理表
        List<Object> centerModelList = centerRepository.findPsychologicalCenterModel(
                universityName, centralName, centralStatus, filterDates.get(0), filterDates.get(1),
                PageRequest.of(pageNum - 1, pageSize)
        );

        List<Map<String, Object>> centralManagementTable = new ArrayList<>();
        for (Object o : centerModelList) {
            Object[] oo = (Object[]) o;
            Map<String, Object> item = new HashMap<>();
            item.put("id", oo[0]);
            item.put("universityName", oo[1]);
            item.put("centralName", oo[2]);
            item.put("centralDirector", oo[3]);
            item.put("centralPhone", oo[4]);
            item.put("centralNum", oo[5]);
            item.put("acceptNum", oo[6]);
            item.put("centralStatus", oo[7]);
            item.put("createTime", oo[8]);
            centralManagementTable.add(item);
        }
        res.put("centralManagementTable", centralManagementTable);

        // 返回参数：数据库中所有高校名单
        List<UniversityModel> universityModelList = universityRepository.findAll();
        List<String> universityList = new ArrayList<>();
        for (UniversityModel universityModel : universityModelList) {
            String uniName = universityModel.getUniversityName();
            if (uniName != null){
                universityList.add(uniName);
            }
        }
        res.put("universityList", universityList);

        res.put("errorCode", 200);
        res.put("errorMsg", "查询成功");
        return res;
    }

    @Override
    public JSONObject deleteCenter(JSONObject params) {

        // 获取请求参数
        Integer id = params.getInteger("id");

        JSONObject res = new JSONObject();

        // 心理咨询中心表中删除
        try {
            centerRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            res.put("errorCode", 500);
            res.put("errorMsg", "删除失败");
            return res;
        }

        // 高校表中删除
        List<UniversityModel> universityModelList = universityRepository.findByCenterId(id);
        for (UniversityModel universityModel : universityModelList) {
            universityModel.setCenterId(null);
            universityRepository.save(universityModel);
        }

        res.put("errorCode", 200);
        res.put("errorMsg", "删除成功");
        return res;
    }

    @Override
    public JSONObject insertCenter(JSONObject params) {

        // 获取请求参数
        String universityName = params.getString("universityName");
        String centralName = params.getString("centralName");
        String centralDirector = params.getString("centralDirector");
        String centralPhone = params.getString("centralPhone");
        Integer centralNum = params.getInteger("centralNum");
        Integer acceptNum = params.getInteger("acceptNum");
        Integer centralStatus = params.getInteger("centralStatus");
        String createTime = params.getString("createTime");

        JSONObject res = new JSONObject();


        // 插入在心理咨询表中
        PsychologicalCenterModel centerModel = new PsychologicalCenterModel();
        centerModel.setCenterName(centralName);
        centerModel.setCenterManager(centralDirector);
        centerModel.setTel(centralPhone);
        centerModel.setTotalNum(centralNum);
        centerModel.setAcceptNum(acceptNum);
        centerModel.setStatus(centralStatus);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse(createTime);
            centerModel.setCreateDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
            res.put("errorCode", 502);
            res.put("errorMsg", "时间格式错误");
            return res;
        }
        centerModel = centerRepository.save(centerModel);

        // 修改高校的心理咨询中心id
        UniversityModel universityModel = universityRepository.findByUniversityName(universityName);
        if(universityModel == null){
            res.put("errorCode", 501);
            res.put("errorMsg", "没有此高校");
            return res;
        }
        universityModel.setCenterId(centerModel.getCenterId());
        universityRepository.save(universityModel);

        res.put("errorCode", 200);
        res.put("errorMsg", "新增成功");
        return res;
    }

    @Override
    public JSONObject editCenter(JSONObject params) {

        // 获取请求参数
        Integer id = params.getInteger("id");
        String universityName = params.getString("universityName");
        String centralName = params.getString("centralName");
        String centralDirector = params.getString("centralDirector");
        String centralPhone = params.getString("centralPhone");
        Integer centralNum = params.getInteger("centralNum");
        Integer acceptNum = params.getInteger("acceptNum");
        Integer centralStatus = params.getInteger("centralStatus");
        String createTime = params.getString("createTime");

        JSONObject res = new JSONObject();


        // 插入在心理咨询表中
        Optional<PsychologicalCenterModel> optional = centerRepository.findById(id);
        if (!optional.isPresent()){
            res.put("errorCode", 503);
            res.put("errorMsg", "没有查到这个心理咨询中心化");
            return res;
        }
        PsychologicalCenterModel centerModel = optional.get();
        centerModel.setCenterName(centralName);
        centerModel.setCenterManager(centralDirector);
        centerModel.setTel(centralPhone);
        centerModel.setTotalNum(centralNum);
        centerModel.setAcceptNum(acceptNum);
        centerModel.setStatus(centralStatus);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse(createTime);
            centerModel.setCreateDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
            res.put("errorCode", 502);
            res.put("errorMsg", "时间格式错误");
            return res;
        }
        centerModel = centerRepository.save(centerModel);

        // 修改高校的心理咨询中心id
        UniversityModel universityModel = universityRepository.findByUniversityName(universityName);
        if(universityModel == null){
            res.put("errorCode", 501);
            res.put("errorMsg", "没有此高校");
            return res;
        }
        Integer oldCenterId = universityModel.getCenterId();
        if (!oldCenterId.equals(id)) {
            universityModel.setCenterId(centerModel.getCenterId());
            universityRepository.save(universityModel);
        }

        res.put("errorCode", 200);
        res.put("errorMsg", "编辑成功");
        return res;
    }
}
