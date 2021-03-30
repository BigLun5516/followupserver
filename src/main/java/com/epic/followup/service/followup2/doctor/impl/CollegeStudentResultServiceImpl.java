package com.epic.followup.service.followup2.doctor.impl;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.followup2.doctor.StudentResultModel;
import com.epic.followup.model.followup2.student.StudentInfo;
import com.epic.followup.repository.followup2.doctor.StudentResultRepository;
import com.epic.followup.service.followup2.doctor.CollegeStudentResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author : zx
 * @version V1.0
 */

@Service
public class CollegeStudentResultServiceImpl implements CollegeStudentResultService {
    private StudentResultRepository studentResultRepository;

    @Autowired
    public CollegeStudentResultServiceImpl(StudentResultRepository studentResultRepository){
        this.studentResultRepository = studentResultRepository;
    }

    @Override
    public List<Map> findProvinceCountsByDepartment(String department, String college) {
        List<Map> ml = new ArrayList<>();
        List l = studentResultRepository.findProvinceCountsByDepartmentAAndCollege(department, college);
        for (Object row : l){
            Object[] cells = (Object[]) row;
            Map m = new HashMap();
            m.put("province", cells[0]);
            m.put("level", cells[1]);
            m.put("num", cells[2]);
            ml.add(m);
        }
        return ml;
    }

    @Override
    public List<Map> findStypeCountsByDepartment(String department, String college) {
        List<Map> ml = new ArrayList<>();
        List l = studentResultRepository.findStypeCountsByDepartmentAAndCollege(department, college);
        for (Object row : l){
            Object[] cells = (Object[]) row;
            Map m = new HashMap();
            m.put("stype", cells[0]);
            m.put("num", cells[2]);
            m.put("level", cells[1]);
            ml.add(m);
        }
        return ml;
    }

    @Override
    public List<Map> findAgeCountsByDepartment(String department, String college) {
        List<Map> ml = new ArrayList<>();
        List l = studentResultRepository.findAgeCountsByDepartmentAndCollege(department, college);
        for (Object row : l){
            Object[] cells = (Object[]) row;
            Map m = new HashMap();
            m.put("age", cells[0]);
            m.put("level", cells[1]);
            m.put("num", cells[2]);
            ml.add(m);
        }
        return ml;
    }

    @Override
    public List<Map> findAllCountsByDepartment(String department, String college) {
        List<Map> ml = new ArrayList<>();
        List l = studentResultRepository.findAllCountsByDepartmentAAndCollege(department, college);
        for (Object row : l){
            Object[] cells = (Object[]) row;
            Map m = new HashMap();
            m.put("level", cells[0]);
            m.put("num", cells[1]);
            ml.add(m);
        }
        return ml;
    }

    @Override
    public List<StudentResultModel> findListByDepartmentAndCollege(String department, String college) {
        return studentResultRepository.findListByDepartmentAndCollege(department, college);
    }

    @Override
    public List<StudentResultModel> findList(JSONObject params) {
        String universityName=params.getString("universityName");
        String illness=params.getString("illness");
        Integer userUniversityId = params.getInteger("userUniversityId");
        Integer userCollegeId = params.getInteger("userCollegeId");
        return studentResultRepository.findbyUniversityAndIllness(universityName,illness, userUniversityId, userCollegeId);
    }
}
