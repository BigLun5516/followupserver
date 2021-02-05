package com.epic.followup.service.school.impl;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.school.SchoolEmergencyModel;
import com.epic.followup.repository.school.SchoolCounselorRepository;
import com.epic.followup.repository.school.SchoolEmergencyRepository;
import com.epic.followup.repository.school.SchoolStudentRepository;
import com.epic.followup.service.school.SchoolCounselorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SchoolCounselorServiceImpl implements SchoolCounselorService {

    @Autowired
    SchoolCounselorRepository schoolCounselorRepository;

    @Autowired
    SchoolStudentRepository schoolStudentRepository;

    @Autowired
    SchoolEmergencyRepository schoolEmergencyRepository;


    @Override
    //获取辅导员对应的学院和班级列表
    public JSONObject getCollegeAndClasses(Long id){
        JSONObject res=new JSONObject();
        List l= schoolCounselorRepository.findCollegeAndClassBycId(id);
        Object[] cells;
        if(l.size()==0){
            res.put("errorCode",502);
            res.put("errorMsg","查询失败");
        }else{
            Object row = l.get(0);
            cells = (Object[]) row;
            res.put("errorCode",200);
            res.put("errorMsg","查询成功");
            res.put("college",cells[0]);
            res.put("classes",cells[1]);
        }
        return res;
    }

    @Override
    //获取班级下的学生列表（带有id)
    public JSONObject getStuList(JSONObject req){
        JSONObject res=new JSONObject();
        List<JSONObject> ml = new ArrayList<>();
        List l= schoolStudentRepository.findStuByClassAndCollege(req.getString("college"),req.getString("bclass"));
        if(l.size()==0){
            res.put("errorCode",502);
            res.put("errorMsg","查询失败");
            return res;
        }
        for (Object row : l){
            Object[] cells = (Object[]) row;
            JSONObject m =new JSONObject();
            m.put("id", cells[0]);
            m.put("name", cells[1]);
            ml.add(m);
        }
        res.put("errorCode",200);
        res.put("errorMsg","查询成功");
        res.put("data",ml);
        return res;
    }

    @Override
    //新增一个紧急事件（辅导员id，学生id，紧急事件）
    public JSONObject addEmergency(JSONObject req){
        JSONObject res=new JSONObject();
        SchoolEmergencyModel s=new SchoolEmergencyModel();
        s.setcId(req.getLong("cId"));
        s.setsId(req.getLong("sId"));
        s.setEvent(req.getString("event"));
        schoolEmergencyRepository.save(s);
        res.put("errorCode",200);
        res.put("errorMsg","新增成功");
        return res;
    }

    @Override
    //查询辅导员对应的所有紧急事件
    public JSONObject getAllEmergency(Long id){
        JSONObject res=new JSONObject();
        List<JSONObject> ml = new ArrayList<>();
        List l= schoolCounselorRepository.findAllEmergency(id);
        if(l.size()==0){
            res.put("errorCode",502);
            res.put("errorMsg","查询失败");
            return res;
        }
        for (Object row : l){
            Object[] cells = (Object[]) row;
            JSONObject m =new JSONObject();
            m.put("id", cells[0]);
            m.put("name", cells[2]);
            m.put("college", cells[3]);
            m.put("bclass", cells[4]);
            m.put("stu_number", cells[5]);
            m.put("event", cells[6]);
            ml.add(m);
        }
        res.put("errorCode",200);
        res.put("errorMsg","查询成功");
        res.put("data",ml);
        return res;
    }
}
