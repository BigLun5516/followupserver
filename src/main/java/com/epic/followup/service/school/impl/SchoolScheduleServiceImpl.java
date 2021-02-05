package com.epic.followup.service.school.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.school.SchoolPsychologistModel;
import com.epic.followup.model.school.SchoolScheduleModel;
import com.epic.followup.repository.school.SchoolPsychologistRepository;
import com.epic.followup.repository.school.SchoolScheduleRepository;
import com.epic.followup.service.school.SchoolScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SchoolScheduleServiceImpl implements SchoolScheduleService {

    
    @Autowired
    private SchoolScheduleRepository schoolScheduleRepository;

    @Autowired
    private SchoolPsychologistRepository schoolPsychologistRepository;

    
    //获取一周的排班表
    @Override
    public JSONObject getWeekSchedule(JSONObject weekTime){
        String week=weekTime.getString("weekTime");
        String[] weeklist=week.split("-");
//        for (String s : weeklist) {
//                System.out.println(s);
//        }
        JSONObject res = new JSONObject();
        List<Object> data=new ArrayList<>();
        for (int j = 0; j < 7; j++) {
            Optional<SchoolScheduleModel> ou = schoolScheduleRepository.findBytime(weeklist[j]);
            if (!ou.isPresent()) {//数据库中没有这一天的记录就新建一行记录
                SchoolScheduleModel ds=new SchoolScheduleModel();
                ds.setTime(weeklist[j]);
                schoolScheduleRepository.save(ds);
                ou = schoolScheduleRepository.findBytime(weeklist[j]);
            }
            SchoolScheduleModel u = ou.get();
            data.add(u);
        }
        res.put("errorCode",200);
        res.put("errorMsg","查询成功");
        res.put("data",data);
        return res;
    }

    @Override
    public JSONObject getPsychologistNames(){
        JSONObject res = new JSONObject();
        List<String> names=schoolScheduleRepository.findByQuery1();
        if(names.size()!=0){
            res.put("errorCode",200);
            res.put("errorMsg","查询成功");
            res.put("data",names);
        }
        else{
            res.put("errorCode",502);
            res.put("errorMsg","查询失败");
        }
        return res;
    }

    @Override
    public SchoolScheduleModel getScheduleById(Long id){
        return schoolScheduleRepository.findById(id).get();
    }

    @Override
    public void saveSchedule(SchoolScheduleModel ds){
        schoolScheduleRepository.save(ds);
    }

    //心理咨询师获取一周时间的排班表
    @Override
    public JSONObject getMyWeekSchedule(JSONObject weekTime){
        JSONObject res = getWeekSchedule(weekTime);
        String s = JSONArray.toJSONString(res.get("data"));
        List<SchoolScheduleModel> data = JSONArray.parseArray(s, SchoolScheduleModel.class);
        Long pid=weekTime.getLong("contactId");//心理咨询师Id
        SchoolPsychologistModel u=schoolPsychologistRepository.findById(pid).get();
        String psyName=u.getName();//心理咨询师姓名
        for(SchoolScheduleModel tmp:data){
            if(tmp.getMorning().contains(psyName))
                tmp.setMorning(psyName);
            else
                tmp.setMorning(null);
            if(tmp.getAfternoon().contains(psyName))
                tmp.setAfternoon(psyName);
            else
                tmp.setAfternoon(null);
            if(tmp.getEvening().contains(psyName))
                tmp.setEvening(psyName);
            else
                tmp.setEvening(null);
        }
        res.remove("data");
        res.put("data",data);
        return res;
    }
}
