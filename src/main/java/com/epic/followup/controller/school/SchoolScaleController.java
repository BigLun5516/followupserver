package com.epic.followup.controller.school;

import java.io.File;

import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/school/scale")
public class SchoolScaleController {

    // ---------------- 量表模板 -----------

    // 量表模板的使用
    @RequestMapping("/template/addScale")
    public JSONObject useTemplate() {
        return null;
    }

    // ---------------- 我的量表 -----------
}
