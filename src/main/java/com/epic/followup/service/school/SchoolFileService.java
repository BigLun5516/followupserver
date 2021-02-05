package com.epic.followup.service.school;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.school.SchoolStudentModel;

import java.util.List;

public interface SchoolFileService {

    //获取院系基本信息(院系名字)
    JSONObject getCollegeInfo(String name);

    //获取院系对应的班级名字列表(院系名字)
    JSONObject getCollegeClassList(String name);

    //获取班级详细信息(院系名字，班级名字）
    JSONObject getClassInfo(JSONObject req);

    //查看班级下的学生列表（院系名字，班级名字，当前页面，页面大小）
    JSONObject getClassStuList(JSONObject req);

    //查看班级下的管理员（院系名字，班级名字）
    JSONObject getClassCounselor(JSONObject req);

    //查看所有学生的入学年份
    JSONObject getAllStartDate();

    //根据条件获取学生列表
    List<SchoolStudentModel> getStuByQuery(String name, String min_date, String max_date);

    //编辑和添加学生
    JSONObject editStuInfo(JSONObject req);

    //获取辅导员列表
    JSONObject getConList(JSONObject req);

    //编辑辅导员信息
    JSONObject editConInfo(JSONObject req);
}
