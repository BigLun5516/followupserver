package com.epic.followup.controller.school;


import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.school.SchoolStudentModel;
import com.epic.followup.service.school.SchoolFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@CrossOrigin
@RequestMapping("/school/file")
public class SchoolFileController {

    @Autowired
    private SchoolFileService schoolFileService;

    //---------组织机构-------------

    //获取院系基本信息
    @RequestMapping(value = "/organization/getCollegeInfo", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getCollegeInfo (@RequestParam String collegeName){
        return schoolFileService.getCollegeInfo(collegeName);
    }

    //获取院系对应的班级名字列表
    @RequestMapping(value = "/organization/getClassList", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getCollegeClassList (@RequestParam String collegeName){
        return schoolFileService.getCollegeClassList(collegeName);
    }

    //获取班级详细信息
    @RequestMapping(value = "/organization/getClassInfo", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getClassInfo (@RequestBody JSONObject req){
        return schoolFileService.getClassInfo(req);
    }

    //查看班级下的学生列表
    @RequestMapping(value = "/organization/getClassStuList", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getClassStuList (@RequestBody JSONObject req){
        return schoolFileService.getClassStuList(req);
    }

    //查看班级下的管理员
    @RequestMapping(value = "/organization/getClassCounselor", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getClassCounselor (@RequestBody JSONObject req){
        return schoolFileService.getClassCounselor(req);
    }

    //查看所有学生的所有入学年份
    @RequestMapping(value = "/organization/getAllStartDate", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getAllStartDate (){
        return schoolFileService.getAllStartDate();
    }

    //---------学生档案-------------

    //根据条件查询学生列表
    @RequestMapping(value = "/stuFile/getStuByQuery", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getStuByQuery (@RequestBody JSONObject req){
        JSONObject res = new JSONObject();
        Integer pagenum=req.getInteger("pagenum");
        Integer pagesize=req.getInteger("pagesize");
        List<SchoolStudentModel> tmp=schoolFileService.getStuByQuery(req.getString("name"),req.getString("min_birth"),req.getString("max_birth"));
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

    //编辑和新增学生
    @RequestMapping(value = "/stuFile/editStuInfo", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject editStuInfo (@RequestBody JSONObject req){
        return schoolFileService.editStuInfo(req);
    }

    //----------师资档案--------------

    //获取辅导员列表
    @RequestMapping(value = "/tchFile/getConList", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getConList (@RequestBody JSONObject req){
        return schoolFileService.getConList(req);
    }
    //编辑辅导员信息
    @RequestMapping(value = "/tchFile/editConInfo", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject editConInfo (@RequestBody JSONObject req){
        return schoolFileService.editConInfo(req);
    }




}
