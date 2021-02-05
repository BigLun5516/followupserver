package com.epic.followup.controller.app;


import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.app.AppDoctorModel;
import com.epic.followup.model.app.DoctorScheduleModel;
import com.epic.followup.service.app.AppDoctorService;
import com.epic.followup.service.app.DoctorManagerService;
import com.epic.followup.temporary.app.manager.DoctorList;
import com.epic.followup.temporary.app.manager.LoginManagerRequest;
import com.epic.followup.temporary.app.manager.LoginManagerResponse;
import com.epic.followup.temporary.app.manager.createDoctorRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@CrossOrigin    //允许跨域
@RequestMapping("/doctor/manager")
public class DoctorManagerController {

    @Autowired
    private DoctorManagerService doctorManagerService;

    @Autowired
    private AppDoctorService appDoctorService;



    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public LoginManagerResponse loginManager(@RequestBody LoginManagerRequest req){

        return doctorManagerService.loginManager(req);
    }

    @RequestMapping(value = "/listDoctor", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getUserList(@RequestParam Integer pagenum,
                                           @RequestParam Integer pagesize,
                                           @RequestParam String name,
                                           @RequestParam String department,
                                           @RequestParam String title) throws JsonProcessingException {

        Map<String, Object> map = new LinkedHashMap<String, Object>();
        List<AppDoctorModel> tmp = doctorManagerService.getByQuery(department, title, name);
        System.out.println(tmp);
        if (tmp == null) {
            map.put("code", 400);
            map.put("msg", "");
            map.put("page", pagenum);
            map.put("totalpage", 0);
            map.put("data", null);
            return map;
        }
        List<Object> doctorList = new ArrayList<>();
        int i;
        for (i = (pagenum - 1) * pagesize; i < pagenum * pagesize && i < tmp.size(); i++) {
            DoctorList doctorList1 = new DoctorList();
            AppDoctorModel obj = tmp.get(i);

            doctorList1.setDoctorId(obj.getDoctorId());
            doctorList1.setEmployeeNum(obj.getEmployeeNum());
            doctorList1.setName(obj.getName());
            doctorList1.setPassword(obj.getPassword());
            doctorList1.setTel(obj.getTel());
            doctorList1.setDepartment(obj.getDepartment());
            doctorList1.setTitle(obj.getTitle());

            doctorList.add(doctorList1);
        }

        System.out.println(doctorList);

        map.put("code", 200);
        map.put("msg", "");
        map.put("page", pagenum);
        map.put("totalpage", tmp.size());
        map.put("data", doctorList);

        return map;
    }

    @RequestMapping(value = "/addDoctor")
    @ResponseBody
    public Map<String, Object> createDoctor(@RequestBody createDoctorRequest req)throws Exception{

        Map<String,Object> map = new HashMap<>();

        AppDoctorModel byEmployeeNum = appDoctorService.getByEmployeeNum(req.getEmployeeNum());
        if (byEmployeeNum != null){
            map.put("code",400);
            map.put("msg","职工号已经存在");
            return map;
        }
        AppDoctorModel appDoctorModel = new AppDoctorModel();
        appDoctorModel.setEmployeeNum(req.getEmployeeNum());
        appDoctorModel.setName(req.getName());
        appDoctorModel.setPassword(req.getPassword());
        appDoctorModel.setDepartment(req.getDepartment());
        appDoctorModel.setTitle(req.getTitle());

        if (appDoctorService.addDoctor(appDoctorModel)){
            map.put("data",appDoctorModel);
            map.put("code", 200);
            map.put("msg","添加医生成功");
        }
        else {
            map.put("code",400);
            map.put("msg","添加医生失败");
        }
        return map;
    }

    @RequestMapping(value = "/doctorInfo/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> findById(@PathVariable("id") Long id){
        Map<String,Object> map = new HashMap<>();

        AppDoctorModel byId = appDoctorService.getById(id);
        if(byId == null) {
            map.put("code",400);
            map.put("msg","获取医生信息失败");
        }
        else {
            map.put("data",byId);
            map.put("code", 200);
            map.put("msg","获取医生信息成功");
        }

        return map;
    }

    @RequestMapping(value = "/editDoctor")
    @ResponseBody
    public Map<String, Object> editUser(@RequestBody DoctorList doctorList) {

        Map<String,Object> map = new HashMap<>();
        AppDoctorModel byId = appDoctorService.getById(doctorList.getDoctorId());
        byId.setEmployeeNum(doctorList.getEmployeeNum());
        byId.setName(doctorList.getName());
        byId.setPassword(doctorList.getPassword());
        byId.setDepartment(doctorList.getDepartment());
        byId.setTitle(doctorList.getTitle());
        if (appDoctorService.addDoctor(byId)){
            map.put("code", 200);
            map.put("msg","修改医生信息成功");
        }
        else {
            map.put("code",400);
            map.put("msg","修改医生信息失败");
        }
        return map;
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public Map<String, Object> DeleteById(@PathVariable("id") Long id) {
        Map<String, Object> map = new HashMap<>();
        appDoctorService.deleteById(id);
        AppDoctorModel byId = appDoctorService.getById(id);
        if (byId == null) {
            map.put("data", null);
            map.put("code", 200);
            map.put("msg", "成功删除用户");
        } else {
            map.put("code", 400);
            map.put("msg", "删除用户失败");
        }
        return map;
    }

    @RequestMapping(value = "/listSchedule", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getWeekSchedule(@RequestBody JSONObject weekRequest){
        return doctorManagerService.getWeekSchedule(weekRequest);
    }

    @RequestMapping(value = "/doctorNames", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getDoctorNames(){
        return doctorManagerService.getDoctorNames();
    }

    @RequestMapping(value = "/scheduleInfo/{id}", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getScheduleInfo(@PathVariable("id") Long id){
        JSONObject res=new JSONObject();
        DoctorScheduleModel ds = doctorManagerService.getScheduleById(id);
        if(ds == null) {
            res.put("errorCode",502);
            res.put("errorMsg","获取排班信息失败");
        }
        else {
            res.put("data",ds);
            res.put("errorCode", 200);
            res.put("errorMsg","获取排班信息成功");
        }
        return res;
    }

    @RequestMapping(value = "/editSchedule", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject editSchedule(@RequestBody JSONObject editScheduleRequest){
        JSONObject res=new JSONObject();
        DoctorScheduleModel ds = doctorManagerService.getScheduleById(editScheduleRequest.getLong("id"));
        ds.setMorning(editScheduleRequest.getString("morning"));
        ds.setAfternoon(editScheduleRequest.getString("afternoon"));
        ds.setEvening(editScheduleRequest.getString("evening"));
        doctorManagerService.saveSchedule(ds);
        res.put("errorCode", 200);
        res.put("errorMsg","更新排班信息成功");
        return res;
    }
}
