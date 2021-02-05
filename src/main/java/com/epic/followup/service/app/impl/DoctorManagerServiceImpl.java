package com.epic.followup.service.app.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epic.followup.conf.FollowupStaticConfig;
import com.epic.followup.model.app.AppDoctorModel;
import com.epic.followup.model.app.DoctorManagerModel;
import com.epic.followup.model.app.DoctorScheduleModel;
import com.epic.followup.repository.app.AppDoctorRepository;
import com.epic.followup.repository.app.DoctorManagerRepository;
import com.epic.followup.repository.app.DoctorScheduleRepository;
import com.epic.followup.service.app.DoctorManagerService;
import com.epic.followup.temporary.DealMessageResponse;
import com.epic.followup.temporary.app.manager.BaseManagerSession;
import com.epic.followup.temporary.app.manager.LoginManagerRequest;
import com.epic.followup.temporary.app.manager.LoginManagerResponse;
import com.epic.followup.temporary.app.manager.ScheduleList;
import com.google.gson.JsonObject;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class DoctorManagerServiceImpl implements DoctorManagerService {

    @Autowired
    private DoctorManagerRepository doctorManagerRepository;

    @Autowired
    private AppDoctorRepository appDoctorRepository;

    @Autowired
    private DoctorScheduleRepository doctorScheduleRepository;

    private ExpiringMap<String, BaseManagerSession> managerMap; // 医生信息

    public DoctorManagerServiceImpl(){
        this.managerMap = ExpiringMap.builder()
                .maxSize(FollowupStaticConfig.MAX_USERNUM)
                .expiration(1, TimeUnit.DAYS)
                .expirationPolicy(ExpirationPolicy.ACCESSED)
                .variableExpiration()
                .build();
    }

    @Override
    public LoginManagerResponse loginManager(LoginManagerRequest req) {
        LoginManagerResponse res = new LoginManagerResponse();

        DoctorManagerModel byUsername = doctorManagerRepository.findByUsername(req.getUsername());

        if(byUsername == null){
            res.setErrorCode(400);
            res.setErrorMsg("用户名错误");
            return res;
        }else {
            DoctorManagerModel byUsernameAndAndPassword = doctorManagerRepository.findByUsernameAndAndPassword(req.getUsername(), req.getPassword());
            if (byUsernameAndAndPassword == null){
                res.setErrorCode(401);
                res.setErrorMsg("密码错误");
                return res;
            }
            BaseManagerSession s = new BaseManagerSession();
            s.setManagerId(byUsernameAndAndPassword.getManagerId());
            s.setUsername(byUsernameAndAndPassword.getUsername());
            String md =  DigestUtils.md5DigestAsHex((s.getManagerId()+s.getUsername()).getBytes());
            managerMap.put(md, s);

            // 成功返回
            res.setErrorMsg("登录成功.");
            res.setErrorCode(200);
            res.setSessionId(md);
            return res;
        }
    }

    @Override
    public List<AppDoctorModel> getByQuery(String department, String title, String name) {
        System.out.println("department:" + department);
        System.out.println("title:" + title);
        System.out.println("name:" + name);

        if(department != "" || title != "" || name != ""){
            if(department != "" && title !="" && name!= ""){
                return appDoctorRepository.findByQuery5(department, title, name);
            }
            if(department != "" && title != ""){
                return appDoctorRepository.findByQuery2(department, title);
            }
            if(department != "" && name!= ""){
                return appDoctorRepository.findByQuery3(department, name);
            }
            if(title !="" && name!= ""){
                return appDoctorRepository.findByQuery4(title, name);
            }
            if(department != "" || title!=""){
                return appDoctorRepository.findByQuery1(department, title, name);
            }
            if(name != ""){
                return appDoctorRepository.findByQuery6(department, title, name);
            }

        }
        return appDoctorRepository.findAll();
    }

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
            Optional<DoctorScheduleModel> ou = doctorScheduleRepository.findBytime(weeklist[j]);
            if (!ou.isPresent()) {//数据库中没有这一天的记录就新建一行记录
                DoctorScheduleModel ds=new DoctorScheduleModel();
                ds.setTime(weeklist[j]);
                doctorScheduleRepository.save(ds);
                ou = doctorScheduleRepository.findBytime(weeklist[j]);
            }
            DoctorScheduleModel u = ou.get();
            data.add(u);
        }
        res.put("errorCode",200);
        res.put("errorMsg","查询成功");
        res.put("data",data);
        return res;
    }

    @Override
    public JSONObject getDoctorNames(){
        JSONObject res = new JSONObject();
        List<String> names=doctorManagerRepository.findByQuery1();
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
    public DoctorScheduleModel getScheduleById(Long id){
        return doctorScheduleRepository.findById(id).get();
    }

    @Override
    public void saveSchedule(DoctorScheduleModel ds){
        doctorScheduleRepository.save(ds);
    }


}
