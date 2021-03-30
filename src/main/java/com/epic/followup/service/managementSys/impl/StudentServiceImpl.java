package com.epic.followup.service.managementSys.impl;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.followup2.student.StudentInfo;
import com.epic.followup.model.managementSys.CollegeModel;
import com.epic.followup.model.managementSys.UniversityModel;
import com.epic.followup.repository.followup2.student.StudentInfoRepository;
import com.epic.followup.repository.managementSys.CollegeRepository;
import com.epic.followup.repository.managementSys.UniversityRepository;
import com.epic.followup.service.managementSys.StudentService;
import net.bytebuddy.build.Plugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    StudentInfoRepository studentRepository;

    @Autowired
    UniversityRepository universityRepository;

    @Autowired
    CollegeRepository collegeRepository;

    /**
     * 根据条件查询学生
     * @param params
     * @return
     */
    @Override
    public JSONObject findStudent(JSONObject params) {

        // 获取请求参数
        String studentName = params.getString("stname");
        String universityName = params.getString("department");
        Integer studentType = params.getInteger("stype");
        List<String> filterDates = params.getObject("filterDates", List.class);
        Integer pageNum = params.getInteger("pageNum");
        Integer pageSize = params.getInteger("pageSize");
        Integer userUniversityId = params.getInteger("userUniversityId");
        Integer userCollegeId = params.getInteger("userCollegeId");

        // 处理为空的请求参数
        if (studentName == null){
            studentName = "";
        } else {
            // 模糊查询处理
            studentName = "%" + studentName + "%";
        }
        if (universityName == null){
            universityName = "";
        }
        if (studentType == null){
            studentType = -1;
        }
        if (filterDates.get(0) == ""){
            filterDates.set(0, "1900-1-1");
        }
        if (filterDates.get(1) == ""){
            filterDates.set(1, "3000-1-1");
        }

        JSONObject res = new JSONObject();

        // 返回参数：学生总数量
        res.put("totalNum", studentRepository.countStudentInfo(
                studentName, universityName, studentType, filterDates.get(0), filterDates.get(1),
                userUniversityId, userCollegeId
        ));

        // 返回参数：高校管理表
        List<StudentInfo> studentInfoList = studentRepository.findStudentInfo(
                studentName, universityName, studentType, filterDates.get(0), filterDates.get(1),
                userUniversityId, userCollegeId, PageRequest.of(pageNum - 1, pageSize)
        );
        List<Map<String, Object>> userManagementTable = new ArrayList<>();
        for (StudentInfo studentInfo : studentInfoList) {
            Map<String, Object> item = new HashMap<>();
            item.put("infoid", studentInfo.getInfoId());
            item.put("department", studentInfo.getDepartment());
            item.put("college", studentInfo.getCollege());
            item.put("stname", studentInfo.getStname());
            item.put("gender", studentInfo.getGender());
            item.put("age", studentInfo.getAge());
            item.put("stid", studentInfo.getStid());
            item.put("stype", studentInfo.getStype());
            item.put("province", studentInfo.getProvince());
            item.put("year", studentInfo.getYear());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            item.put("create_time", dateFormat.format(studentInfo.getCreateTime()));
            userManagementTable.add(item);
        }
        res.put("userManagementTable", userManagementTable);

        // 返回参数；数据库中所有学校名单
        if (userUniversityId == -1) {
            List<UniversityModel> universityModelList = universityRepository.findAll();
            List<String> departmentList = new ArrayList<>();
            for (UniversityModel universityModel : universityModelList) {
                departmentList.add(universityModel.getUniversityName());
            }
            res.put("departmentList", departmentList);
        } else {
            List<String> departmentList = new ArrayList<>();
            Optional<UniversityModel> optional = universityRepository.findById(userUniversityId);
            if (optional.isPresent()) {
                departmentList.add(optional.get().getUniversityName());
            }
            res.put("departmentList", departmentList);
        }


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
    public JSONObject deleteStudent(JSONObject params) {

        // 获取参数
        Long infoid = params.getLong("infoid");

        JSONObject res = new JSONObject();

        try {
            studentRepository.deleteById(infoid);
        }catch (EmptyResultDataAccessException e){
            res.put("errorCode", 500);
            res.put("errorMsg", "没有这个学生，删除失败");
            return res;
        }

        res.put("errorCode", 200);
        res.put("errorMsg", "删除成功");
        return res;
    }


    @Override
    public JSONObject insertStudent(JSONObject params) {

        // 获取参数
        String department = params.getString("department");
        String collegeName = params.getString("college");
        String stname = params.getString("stname");
        String gender = params.getString("gender");
        Integer age = params.getInteger("age");
        String stid = params.getString("stid");
        Integer stype = params.getInteger("stype");
        String province = params.getString("province");
        String year = params.getString("year");
        String create_time = params.getString("create_time");

        JSONObject res = new JSONObject();

        UniversityModel universityModel = universityRepository.findByUniversityName(department);
        if (universityModel == null){
            res.put("errorCode", 501);
            res.put("errorMsg", "没有这个高校");
            return res;
        }
        CollegeModel collegeModel = collegeRepository.findCollegeByCollegeNameAndUniversityName(collegeName, department);
        if (collegeModel == null){
            res.put("errorCode", 502);
            res.put("errorMsg", "没有这个院系");
            return res;
        }
        StudentInfo studentInfo = new StudentInfo();
        studentInfo.setDepartment(department);
        studentInfo.setCollege(collegeName);
        studentInfo.setStname(stname);
        studentInfo.setGender(gender);
        studentInfo.setAge(age);
        studentInfo.setStid(stid);
        studentInfo.setStype(stype);
        studentInfo.setProvince(province);
        studentInfo.setYear(year);
        studentInfo.setUniversityId(universityModel.getUniversityId());
        studentInfo.setCollegeId(collegeModel.getCollegeId());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            studentInfo.setCreateTime(dateFormat.parse(create_time));
        } catch (ParseException e) {
            res.put("errorCode", 500);
            res.put("errorMsg", "日期格式错误");
            return res;
        }

        studentRepository.save(studentInfo);

        res.put("errorCode", 200);
        res.put("errorMsg", "插入成功");
        return res;
    }

    @Override
    public JSONObject editStudent(JSONObject params) {

        // 获取参数
        Long infoid = params.getLong("infoid");
        String department = params.getString("department");
        String collegeName = params.getString("college");
        String stname = params.getString("stname");
        String gender = params.getString("gender");
        Integer age = params.getInteger("age");
        String stid = params.getString("stid");
        Integer stype = params.getInteger("stype");
        String province = params.getString("province");
        String year = params.getString("year");
        String create_time = params.getString("create_time");

        JSONObject res = new JSONObject();

        UniversityModel universityModel = universityRepository.findByUniversityName(department);
        if (universityModel == null){
            res.put("errorCode", 501);
            res.put("errorMsg", "没有这个高校");
            return res;
        }

        CollegeModel collegeModel = collegeRepository.findCollegeByCollegeNameAndUniversityName(collegeName, department);
        if (collegeModel == null){
            res.put("errorCode", 502);
            res.put("errorMsg", "没有这个院系");
            return res;
        }

        Optional<StudentInfo> optional = studentRepository.findById(infoid);
        if (!optional.isPresent()){
            res.put("errorCode", 503);
            res.put("errorMsg", "没有这个学生");
            return res;
        }
        StudentInfo studentInfo = optional.get();
        studentInfo.setInfoId(infoid);
        studentInfo.setDepartment(department);
        studentInfo.setCollege(collegeName);
        studentInfo.setStname(stname);
        studentInfo.setGender(gender);
        studentInfo.setAge(age);
        studentInfo.setStid(stid);
        studentInfo.setStype(stype);
        studentInfo.setProvince(province);
        studentInfo.setYear(year);
        studentInfo.setUniversityId(universityModel.getUniversityId());
        studentInfo.setCollegeId(collegeModel.getCollegeId());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            studentInfo.setCreateTime(dateFormat.parse(create_time));
        } catch (ParseException e) {
            res.put("errorCode", 500);
            res.put("errorMsg", "日期格式错误");
            return res;
        }

        studentRepository.save(studentInfo);

        res.put("errorCode", 200);
        res.put("errorMsg", "插入成功");
        return res;
    }

}
