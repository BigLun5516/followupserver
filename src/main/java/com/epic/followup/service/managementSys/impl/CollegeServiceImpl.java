package com.epic.followup.service.managementSys.impl;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.managementSys.CollegeModel;
import com.epic.followup.model.managementSys.UniversityModel;
import com.epic.followup.repository.followup2.student.ScaleResult2Repository;
import com.epic.followup.repository.followup2.student.StudentInfoRepository;
import com.epic.followup.repository.managementSys.CollegeRepository;
import com.epic.followup.repository.managementSys.UniversityRepository;
import com.epic.followup.service.managementSys.CollegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CollegeServiceImpl implements CollegeService {

    @Autowired
    CollegeRepository collegeRepository;

    @Autowired
    UniversityRepository universityRepository;

    /**
     * 根据条件查找
     * @param params
     * @return
     */
    @Override
    public JSONObject findCollege(JSONObject params) {

        // 获取请求参数
        String universityName = params.getString("universityName");
        String collegeName = params.getString("collegeName");
        Integer collegeStatus = params.getInteger("collegeStatus");
        List<String> filterDates = params.getObject("filterDates", List.class);
        Integer pageNum = params.getInteger("pageNum");
        Integer pageSize = params.getInteger("pageSize");

        // 处理请求参数
        if (universityName == null){
            universityName = "";
        }
        if (collegeName == null){
            collegeName = "";
        } else {
            // 模糊查询处理
            collegeName = "%" + collegeName + "%";
        }
        if (collegeStatus == null){
            collegeStatus = -1;
        }
        if (filterDates.get(0) == ""){
            filterDates.set(0, "1900-1-1");
        }
        if (filterDates.get(1) == ""){
            filterDates.set(1, "3000-1-1");
        }

        List<Object> collegeModelList = collegeRepository.findCollegeModel(
                universityName, collegeName, collegeStatus, filterDates.get(0), filterDates.get(1), PageRequest.of(pageNum - 1, pageSize));

        JSONObject res = new JSONObject();

        // 学院总数量
        res.put("totalNum", collegeRepository.count());

        // 院系管理表
        List<Map<String, Object>> collegeManagementTable = new ArrayList<>();
        for (Object o : collegeModelList){
            Map<String, Object> item = new HashMap<>();
            Object[] oo = (Object[])o;
            item.put("id", oo[0]);
            item.put("universityName", oo[1]);
            item.put("collegeName", oo[2]);
            item.put("collegeDirector", oo[3]);
            item.put("collegePhone", oo[4]);
            item.put("collegeStuNum", oo[5]);
            item.put("arriveNum", oo[6]);
            item.put("collegeStatus", oo[7]);
            item.put("createTime", oo[8]);
            collegeManagementTable.add(item);
        }
        res.put("collegeManagementTable", collegeManagementTable);

        // 所有高校名单
        List<UniversityModel> universityModelList = universityRepository.findAll();
        List<String> universityList = new ArrayList<>();
        for (UniversityModel universityModel : universityModelList) {
            String name = universityModel.getUniversityName();
            if (name != null){
                universityList.add(name);
            }
        }
        res.put("universityList", universityList);


        res.put("errorCode", 200);
        res.put("errorMsg", "查询成功");
        return res;
    }

    /**
     * 删除
     * @param params
     * @return
     */
    @Override
    public JSONObject deleteCollege(JSONObject params) {

        // 请求参数
        Integer id = params.getInteger("id");

        JSONObject res = new JSONObject();

        try {
            collegeRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            res.put("errorCode", 500);
            res.put("errorMsg", "删除失败");
            return res;
        }

        res.put("errorCode", 200);
        res.put("errorMsg", "删除成功");
        return res;
    }

    /**
     * 新增
     * @param params
     * @return
     */
    @Override
    public JSONObject insertCollege(JSONObject params) {

        // 获取参数
        String universityName = params.getString("universityName");
        String collegeName = params.getString("collegeName");
        String collegeDirector = params.getString("collegeDirector");
        String collegePhone = params.getString("collegePhone");
        Integer collegeStuNum = params.getInteger("collegeStuNum");
        Integer arriveNum = params.getInteger("arriveNum");
        Integer collegeStatus = params.getInteger("collegeStatus");
        String createTimeStr = params.getString("createTime");

        JSONObject res = new JSONObject();

        CollegeModel collegeModel = new CollegeModel();
        UniversityModel universityModel = universityRepository.findByUniversityName(universityName);
        if (universityModel == null){
            res.put("errorCode", 501);
            res.put("errorMsg", "没有此高校");
            return res;
        }
        collegeModel.setUniversityId(universityModel.getUniversityId());
        collegeModel.setCollegeName(collegeName);
        collegeModel.setCollegeManager(collegeDirector);
        collegeModel.setCollegePhone(collegePhone);
        collegeModel.setStudentNum(collegeStuNum);
        collegeModel.setArriveNum(arriveNum);
        collegeModel.setCollegeStatus(collegeStatus);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            collegeModel.setCreateTime(dateFormat.parse(createTimeStr));
        } catch (ParseException e) {
            e.printStackTrace();
            res.put("errorCode", 502);
            res.put("errorMsg", "createTime日期格式错误");
            return res;
        }

        collegeRepository.save(collegeModel);

        res.put("errorCode", 200);
        res.put("errorMsg", "新增成功");
        return res;
    }

    /**
     * 编辑
     * @param params
     * @return
     */
    @Override
    public JSONObject editCollege(JSONObject params) {

        // 获取参数
        Integer id = params.getInteger("id");
        String universityName = params.getString("universityName");
        String collegeName = params.getString("collegeName");
        String collegeDirector = params.getString("collegeDirector");
        String collegePhone = params.getString("collegePhone");
        Integer collegeStuNum = params.getInteger("collegeStuNum");
        Integer arriveNum = params.getInteger("arriveNum");
        Integer collegeStatus = params.getInteger("collegeStatus");
        String createTimeStr = params.getString("createTime");

        JSONObject res = new JSONObject();

        Optional<CollegeModel> optional = collegeRepository.findById(id);
        if (!optional.isPresent()){
            res.put("errorCode", 503);
            res.put("errorMsg", "没有这个院系");
            return res;
        }
        CollegeModel collegeModel = optional.get();
        UniversityModel universityModel = universityRepository.findByUniversityName(universityName);
        if (universityModel == null){
            res.put("errorCode", 501);
            res.put("errorMsg", "没有此高校");
            return res;
        }
        collegeModel.setUniversityId(universityModel.getUniversityId());
        collegeModel.setCollegeName(collegeName);
        collegeModel.setCollegeManager(collegeDirector);
        collegeModel.setCollegePhone(collegePhone);
        collegeModel.setStudentNum(collegeStuNum);
        collegeModel.setArriveNum(arriveNum);
        collegeModel.setCollegeStatus(collegeStatus);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            collegeModel.setCreateTime(dateFormat.parse(createTimeStr));
        } catch (ParseException e) {
            e.printStackTrace();
            res.put("errorCode", 502);
            res.put("errorMsg", "createTime日期格式错误");
            return res;
        }

        collegeRepository.save(collegeModel);

        res.put("errorCode", 200);
        res.put("errorMsg", "编辑成功");
        return res;
    }
}
