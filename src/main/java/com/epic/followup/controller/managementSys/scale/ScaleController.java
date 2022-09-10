package com.epic.followup.controller.managementSys.scale;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.managementSys.NewScaleModel;
import com.epic.followup.repository.managementSys.NewScaleRepository;
import com.epic.followup.service.managementSys.scale.ScaleService;
import com.epic.followup.util.ExcelUtils;
import com.epic.followup.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Map;
import java.util.Random;

@Controller
@CrossOrigin
@RequestMapping("/managementSystem/scale")
@Slf4j
public class ScaleController {

    public static String PQ_PATH = "/home/followup/static/SchoolSurvey/data/PQ.xlsx";

    public static String SQ_PATH = "/home/followup/static/SchoolSurvey/data/SQ.xlsx";
    public static String SQTemplate_PATH = "/home/followup/static/SchoolSurvey/data/SQTemplate.xlsx";

    public static String TQ_PATH = "/home/followup/static/SchoolSurvey/data/TQ.xlsx";
    public static String TQTemplate_PATH = "/home/followup/static/SchoolSurvey/data/TQTemplate.xlsx";

    @Autowired
    private ScaleService scaleService;

    @Autowired
    private NewScaleRepository newScaleRepository;

    /**
     * 量表管理
     */

    // 获取量表列表
    @PostMapping("/scaleList")
    @ResponseBody
    public JSONObject getScaleList(@RequestBody JSONObject params, HttpSession session) {

        params.put("userUniversityId", session.getAttribute("universityId"));

        return scaleService.getScaleList(params);
    }

    // 量表禁用接口
    @PutMapping("/scaleDisable")
    @ResponseBody
    public JSONObject putScaleDisable(@RequestBody JSONObject params, HttpSession session) {
        params.put("userUniversityId", session.getAttribute("universityId"));
        return scaleService.editScaleStatus(params);
    }

    // 量表删除接口
    @DeleteMapping("/scaleDelete")
    @ResponseBody
    public JSONObject DeleteScale(@RequestBody JSONObject params) {
        return scaleService.DeleteScale(params);
    }

    // 后台任务管理获取学校对应的所有量表用于新增
    @PostMapping("/findScaleName")
    @ResponseBody
    public JSONObject findScaleName(HttpSession session) {

        JSONObject params = new JSONObject();
        params.put("pageNum", 1);
        params.put("pageSize", 10);
        params.put("scalename", "");
        params.put("schoolname", "");
        params.put("isnz", "");
        params.put("status", 1);
        params.put("userUniversityId", session.getAttribute("universityId"));
        return scaleService.getScaleList(params);
    }

    // 胡老师新增的量表的上传接口
    @PostMapping("/uploadScale")
    @ResponseBody
    public synchronized JSONObject uploadScale(@RequestBody JSONObject req, HttpServletRequest request) {
        JSONObject res = new JSONObject();
        JSONArray answer = req.getJSONArray("answer");
        String ipAddress = IpUtil.getIpAddr(request);
        log.info(ipAddress);
        String region = IpUtil.recordIp(ipAddress);
        log.info(region);
        Integer scaleId = req.getInteger("scaleId");
        String startTime = req.getString("startTime");
        String finishTime = req.getString("finishTime");
        Long timeDur = ExcelUtils.timeDuration(startTime, finishTime);
        //表id不能为空
        if(scaleId == null){
            res.put("code", "502");
            res.put("msg", "表id不能为空");
            return res;
        }
        String path;
        if(scaleId == 1){
            path = SQ_PATH;
        }else if(scaleId == 2){
            path = PQ_PATH;
        }else{
            path = TQ_PATH;
        }
        answer.add(0,ipAddress);
        answer.add(0,timeDur + "秒");
        answer.add(0,startTime);
        ExcelUtils.writeExcel(path,answer);

        //数据入库
        NewScaleModel newScaleModel = new NewScaleModel();
        newScaleModel.setAnswer(answer.toString());
        newScaleModel.setStartTime(startTime);
        newScaleModel.setFinishTime(finishTime);
        newScaleModel.setIp(ipAddress);
        newScaleModel.setRegion(region);
        newScaleModel.setPhone("");
        newScaleModel.setScaleId(scaleId);
        newScaleRepository.save(newScaleModel);
        res.put("code", "200");
        res.put("msg", "插入成功");
        return res;
    }


}
