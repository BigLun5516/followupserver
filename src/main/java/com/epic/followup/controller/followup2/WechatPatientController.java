package com.epic.followup.controller.followup2;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.app.MiniScaleModel;
import com.epic.followup.service.followup2.BaseUserService;
import com.epic.followup.service.followup2.WechatPatientService;
import com.epic.followup.temporary.DealMessageResponse;
import com.epic.followup.temporary.app.MiniSubmitRequest;
import com.epic.followup.temporary.followup2.session.BaseUserSession;
import com.epic.followup.temporary.wechat.patient.diary.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/followup2/patient")
public class WechatPatientController {

    @Autowired
    private BaseUserService baseUserService;

    @Autowired
    private WechatPatientService wechatPatientService ;

    //---------个人身体信息部分开始--------

    @RequestMapping(value = "/bodyInfo/saveorupdateInfo", method = RequestMethod.POST)
    @ResponseBody
    public DealMessageResponse saveorupdateInfo(HttpServletRequest request, @RequestBody JSONObject information ){
        BaseUserSession bus = baseUserService.findBySessionId(request.getHeader("sessionId"));

        return wechatPatientService.saveorupdateInformation(bus,information);
    }

    @RequestMapping(value = "/bodyInfo/retrieveInfo", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject retrieveInfo(HttpServletRequest request,@RequestBody JSONObject information ){

        BaseUserSession bus = baseUserService.findBySessionId(request.getHeader("sessionId"));

        return wechatPatientService.retrieveInformation(bus,information);
    }


    @PostMapping(value = "/bodyInfo/getSevenDaysInfo")
    @ResponseBody
    public JSONObject getbodyInfo(HttpServletRequest request){

        BaseUserSession bus = baseUserService.findBySessionId(request.getHeader("sessionId"));

        return wechatPatientService.getSevenDaysBodyInfo(bus);
    }

    @PostMapping(value = "/bodyInfo/getDays")
    @ResponseBody
    public JSONObject getDays(HttpServletRequest request, @RequestBody JSONObject obj){

        BaseUserSession bus = baseUserService.findBySessionId(request.getHeader("sessionId"));
        return wechatPatientService.getDaysByMonth(bus, obj.getString("month"));
    }


    //------日记部分开始-------

    @RequestMapping(value = "/diary/uploadDiaryImg", method = RequestMethod.POST)
    @ResponseBody
    public upLoadDiaryImgResponse uploadDiaryImg(@RequestParam MultipartFile file){

        return wechatPatientService.uploadDiaryImg(file);
    }

    @RequestMapping(value = "/diary/saveDiary", method = RequestMethod.POST)
    @ResponseBody
    public savePatientDiaryResponse saveDiary(HttpServletRequest request, @RequestBody savePatientDiaryRequest saveDiaryRequest){

        BaseUserSession bus = baseUserService.findBySessionId(request.getHeader("sessionId"));

        return wechatPatientService.saveDiary(bus,saveDiaryRequest);
    }

    @RequestMapping(value = "/diary/getDiary", method = RequestMethod.POST)
    @ResponseBody
    public getPatientDiaryResponse getDiary(@RequestBody diaryIdRequest diaryIdRequest){

        return wechatPatientService.getPatientDiaryById(diaryIdRequest);
    }

    @RequestMapping(value = "/diary/deleteDiary", method = RequestMethod.POST)
    @ResponseBody
    public DealMessageResponse deleteDiary(@RequestBody diaryIdRequest diaryIdRequest){

        return wechatPatientService.delPatientDiaryById(diaryIdRequest);
    }

    @RequestMapping(value = "/diary/getAllDiaries", method = RequestMethod.POST)
    @ResponseBody
    public getAllPatientDiaryResponse getAllDiaries(HttpServletRequest request){

        BaseUserSession bus = baseUserService.findBySessionId(request.getHeader("sessionId"));

        return wechatPatientService.getAllPatientDiary(bus);
    }

    @RequestMapping(value = "/diary/getMood", method = RequestMethod.POST)
    @ResponseBody
    public getAllMoodsResponse getAllMoods(HttpServletRequest request){

        BaseUserSession bus = baseUserService.findBySessionId(request.getHeader("sessionId"));

        return wechatPatientService.getAllPatientMoods(bus);
    }

    // mini量表结果保存
    @PostMapping(value = "/mini/submit")
    @ResponseBody
    public String submitMiniScale(HttpServletRequest request, @RequestBody MiniSubmitRequest miniSubmitRequest){

        BaseUserSession bus = baseUserService.findBySessionId(request.getHeader("sessionId"));

        return JSON.toJSONString(wechatPatientService.saveMiniResult(bus.getUserId(), miniSubmitRequest));
    }

    //新生量表结果保存
    @PostMapping(value = "/newStudent/submit")
    @ResponseBody
    public JSONObject submitNewStudentScale(HttpServletRequest request, @RequestBody JSONObject submitInfo){

        BaseUserSession bus = baseUserService.findBySessionId(request.getHeader("sessionId"));

        return wechatPatientService.saveNewStudentResult(bus.getUserId(), submitInfo);
    }


}
