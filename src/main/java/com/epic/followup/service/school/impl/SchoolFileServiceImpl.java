package com.epic.followup.service.school.impl;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.school.SchoolClassModel;
import com.epic.followup.model.school.SchoolCollegeModel;
import com.epic.followup.model.school.SchoolCounselorModel;
import com.epic.followup.model.school.SchoolStudentModel;
import com.epic.followup.repository.school.SchoolClassRepository;
import com.epic.followup.repository.school.SchoolCollegeRepository;
import com.epic.followup.repository.school.SchoolCounselorRepository;
import com.epic.followup.repository.school.SchoolStudentRepository;
import com.epic.followup.service.school.SchoolFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SchoolFileServiceImpl implements SchoolFileService {

    @Autowired
    SchoolClassRepository schoolClassRepository;

    @Autowired
    SchoolCollegeRepository schoolCollegeRepository;

    @Autowired
    SchoolCounselorRepository schoolCounselorRepository;

    @Autowired
    SchoolStudentRepository schoolStudentRepository;

    //获取学院详细信息
    @Override
    public JSONObject getCollegeInfo(String name){
        JSONObject res = new JSONObject();
        SchoolCollegeModel ou=schoolCollegeRepository.findByName(name);
        if(ou==null){
            res.put("errorCode",402);
            res.put("errorMsg","查询失败");
            return res;
        }
        res.put("errorCode",200);
        res.put("errorMsg","查询成功");
        res.put("data",ou);
        return res;
    }

    //获取院系对应的班级列表（只有名字）
    @Override
    public JSONObject getCollegeClassList(String name){
        JSONObject res = new JSONObject();
        List<String> ou=schoolCollegeRepository.findByCollegeName(name);
        res.put("errorCode",200);
        res.put("errorMsg","查询成功");
        res.put("data",ou);
        return res;
    }

    @Override
    //获取班级详细信息(院系名字，班级名字）
    public JSONObject getClassInfo(JSONObject req){
        JSONObject res = new JSONObject();
        SchoolClassModel ou=schoolClassRepository.findByCollegeAndName(req.getString("collegeName"),req.getString("className"));
        if(ou==null){
            res.put("errorCode",402);
            res.put("errorMsg","查询失败");
            return res;
        }
        res.put("errorCode",200);
        res.put("errorMsg","查询成功");
        res.put("data",ou);
        return res;
    }

    @Override
    //查看班级下的学生列表，含分页信息（院系名字，班级名字，当前页面，页面大小）
    public JSONObject getClassStuList(JSONObject req){
        JSONObject res = new JSONObject();
        Integer pagenum=req.getInteger("pagenum");
        Integer pagesize=req.getInteger("pagesize");
        List<SchoolStudentModel> tmp=schoolStudentRepository.findByCollegeAndClass(req.getString("collegeName"),req.getString("className"));
        System.out.println(tmp);
        if (tmp == null) {
            res.put("errorCode", 400);
            res.put("errorMsg", "没有数据");
            res.put("page", pagenum);
            res.put("totalnum", 0);
            res.put("data", null);
            return res;
        }
        List<Object> data = new ArrayList<>();
        int i;
        for (i = (pagenum - 1) * pagesize; i < pagenum * pagesize && i < tmp.size(); i++) {
            data.add(tmp.get(i));
        }
        res.put("errorCode",200);
        res.put("errorMsg","查询成功");
        res.put("page", pagenum);
        res.put("totalnum", tmp.size());
        res.put("data",data);
        return res;
    }

    @Override
    //查看班级下的管理员（院系名字，班级名字）
    public JSONObject getClassCounselor(JSONObject req){
        JSONObject res = new JSONObject();
        SchoolCounselorModel ou=schoolCounselorRepository.findByCollegeAndClass(req.getString("collegeName"),"%"+req.getString("className")+"%");
        if(ou==null){
            res.put("errorCode",402);
            res.put("errorMsg","没有查到辅导员");
            return res;
        }
        res.put("errorCode",200);
        res.put("errorMsg","查询成功");
        res.put("data",ou);
        return res;
    }

    @Override
    //查看所有学生的所有入学年份
    public JSONObject getAllStartDate(){
        JSONObject res = new JSONObject();
        List<SchoolStudentModel> tmp=schoolStudentRepository.findAllStuByOrder();
        Set<String> data = new HashSet<String>();
        for(SchoolStudentModel s:tmp){
            data.add(s.getSdate().substring(0,4));
        }
        List<String> result = new ArrayList<>(data);
        Collections.sort(result);
        res.put("errorCode", 200);
        res.put("errorMsg", "查询成功");
        res.put("allYear", result);
        return res;
    }

    @Override
    //根据条件获取学生列表
    public List<SchoolStudentModel> getStuByQuery(String name,String min_date,String max_date){
        if(!name.equals("")){
            if(!max_date.equals("")){
                return schoolStudentRepository.findByQuery1(name,min_date,max_date);
            }
            else{
                return schoolStudentRepository.findByQuery1(name,min_date,"99999999");
            }
        }
        else{
            if(!max_date.equals("")){
                return schoolStudentRepository.findByQuery2(min_date,max_date);
            }
            else{
                return schoolStudentRepository.findByQuery2(min_date,"99999999");
            }
        }
    }

    @Override
    //编辑和添加学生
    public JSONObject editStuInfo(JSONObject req){
        JSONObject res = new JSONObject();
        SchoolStudentModel s=null;
        if(req.containsKey("id")){//编辑
            Optional<SchoolStudentModel> ou=schoolStudentRepository.findById(req.getLong("id"));
            s=ou.get();
        }else{//新增
            s=new SchoolStudentModel();
        }
        s.setBclass(req.getString("bclass"));
        s.setBirth(req.getString("birth"));
        s.setCollege(req.getString("college"));
        s.setName(req.getString("name"));
        s.setPhone(req.getString("phone"));
        s.setSdate(req.getString("sdate"));
        s.setSex(req.getString("sex"));
        s.setStu_number(req.getString("stu_number"));
        schoolStudentRepository.save(s);
        res.put("errorCode", 200);
        res.put("errorMsg", "操作成功");
        return res;
    }


    @Override
    //获取辅导员列表
    public JSONObject getConList(JSONObject req){
        JSONObject res = new JSONObject();
        Integer pagenum=req.getInteger("pagenum");
        Integer pagesize=req.getInteger("pagesize");
        List<SchoolCounselorModel> tmp=schoolCounselorRepository.findAll();
        System.out.println(tmp);
        if (tmp == null) {
            res.put("errorCode", 400);
            res.put("errorMsg", "没有数据");
            res.put("page", pagenum);
            res.put("totalnum", 0);
            res.put("data", null);
            return res;
        }
        List<Object> data = new ArrayList<>();
        int i;
        for (i = (pagenum - 1) * pagesize; i < pagenum * pagesize && i < tmp.size(); i++) {
            data.add(tmp.get(i));
        }
        res.put("errorCode",200);
        res.put("errorMsg","查询成功");
        res.put("page", pagenum);
        res.put("totalnum", tmp.size());
        res.put("data",data);
        return res;
    }



    @Override
    //编辑辅导员信息
    public JSONObject editConInfo(JSONObject req){
        JSONObject res = new JSONObject();
        Optional<SchoolCounselorModel> ou=schoolCounselorRepository.findById(req.getLong("id"));
        SchoolCounselorModel s=ou.get();
        s.setBclass(req.getString("bclass"));
        s.setCollege(req.getString("college"));
        s.setName(req.getString("name"));
        s.setPhone(req.getString("phone"));
        s.setSex(req.getString("sex"));
        s.setTch_number(req.getString("tch_number"));
        schoolCounselorRepository.save(s);
        res.put("errorCode", 200);
        res.put("errorMsg", "操作成功");
        return res;
    }
}
