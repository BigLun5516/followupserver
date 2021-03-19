package com.epic.followup.controller.ncov;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epic.followup.conf.NCovConfig;
import com.epic.followup.model.app.MiniScalePublicModel;
import com.epic.followup.model.ncov.NCovResultModel;
import com.epic.followup.model.ncov.NCovUserInfoModel;
import com.epic.followup.repository.app.MiniScalePublicRepository;
import com.epic.followup.repository.ncov.NCovUserInfoRepository;
import com.epic.followup.service.WechatUserService;
import com.epic.followup.service.ncov.NCovUserService;
import com.epic.followup.service.ncov.ScaleResultService;
import com.epic.followup.service.ncov.ScaleService;
import com.epic.followup.temporary.DealMessageResponse;
import com.epic.followup.temporary.NormalUserRequest;
import com.epic.followup.temporary.ncov.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author : zx
 * @version V1.0
 */

@Controller
@RequestMapping("/ncov")
// 允许跨域，记得关闭
@CrossOrigin(origins = "*",maxAge = 3600)
public class NCovController {
    @Autowired
    private WechatUserService wechatUserService;
    @Autowired
    private ScaleService scaleService;
    @Autowired
    private ScaleResultService scaleResultService;
    @Autowired
    private NCovUserService nCovUserService;
    @Autowired
    private NCovConfig nCovConfig;
    @Autowired
    private MiniScalePublicRepository miniScalePublicRepository;

    @RequestMapping(value = "/getscale", method = RequestMethod.POST)
    @ResponseBody
    public String getData(@RequestBody GetScaleRequest userInfo){

        GetScaleResponse dm = new GetScaleResponse();

        // 校验用户身份合法性
        if (!wechatUserService.checkUser(userInfo.getOpenid(), userInfo.getSession_key())){
            dm.setErrorCode(500);
            dm.setErrorMsg("/wechat/chart: 用户登录状态异常");
            return JSON.toJSONString(dm);
        }

        JSONArray j = JSONArray.parseArray(scaleService.getQuesJson(userInfo.getFileName()));

        dm.setQuestionNum(j.size());
        dm.setQuestion(j);

        dm.setErrorCode(200);
        dm.setErrorMsg("succ");
        return JSON.toJSONString(dm);
    }

    @RequestMapping(value = "/submitscale", method = RequestMethod.POST)
    @ResponseBody
    public String submitData(@RequestBody SubmitScaleRequest userInfo){
        SubmitScaleResponse dm = new SubmitScaleResponse();

        // 校验用户身份合法性
        if (!wechatUserService.checkUser(userInfo.getOpenid(), userInfo.getSession_key())){
            dm.setErrorCode(500);
            dm.setErrorMsg("/submitscale: 用户登录状态异常");
            return JSON.toJSONString(dm);
        }

        NCovResultModel nc = new NCovResultModel();
        nc.setAnswerArray(userInfo.getAnswers());
        nc.setOpenId(userInfo.getOpenid());
        nc.setAnswerTime(new Date());
        nc.setLevel(getLevel(userInfo.getQuestionNaire(), getScore(userInfo.getQuestionNaire(), userInfo.getAnswers())));
        nc.setScore(getScore(userInfo.getQuestionNaire(), userInfo.getAnswers()));
        nc.setScaleId(userInfo.getQuestionNaire());
        nc.setText(userInfo.getText());
        scaleResultService.saveResult(nc);

        dm.setLevel(nc.getLevel());
        dm.setScore(nc.getScore());
        dm.setErrorCode(200);
        dm.setErrorMsg("succ");
        return JSON.toJSONString(dm);
    }

    @RequestMapping(value = "/submitPublicScale", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject submitPublicScale(@RequestBody JSONObject userInfo){
        JSONObject res = new JSONObject();


        MiniScalePublicModel mini=new MiniScalePublicModel();
        mini.setMiniAnswer(userInfo.getString("answer"));
        mini.setOpenId(userInfo.getString("openid"));
        mini.setMiniTime(new Date());
        mini.setMiniResult(userInfo.getString("result"));
        mini.setTotal_time(userInfo.getString("totalTime"));
        mini.setUserName(userInfo.getString("userName"));
        miniScalePublicRepository.save(mini);

        res.put("errorCode",200);
        res.put("errorMsg","success");
        return res;
    }

    //查询小程序mini量表结果
    @RequestMapping(value = "/miniResult", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject miniResult(){
        JSONObject res = new JSONObject();
        List<MiniScalePublicModel> dataList = miniScalePublicRepository.findAll();
        List<Map<String, Object>> data = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(MiniScalePublicModel d:dataList){
            Map<String, Object> item = new HashMap<>();
            item.put("mini_answer",d.getMiniAnswer());
            item.put("mini_result",d.getMiniResult());
            item.put("mini_time",dateFormat.format(d.getMiniTime()));
            item.put("total_time",d.getTotal_time());
            item.put("user_name",d.getUserName());
            data.add(item);
        }
        res.put("data",data);
        res.put("errorCode",200);
        res.put("errorMsg","success");
        return res;
    }


    /*
     * 提交基本信息
     */
    @RequestMapping(value = "/submitinfo", method = RequestMethod.POST)
    @ResponseBody
    public String submitInfo(@RequestBody SubmitUserInfoRequest userInfo){
        DealMessageResponse dm = new DealMessageResponse();

        // 校验用户身份合法性
        if (!wechatUserService.checkUser(userInfo.getOpenid(), userInfo.getSession_key())){
            dm.setErrorCode(500);
            dm.setErrorMsg("/submitinfo: 用户登录状态异常");
            return JSON.toJSONString(dm);
        }

        NCovUserInfoModel nc = nCovUserService.findByOpenId(userInfo.getOpenid());
        if (nc == null){
            nc = new NCovUserInfoModel();
        }
        nc.setOpenId(userInfo.getOpenid());
        nc.setInfo(userInfo.getMessage());
        nCovUserService.saveInfo(nc);

        dm.setErrorCode(200);
        dm.setErrorMsg("succ");
        return JSON.toJSONString(dm);
    }

    /*
     * 提交近况
     */
    @RequestMapping(value = "/submitrecent", method = RequestMethod.POST)
    @ResponseBody
    public String submitRecent(@RequestBody SubmitUserInfoRequest userInfo){
        DealMessageResponse dm = new DealMessageResponse();

        // 校验用户身份合法性
        if (!wechatUserService.checkUser(userInfo.getOpenid(), userInfo.getSession_key())){
            dm.setErrorCode(500);
            dm.setErrorMsg("/submitrecent: 用户登录状态异常");
            return JSON.toJSONString(dm);
        }

        NCovUserInfoModel nc = nCovUserService.findByOpenId(userInfo.getOpenid());
        if (nc == null){
            nc = new NCovUserInfoModel();
        }
        nc.setOpenId(userInfo.getOpenid());
        nc.setRecent(userInfo.getMessage());
        nCovUserService.saveInfo(nc);

        dm.setErrorCode(200);
        dm.setErrorMsg("succ");
        return JSON.toJSONString(dm);
    }

    /*
     * 提交医护专用版的事件影响
     */
    @RequestMapping(value = "/submitncov", method = RequestMethod.POST)
    @ResponseBody
    public String submitNcov(@RequestBody SubmitUserInfoRequest userInfo){
        DealMessageResponse dm = new DealMessageResponse();

        // 校验用户身份合法性
        if (!wechatUserService.checkUser(userInfo.getOpenid(), userInfo.getSession_key())){
            dm.setErrorCode(500);
            dm.setErrorMsg("/submitncov: 用户登录状态异常");
            return JSON.toJSONString(dm);
        }

        NCovUserInfoModel nc = nCovUserService.findByOpenId(userInfo.getOpenid());
        if (nc == null){
            nc = new NCovUserInfoModel();
        }
        nc.setOpenId(userInfo.getOpenid());
        nc.setDoctorNcov(userInfo.getMessage());
        nCovUserService.saveInfo(nc);

        dm.setErrorCode(200);
        dm.setErrorMsg("succ");
        return JSON.toJSONString(dm);
    }

    /*
     * 提交心理需求
     */
    @RequestMapping(value = "/submitneed", method = RequestMethod.POST)
    @ResponseBody
    public String submitNeed(@RequestBody SubmitUserInfoRequest userInfo){
        DealMessageResponse dm = new DealMessageResponse();

        // 校验用户身份合法性
        if (!wechatUserService.checkUser(userInfo.getOpenid(), userInfo.getSession_key())){
            dm.setErrorCode(500);
            dm.setErrorMsg("/submitneed: 用户登录状态异常");
            return JSON.toJSONString(dm);
        }

        NCovUserInfoModel nc = nCovUserService.findByOpenId(userInfo.getOpenid());
        if (nc == null){
            nc = new NCovUserInfoModel();
        }
        nc.setOpenId(userInfo.getOpenid());
        nc.setNeed(userInfo.getMessage());
        nc.setText(userInfo.getText());
        nCovUserService.saveInfo(nc);

        dm.setErrorCode(200);
        dm.setErrorMsg("succ");
        return JSON.toJSONString(dm);
    }

    /*
     * 获取评测结果
     */
    @RequestMapping(value = "/getresults", method = RequestMethod.POST)
    @ResponseBody
    public String getResult(@RequestBody NormalUserRequest userInfo){
        DealMessageResponse dm = new DealMessageResponse();

        // 校验用户身份合法性
        if (!wechatUserService.checkUser(userInfo.getOpenid(), userInfo.getSession_key())){
            dm.setErrorCode(500);
            dm.setErrorMsg("/submitrecent: 用户登录状态异常");
            return JSON.toJSONString(dm);
        }

        GetScaleResultResponse gsr = scaleResultService.getRecentScales(userInfo.getOpenid());

        gsr.setErrorCode(200);
        gsr.setErrorMsg("succ");
        return JSON.toJSONString(gsr);
    }

    /*
     * 获取学生端评测结果
     */
    @RequestMapping(value = "/getsturesults", method = RequestMethod.POST)
    @ResponseBody
    public String getStuResult(@RequestBody NormalUserRequest userInfo){
        DealMessageResponse dm = new DealMessageResponse();

        // 校验用户身份合法性
        if (!wechatUserService.checkUser(userInfo.getOpenid(), userInfo.getSession_key())){
            dm.setErrorCode(500);
            dm.setErrorMsg("/submitrecent: 用户登录状态异常");
            return JSON.toJSONString(dm);
        }

        GetScaleResultResponse gsr = scaleResultService.getStuRecentScales(userInfo.getOpenid());

        gsr.setErrorCode(200);
        gsr.setErrorMsg("succ");
        return JSON.toJSONString(gsr);
    }


    /*
     * 计算得分
     */
    private static int getScore(int num, int[]answers){
        int sum = 0;
        switch (num){
            case 0:
                // 自杀
                for (int i : answers){
                    sum += i;
                }
                return sum;

            case 1:
                // 抑郁症
                for (int i : answers){
                    sum += i;
                }
                return sum;
            case 2:
                // 焦虑
                for (int i : answers){
                    sum += i;
                }
                return sum;

            case 3:
                // 失眠严重指数
                for (int i : answers){
                    sum += i;
                }
                return sum;
            case 4:
                // 应激
                for (int i : answers){
                    sum += i;
                }
                return sum;

            default:
                 return -1;
        }
    }


    /*
     * 评价
     */
    private static String getLevel(int num, int score){
        switch (num){
            case 0:
                // 自杀检测
                if (score >= 0 && score <= 4){
                    return "没有自杀倾向，继续保持健康的生活方式，祝您身体健康。";
                }else if (score >= 5 && score <= 9){
                    return "建议您适当调节心理状态，保持良好的心态，祝您身体健康。  ";
                }else if (score >= 10 && score <= 14){
                    return "建议您规律作息，适量运动，转移注意力，适当心理调整（可请专业人士帮助），祝您身体健康。";
                }else if (score >= 15 && score <= 19){
                    return "请及时寻求专业心理医生的帮助，祝您身体健康。";
                }
                break;

            case 1:
                // 抑郁症筛查量表
                if (score >= 0 && score <= 4){
                    return "没有抑郁，继续保持健康的生活方式，祝您身体健康。";
                }else if (score >= 5 && score <= 9){
                    return "建议您及时疏导情绪，调整生活作息，祝您身体健康。  ";
                }else if (score >= 10 && score <= 14){
                    return "建议您规律作息，适量运动，转移注意力，适当心理调整（可请专业人士帮助），祝您身体健康。";
                }else if (score >= 15 && score <= 27){
                    return "请及时寻求专业心理医生的帮助，祝您身体健康。";
                }
                break;
            case 2:
                // 广泛性焦虑量表
                if (score >= 0 && score <= 4){
                    return "正常，没有焦虑，非常好，请继续保持。";
                }else if (score >= 5 && score <= 9){
                    return "建议您适当放松，明天会更好。";
                }else if (score >= 10 && score <= 14){
                    return "建议您规律生活，适当心理调整（可请专业人士帮助）。  ";
                }else if (score >= 15 && score <= 21){
                    return "建议您向专业人士咨询，积极调整。 ";
                }
                break;

            case 3:
                // 失眠严重指数
                if (score >= 0 && score <= 7){
                    return "没有临床上显著的失眠症，非常好，请继续保持。 ";
                }else if (score >= 8 && score <= 14){
                    return "建议您规律作息，适量运动。 ";
                }else if (score >= 15 && score <= 21){
                    return "建议您规律作息，减少日间卧床时间，适量运动，必要时请专业人士帮助。";
                }else if (score >= 22 && score <= 28){
                    return "建议您向专业人士咨询，积极调整。";
                }
                break;
            case 4:
                // 事件影响量表
                if (score >= 0 && score <= 8){
                    return "无明显应激反应，非常好，请继续保持。 ";
                }else if (score >= 9 && score <= 25){
                    return "建议您适当放松，明天会更好。 ";
                }else if (score >= 26 && score <= 43){
                    return "建议您转移注意力，适当心理调整（可请专业人士帮助）。 ";
                }else if (score >= 44 && score <= 88){
                    return "建议您向专业人士咨询，积极调整。";
                }
                break;

            default:
                return "数据异常";
        }
        return "数据异常";
    }
}
