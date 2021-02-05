package com.epic.followup.service.followup2.doctor.impl;

import com.epic.followup.model.followup2.doctor.StudentResultModel;
import com.epic.followup.model.followup2.student.StudentInfo;
import com.epic.followup.repository.followup2.doctor.StudentResultRepository;
import com.epic.followup.service.followup2.doctor.StudentResultService;
import com.mysql.cj.result.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author : zx
 * @version V1.0
 */

@Service
public class StudentResultServiceImpl implements StudentResultService {

    private StudentResultRepository studentResultRepository;

    @Autowired
    public StudentResultServiceImpl(StudentResultRepository studentResultRepository){
        this.studentResultRepository = studentResultRepository;
    }

    @Override
    public void addResult(Long userId, StudentInfo stInfo, String symptoms, int score, String level) {
        Optional<StudentResultModel> ost = this.studentResultRepository.findByUserId(userId);
        StudentResultModel s;
        if (ost.isPresent()){
            // 有则更新
            s = ost.get();
            s.setAge(stInfo.getAge());
            s.setCollege(stInfo.getCollege());
            s.setDepartment(stInfo.getDepartment());
            s.setGender(stInfo.getGender());
            s.setProvince(stInfo.getProvince());
            s.setScore(score);
            s.setStid(stInfo.getStid());
            s.setStype(stInfo.getStype());
            s.setImgPath(stInfo.getImgPath());
            s.setYear(stInfo.getYear());
            s.setStname(stInfo.getStname());
            s.setLevel(level);
            s.setUpdateTime(new Date());
        }else {
            // 无则加冕
            s = new StudentResultModel();
            s.setAge(stInfo.getAge());
            s.setCollege(stInfo.getCollege());
            s.setDepartment(stInfo.getDepartment());
            s.setGender(stInfo.getGender());
            s.setProvince(stInfo.getProvince());
            s.setScore(score);
            s.setStid(stInfo.getStid());
            s.setStype(stInfo.getStype());
            s.setImgPath(stInfo.getImgPath());
            s.setYear(stInfo.getYear());
            s.setStname(stInfo.getStname());
            s.setLevel(level);
            s.setUpdateTime(new Date());
        }
        s.setUserId(userId);
        s.setSymptoms(symptoms);
        studentResultRepository.save(s);
    }

    @Override
    public List<Map> findProvinceCountsByDepartment(String department) {
        List<Map> ml = new ArrayList<>();
        List l = studentResultRepository.findProvinceCountsByDepartment(department);
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
    public List<Map> findStypeCountsByDepartment(String department) {
        List<Map> ml = new ArrayList<>();
        List l = studentResultRepository.findStypeCountsByDepartment(department);
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
    public List<Map> findAgeCountsByDepartment(String department) {
        List<Map> ml = new ArrayList<>();
        List l = studentResultRepository.findAgeCountsByDepartment(department);
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
    public List<Map> findGenderCountsByDepartment(String department) {
        List<Map> ml = new ArrayList<>();
        List l = studentResultRepository.findGenderCountsByDepartment(department);
        for (Object row : l){
            Object[] cells = (Object[]) row;
            Map m = new HashMap();
            m.put("gender", cells[0]);
            m.put("level", cells[1]);
            m.put("num", cells[2]);
            ml.add(m);
        }
        return ml;
    }

    @Override
    public List<Map> findAllCountsByDepartment(String department) {
        List<Map> ml = new ArrayList<>();
        List l = studentResultRepository.findAllCountsByDepartment(department);
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
    public List<StudentResultModel> findLast(String department) {
        return studentResultRepository.findLast(department);
    }

    @Override
    public StudentResultModel findByUserId(Long userId) {
        return studentResultRepository.findByUserId(userId).get();
    }

    @Override
    public StudentResultModel findByStid(String department, String stid) {
        Optional<StudentResultModel> ou = studentResultRepository.findOneByDepartmentAndStid(department, stid);
        if (ou.isPresent()){
            return ou.get();
        }else {
            return null;
        }
    }
}
