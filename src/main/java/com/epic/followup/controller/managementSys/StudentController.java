package com.epic.followup.controller.managementSys;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.followup2.doctor.StudentResultModel;
import com.epic.followup.model.managementSys.StudentExcelData;
import com.epic.followup.repository.followup2.student.StudentInfoRepository;
import com.epic.followup.repository.managementSys.CollegeRepository;
import com.epic.followup.repository.managementSys.UniversityRepository;
import com.epic.followup.service.followup2.doctor.CollegeStudentResultService;
import com.epic.followup.service.managementSys.StudentDataListener;
import com.epic.followup.service.managementSys.StudentService;
import com.epic.followup.temporary.followup2.session.BaseUserSession;
import com.epic.followup.temporary.wechat.patient.diary.getAllMoodsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

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

    @Autowired
    private CollegeStudentResultService collegeStudentResultService;

    // 根据条件查询
    @PostMapping("/find")
    @ResponseBody
    public JSONObject findStudent(@RequestBody JSONObject params, HttpSession session){

        params.put("userUniversityId", session.getAttribute("universityId"));
        params.put("userCollegeId", session.getAttribute("collegeId"));

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

    //查询studentresult表学生信息
    @RequestMapping(value = "/stuInfo", method = RequestMethod.POST)
    @ResponseBody
    public List<StudentResultModel> findListByDepartmentAndCollege(@RequestBody JSONObject params, HttpSession session) {

        params.put("userUniversityId", session.getAttribute("universityId"));
//        params.put("userCollegeId", session.getAttribute("collegeId"));
        List<Integer> list=(List<Integer>)session.getAttribute("collegeId");
        if(list.size()==1&&list.get(0)==-1){
            params.put("userCollegeId", list.get(0));
        }

        return this.collegeStudentResultService.findList(params);
    }

    // 查询学生的心情
    @RequestMapping(value = "/stuInfo/getMood", method = RequestMethod.POST)
    @ResponseBody
    public getAllMoodsResponse getAllMoods(@RequestBody JSONObject params){
        return studentService.getAllMoods(params);
    }

    // 获取学生7天的身体状况
    @PostMapping(value = "/stuInfo/getSevenDaysInfo")
    @ResponseBody
    public JSONObject getBodyInfo(@RequestBody JSONObject params){
        return studentService.getBodyInfo(params);

    }

}
