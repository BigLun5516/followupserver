package com.epic.followup.controller.managementSys;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.followup2.student.StudentInfo;
import com.epic.followup.model.managementSys.StudentExcelData;
import com.epic.followup.repository.followup2.student.StudentInfoRepository;
import com.epic.followup.repository.managementSys.CollegeRepository;
import com.epic.followup.repository.managementSys.UniversityRepository;
import com.epic.followup.service.managementSys.StudentDataListener;
import com.epic.followup.service.managementSys.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@CrossOrigin
@RequestMapping("/managementSystem/student")
public class StudentController {

    @Autowired
    StudentService studentService;

    @Autowired
    StudentInfoRepository studentInfoRepository;

    @Autowired
    UniversityRepository universityRepository;

    @Autowired
    CollegeRepository collegeRepository;

    // 根据条件查询
    @PostMapping("/find")
    @ResponseBody
    public JSONObject findStudent(@RequestBody JSONObject params){

        return studentService.findStudent(params);
    }

    // 删除
    @PostMapping("/delete")
    @ResponseBody
    public JSONObject deleteStudent(@RequestBody JSONObject params){

        return studentService.deleteStudent(params);
    }

    // 新增
    @PostMapping("/insert")
    @ResponseBody
    public JSONObject insertStudent(@RequestBody JSONObject params){

        return studentService.insertStudent(params);
    }

    // 编辑
    @PostMapping("/edit")
    @ResponseBody
    public JSONObject editStudent(@RequestBody JSONObject params){

        return studentService.editStudent(params);
    }

    // 导入
    @PostMapping("/import")
    @ResponseBody
    public JSONObject importStudent(MultipartFile file) throws IOException {

        EasyExcel.read(file.getInputStream(), StudentExcelData.class, new StudentDataListener(
                studentInfoRepository, universityRepository, collegeRepository
        )).sheet().doRead();

        JSONObject res = new JSONObject();
        res.put("errorCode", 200);
        res.put("errorMsg", "导入成功");
        return res;
    }
}
