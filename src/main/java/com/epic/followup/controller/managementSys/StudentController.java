package com.epic.followup.controller.managementSys;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.service.managementSys.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
@RequestMapping("/managementSystem/student")
public class StudentController {

    @Autowired
    StudentService studentService;

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
    public JSONObject importStudent(@RequestBody JSONObject params){

        return studentService.importStudent(params);
    }
}
