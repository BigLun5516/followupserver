package com.epic.followup.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.SecondUserModel;
import com.epic.followup.model.followup2.doctor.StudentResultModel;
import com.epic.followup.service.SecondUserService;
import com.epic.followup.service.followup2.BaseUserService;
import com.epic.followup.service.followup2.doctor.CollegeStudentResultService;
import com.epic.followup.service.followup2.doctor.StudentInfoSubmitService;
import com.epic.followup.service.followup2.doctor.StudentResultService;
import com.epic.followup.temporary.DealMessageResponse;
import com.epic.followup.temporary.followup2.session.BaseUserSession;
import com.epic.followup.temporary.web.LoginRequest;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author : zx
 * @version V1.0
 */

@Controller
@RequestMapping("/web")
public class WebController {

    private SecondUserService secondUserService;
    private StudentResultService studentResultService;
    private CollegeStudentResultService collegeStudentResultService;
    private BaseUserService baseUserService;
    private org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());
    private StudentInfoSubmitService studentInfoSubmitService;

    @Autowired
    public WebController(SecondUserService secondUserService, StudentResultService studentResultService,
            CollegeStudentResultService collegeStudentResultService, BaseUserService baseUserService,
            StudentInfoSubmitService studentInfoSubmitService) {
        this.secondUserService = secondUserService;
        this.studentResultService = studentResultService;
        this.collegeStudentResultService = collegeStudentResultService;
        this.baseUserService = baseUserService;
        this.studentInfoSubmitService = studentInfoSubmitService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public String login(@RequestBody LoginRequest user, BindingResult result, HttpSession session) {

        // MD5加密
        String md5Password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Password);
        if (result.hasErrors()) {
            return "false";
        }
        // 检查用户身份
        SecondUserModel u = secondUserService.checkUser(user);
        if (u != null) {
            session.setAttribute("userName", u.getUserName());
            session.setAttribute("type", u.getType());
            session.setAttribute("department", u.getDepartment());
            session.setAttribute("college", u.getCollege());
            // 管理主页
            JSONObject res = new JSONObject();
            res.put("userName", u.getUserName());
            res.put("type", u.getType());
            res.put("department", u.getDepartment());
            res.put("college", u.getCollege());
            return res.toJSONString();
        } else {
            return "false";
        }
    }

    @RequestMapping(value = "/admin/findProvinceCountsByDepartment", method = RequestMethod.POST)
    @ResponseBody
    public List<Map> findProvinceCountsByDepartment(HttpSession session) {

        return this.studentResultService.findProvinceCountsByDepartment((String) session.getAttribute("department"));
    }

    @RequestMapping(value = "/admin/findStypeCountsByDepartment", method = RequestMethod.POST)
    @ResponseBody
    public List<Map> findStypeCountsByDepartment(HttpSession session) {

        return this.studentResultService.findStypeCountsByDepartment((String) session.getAttribute("department"));
    }

    @RequestMapping(value = "/admin/findAgeCountsByDepartment", method = RequestMethod.POST)
    @ResponseBody
    public List<Map> findAgeCountsByDepartment(HttpSession session) {

        return this.studentResultService.findAgeCountsByDepartment((String) session.getAttribute("department"));
    }

    @RequestMapping(value = "/admin/findGenderCountsByDepartment", method = RequestMethod.POST)
    @ResponseBody
    public List<Map> findGenderCountsByDepartment(HttpSession session) {

        return this.studentResultService.findGenderCountsByDepartment((String) session.getAttribute("department"));
    }

    @RequestMapping(value = "/admin/findAllCountsByDepartment", method = RequestMethod.POST)
    @ResponseBody
    public List<Map> findAllCountsByDepartment(HttpSession session) {

        return this.studentResultService.findAllCountsByDepartment((String) session.getAttribute("department"));
    }

    @RequestMapping(value = "/admin/findLast", method = RequestMethod.POST)
    @ResponseBody
    public List<StudentResultModel> findLast(HttpSession session) {

        return this.studentResultService.findLast((String) session.getAttribute("department"));
    }

    @RequestMapping(value = "/admin/findListByDepartmentAndCollege", method = RequestMethod.POST)
    @ResponseBody
    public List<StudentResultModel> findListByDepartmentAndCollege(HttpSession session) {

        return this.collegeStudentResultService.findListByDepartmentAndCollege(
                (String) session.getAttribute("department"), (String) session.getAttribute("college"));
    }

    @RequestMapping(value = "/admin/findByUserId", method = RequestMethod.POST)
    @ResponseBody
    public StudentResultModel login(@RequestBody String user, HttpSession session) {
        JSONObject j = JSONObject.parseObject(user);
        return this.studentResultService.findByUserId(j.getLong("userId"));

    }

    @RequestMapping(value = "/admin/findByStid", method = RequestMethod.POST)
    @ResponseBody
    public StudentResultModel findByStid(@RequestBody String user, HttpSession session) {
        JSONObject j = JSONObject.parseObject(user);
        return this.studentResultService.findByStid((String) session.getAttribute("department"), j.getString("stid"));

    }

    /**
     * 学院接口
     */
    @RequestMapping(value = "/admin/findProvinceCountsByDepartmentAndCollege", method = RequestMethod.POST)
    @ResponseBody
    public List<Map> findProvinceCountsByDepartmentAndCollege(HttpSession session) {

        return this.collegeStudentResultService.findProvinceCountsByDepartment(
                (String) session.getAttribute("department"), (String) session.getAttribute("college"));
    }

    @RequestMapping(value = "/admin/findStypeCountsByDepartmentAndCollege", method = RequestMethod.POST)
    @ResponseBody
    public List<Map> findStypeCountsByDepartmentAndCollege(HttpSession session) {

        return this.collegeStudentResultService.findStypeCountsByDepartment((String) session.getAttribute("department"),
                (String) session.getAttribute("college"));
    }

    @RequestMapping(value = "/admin/findAgeCountsByDepartmentAndCollege", method = RequestMethod.POST)
    @ResponseBody
    public List<Map> findAgeCountsByDepartmentAndCollege(HttpSession session) {

        return this.collegeStudentResultService.findAgeCountsByDepartment((String) session.getAttribute("department"),
                (String) session.getAttribute("college"));
    }

    @RequestMapping(value = "/admin/findAllCountsByDepartmentAndCollege", method = RequestMethod.POST)
    @ResponseBody
    public List<Map> findAllCountsByDepartmentAndCollege(HttpSession session) {

        return this.collegeStudentResultService.findAllCountsByDepartment((String) session.getAttribute("department"),
                (String) session.getAttribute("college"));
    }

    /**
     * 提供图片base64下载
     * 
     * @param filePath 图片相对路径
     * @return String base64
     */
    @RequestMapping(value = "/admin/getFace")
    @ResponseBody
    public String getFace(@RequestParam(value = "imgPath", required = true) String filePath) {

        // 转为base64
        try {
            InputStream in = new FileInputStream(filePath);
            byte[] data = new byte[in.available()];
            in.read(data);
            in.close();
            Base64.Encoder encoder = Base64.getEncoder();
            return "data:image/jpg;base64," + encoder.encodeToString(Objects.requireNonNull(data));
        } catch (Exception e) {
            log.error("处理错误");
            log.error(e.getMessage());
            DealMessageResponse res = new DealMessageResponse();
            res.setErrorCode(503);
            res.setErrorMsg("服务器错误");
            return JSON.toJSONString(res);
        }
    }

    /*
     * 备用
     */
    // @RequestMapping(value = "/admin/getServiceData", method = RequestMethod.POST)
    // @ResponseBody
    // public String getServiceData(HttpSession session){
    // JSONObject j = new JSONObject();
    // j.put("regist", this.baseUserService.)
    //
    //
    // }

    /**
     * 上传学生信息excl文件
     */

    @RequestMapping(value = "/admin/upload")
    @ResponseBody
    public String upload(HttpServletRequest request, @RequestParam(value = "file", required = true) MultipartFile file)
            throws IOException {

        DealMessageResponse dm = new DealMessageResponse();

        if (this.studentInfoSubmitService.save(request.getSession(), file)) {
            dm.setErrorCode(200);
            dm.setErrorMsg("上传成功");
            return JSON.toJSONString(dm);
        } else {
            dm.setErrorCode(500);
            dm.setErrorMsg("上传失败");
            return JSON.toJSONString(dm);
        }
    }
}
