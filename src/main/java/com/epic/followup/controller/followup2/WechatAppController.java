package com.epic.followup.controller.followup2;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epic.followup.conf.WeChatConfig;
import com.epic.followup.model.followup2.student.*;
import com.epic.followup.model.followup2.WechatAppUserModel;
import com.epic.followup.service.KnowledgeMapService;
import com.epic.followup.service.NLPService;
import com.epic.followup.service.followup2.BaseUserService;
import com.epic.followup.service.followup2.WechatAppUserService;
import com.epic.followup.service.followup2.WechatCCBTService;
import com.epic.followup.service.followup2.doctor.StudentResultService;
import com.epic.followup.service.followup2.student.*;
import com.epic.followup.temporary.*;
import com.epic.followup.temporary.followup2.CCBTAccessAnswerRequest;
import com.epic.followup.temporary.followup2.session.BaseUserSession;
import com.epic.followup.temporary.ncov.GetScaleResultResponse;
import com.epic.followup.temporary.ncov.SubmitScaleRequest;
import com.epic.followup.temporary.ncov.SubmitScaleResponse;
import com.epic.followup.util.DateTimeUtils;
import com.epic.followup.util.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.xmlrpc.XmlRpcException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.epic.followup.util.HTTPsUtils.get;

/**
 * @author : zx
 * @version V1.0
 */

@Controller
@CrossOrigin
@RequestMapping("/followup2/wechat")
public class WechatAppController {

    private WechatAppUserService wechatAppUserService;
    private WeChatConfig weChatConfig;
    private Answer2Service answer2Service;
    private WechatFile2Service wechatFile2Service;
    private Result2Service result2Service;
    private NLPService nlpService;
    private BaseUserService baseUserService;
    private StudentResultService studentResultService;
    private ScaleResult2Service scaleResult2Service;
    private KnowledgeMapService knowledgeMapService;
    private org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public CCBTAccessAnswerService ccbtAccessAnswerService;

    @Autowired
    WechatCCBTService wechatCCBTService; // ?????????CCBT

    @Autowired
    public WechatAppController(WechatAppUserService wechatAppUserService,
                               WeChatConfig weChatConfig,
                               Answer2Service answer2Service,
                               Result2Service result2Service,
                               WechatFile2Service wechatFile2Service,
                               NLPService nlpService,
                               BaseUserService baseUserService,
                               StudentResultService studentResultService,
                               ScaleResult2Service scaleResult2Service,
                               KnowledgeMapService knowledgeMapService
    ) {
        this.wechatAppUserService = wechatAppUserService;
        this.weChatConfig = weChatConfig;
        this.answer2Service = answer2Service;
        this.result2Service = result2Service;
        this.wechatFile2Service = wechatFile2Service;
        this.nlpService = nlpService;
        this.baseUserService = baseUserService;
        this.studentResultService = studentResultService;
        this.scaleResult2Service = scaleResult2Service;
        this.knowledgeMapService = knowledgeMapService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseBody
    public String getLoginStatus(@RequestParam(value = "code", required = true) String code) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("secret", weChatConfig.getAppSecert());
        params.put("appid", weChatConfig.getAppID());
        params.put("js_code", code);
        params.put("grant_type", weChatConfig.getGrantType());

        String result = null;
        try {
            result = get(weChatConfig.getUrl(), params, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject r = JSON.parseObject(result);
        if (r.getString("openid") != null) {

            WechatAppUserModel u = wechatAppUserService.findByOpenId(r.getString("openid"));
            if (u != null) {
                u.setSessionKey(r.getString("session_key"));
                u.setOpenId(r.getString("openid"));
            } else {
                u = new WechatAppUserModel();
                u.setSessionKey(r.getString("session_key"));
                u.setOpenId(r.getString("openid"));
                u.setCreateTime(new Date());
            }

            wechatAppUserService.updateUser(u);

            r.put("errorCode", 200);
            r.put("sessionId", u.getSessionId() == null ? "-" : u.getSessionId());
            return JSON.toJSONString(r);
        } else {
            return result;
        }
    }

    @RequestMapping(value = "/answer/test", method = RequestMethod.GET)
    @ResponseBody
    public int rpcServerTest(HttpServletRequest request) {
//        BaseUserSession bus = baseUserService.findBySessionId(request.getHeader("sessionId"));
        int i = nlpService.fun_add();
        System.out.println(i);
        return i;
    }

    @RequestMapping(value = "/answer/submit", method = RequestMethod.POST)
    @ResponseBody
    public String getLoginStatus(HttpServletRequest request, @RequestBody AnsewerSubmitRequest answerInfo) {
        BaseUserSession bus = baseUserService.findBySessionId(request.getHeader("sessionId"));
        AnswerResponse dm = new AnswerResponse();

        // ????????????????????????1.????????????????????????5??????????????????????????????????????? 2.??????????????????????????????
        AnswerModel lastAnswer = answer2Service.getLastAnswer(bus.getUserId());
        if (lastAnswer != null) {
            // ???????????????????????????
            Date nowDate = new Date();
            if ((int) ((nowDate.getTime() - lastAnswer.getAnswerTime().getTime()) / (24 * 60 * 60 * 1000)) == 0) {
                // ??????????????????????????????????????????
                if (answerInfo.getQuestion() - lastAnswer.getNumber() != 1) {
                    dm.setErrorCode(504);
                    dm.setErrorMsg("????????????????????????????????????[" + (lastAnswer.getNumber() + 1) + "].");
                    dm.setNextQuestion(lastAnswer.getNumber() + 1);
                    return JSON.toJSONString(dm);
                }
                // ??????????????????
                if (nowDate.getTime() - lastAnswer.getAnswerTime().getTime() > (weChatConfig.getDelay() * 60 * 1000)) {
                    dm.setErrorCode(504);
                    dm.setErrorMsg("????????????.");
                    return JSON.toJSONString(dm);
                }
            }
            // ??????????????????????????????
        }

        // ????????????????????????
        AnswerModel submitAnswer = Answer2Service.transformAnswerRequest(answerInfo, bus.getUserId());
        if (answerInfo.getResult() == -1) {
            double result;
            try {
                result = nlpService.questionFunSelect(submitAnswer.getNumber(), submitAnswer.getAnswerResult());
            } catch (XmlRpcException e) {
                e.printStackTrace();
                dm.setErrorCode(505);
                dm.setErrorMsg("???????????????,RPC????????????");
                return JSON.toJSONString(dm);
            }

            submitAnswer.setAnalyseResult(result);
        } else {
            // ????????????2 ???????????????

//            submitAnswer.setAnalyseResult(answerInfo.getResult() >= 2 ? 1 : 0);
            submitAnswer.setAnalyseResult(answerInfo.getResult() >= 0 ? answerInfo.getResult() : 0);
        }

        if (answer2Service.insert(submitAnswer)) {
            dm.setErrorCode(200);
            dm.setErrorMsg("succ");
            return JSON.toJSONString(dm);
        } else {
            dm.setErrorCode(505);
            dm.setErrorMsg("???????????????");
            return JSON.toJSONString(dm);
        }
    }

    @RequestMapping(value = "/answer/query", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public String getLoginStatus(HttpServletRequest request, @RequestBody AnswerQueryRequest answerInfo) throws Exception {
        BaseUserSession bus = baseUserService.findBySessionId(request.getHeader("sessionId"));
        AnswerResponse dm = new AnswerResponse();

        //  checkOrEnd : 1 ?????? ???2 ??????????????????0??? 3 ??????????????????1???
        if (answerInfo.getCheckOrEnd() == 1) {
            AnswerModel lastAnswer = answer2Service.getLastAnswer(bus.getUserId());

            if (lastAnswer == null) {
                dm.setErrorCode(200);
                dm.setErrorMsg("?????????");
                dm.setNextQuestion(1);
                return JSON.toJSONString(dm);
            }
            Date nowDate = new Date();
            if (nowDate.getTime() - lastAnswer.getAnswerTime().getTime() > (weChatConfig.getDelay() * 60 * 1000)) {
                // ??????????????????
                answer2Service.deleteOutData(bus.getUserId());
                wechatFile2Service.deleteOutData(bus.getUserId());
                dm.setErrorCode(504);
                dm.setErrorMsg("????????????.");
                dm.setNextQuestion(-1);
                return JSON.toJSONString(dm);
            }
            dm.setErrorCode(200);
            dm.setErrorMsg("succ");
            dm.setNextQuestion(lastAnswer.getNumber() + 1);
            return JSON.toJSONString(dm);
        } else if (answerInfo.getCheckOrEnd() == 2) {
            answer2Service.deleteOutData(bus.getUserId());
            wechatFile2Service.deleteOutData(bus.getUserId());
            dm.setErrorCode(200);
            dm.setErrorMsg("succ????????????");
            dm.setNextQuestion(1);
            return JSON.toJSONString(dm);
        } else if (answerInfo.getCheckOrEnd() == 3) {
            // ??????result
            List<AnswerModel> al = answer2Service.getAppointDayUnsuccData(bus.getUserId(), 0);
            List<WechatFileModel> fl = wechatFile2Service.getAppointDayUnsuccData(bus.getUserId(), 0);
            long[] aa = new long[al.size()];
            long[] fa = new long[fl.size()];
            for (int i = 0; i < al.size(); i++) {
                aa[i] = al.get(i).getId();
            }
            for (int i = 0; i < fl.size(); i++) {
                fa[i] = fl.get(i).getId();
            }
            ResultModel rm = new ResultModel();
            rm.setAnswerIds(aa);
            rm.setWechatFileIds(fa);
            rm.setLevel(answer2Service.getDPLevel(al));
            rm.setUserId(bus.getUserId());
            rm.setScore(answer2Service.countScore(al));
            rm.setAnswerTime(al.get(0).getAnswerTime());
            result2Service.saveResult(rm);

            /*
             * ??????student result
             */
            StudentInfo stinfo = this.baseUserService.getStudentInfoByUserID(bus.getUserId());

            /* ??????sym ????????? ???1???20???
  `            * ??????
                ??????/????????? ?????? ??????
                ???????????? ????????????????????? ?????? ???????????? ????????????
                ???????????? ???????????? ???????????? ???????????? ???????????? ????????????
                ???????????? ???????????? ??????????????????????????? ???????????? ??????????????????????????? ???????????????
            */
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < al.size(); i++) {
//                log.info(Integer.toString((int)al.get(i).getAnalyseResult()));
                sb.append(Integer.toString((int) al.get(i).getAnalyseResult()));
            }
            this.studentResultService.addResult(bus.getUserId(), stinfo, sb.toString(), rm.getScore(), rm.getLevel());


            answer2Service.updateSucc(bus.getUserId());
            wechatFile2Service.updateSucc(bus.getUserId());
            dm.setErrorCode(200);
            dm.setErrorMsg("succ,?????????");
            dm.setNextQuestion(0);
            return JSON.toJSONString(dm);
        } else {
            dm.setErrorCode(503);
            dm.setErrorMsg("????????????.");
            return JSON.toJSONString(dm);
        }
    }

    @RequestMapping(value = "/chart", method = RequestMethod.POST)
    @ResponseBody
    public String getData(HttpServletRequest request, @RequestBody NormalUserRequest userInfo) {
        BaseUserSession bus = baseUserService.findBySessionId(request.getHeader("sessionId"));
        ChartResponse dm = new ChartResponse();

        // ??????????????????
        List<AnswerModel> lastSuccs = answer2Service.getLastSucces(bus.getUserId(), weChatConfig.getAnswerNums());
        if (lastSuccs.size() != weChatConfig.getAnswerNums()) {
            dm.setErrorCode(501);
            dm.setErrorMsg("/wechat/chart: ?????????????????????");
            return JSON.toJSONString(dm);
        } else {
            dm.setScore(answer2Service.countScore(lastSuccs));
        }

        // ??????
        dm.setSuggestion("????????????");

        // ??????level
        dm.setLevel(answer2Service.getDPLevel(lastSuccs));

        // ??????????????????
        ChartData[] cd = new ChartData[7];
        for (int i = 0; i < 7; i++) {
            List<AnswerModel> l = answer2Service.getAppointDayData(bus.getUserId(), i);
            cd[i] = new ChartData();
            // ?????????
            cd[i].setDay(DateTimeUtils.getDayOfWeek(i));
            // ????????????
            List<Integer> dataList = new ArrayList<Integer>();
            for (int z = 0; z < l.size(); z += weChatConfig.getAnswerNums()) {

                // ????????? ??? ????????????
                if (l.size() + 1 - z < weChatConfig.getAnswerNums()) {
                    break;
                } else {
                    cd[i].setDay(DateTimeUtils.getDayOfWeek(i));
                    cd[i].setTime(l.get(z).getAnswerTime());
                    dataList.add(answer2Service.countScore(l.subList(z, z + weChatConfig.getAnswerNums())));
                    cd[i].setLevel(answer2Service.getDPLevel(l.subList(z, z + weChatConfig.getAnswerNums())));
                }
            }
            // ?????????
            if (!dataList.isEmpty()) {
                int sum = 0;
                int tmp[] = new int[dataList.size()];
                for (int y = 0; y < dataList.size(); y++) {
                    tmp[y] = dataList.get(y);
                    sum += tmp[y];
                }
                cd[i].setData(tmp);
                cd[i].setTitle(sum / dataList.size());

            }
        }
        dm.setErrorCode(200);
        dm.setErrorMsg("succ");
        dm.setData(cd);
        return JSON.toJSONString(dm);
    }

    @RequestMapping(value = "/upload")
    @ResponseBody
    public String upload(HttpServletRequest request,
                         @RequestParam(value = "file", required = true) MultipartFile file,
                         @RequestParam(value = "openid", required = false) String openId,
                         @RequestParam(value = "session_key", required = true) String sessionKey,
                         @RequestParam(value = "picOrVid", required = true) String picOrVid,
                         @RequestParam(value = "fileName", required = true) String fileName,
                         @RequestParam(value = "question", required = true) int question) throws Exception {
        BaseUserSession bus = baseUserService.findBySessionId(request.getHeader("sessionId"));
        int type = Integer.valueOf(picOrVid);
        DealMessageResponse dm = new DealMessageResponse();


        String fileNowName = null;
        Map<String, Object> param = new HashMap<>();
        String relativePath = null;


        InputStream in = file.getInputStream();
        // ??????????????????
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        if (file.isEmpty()) {
            dm.setErrorCode(504);
            dm.setErrorMsg("??????????????????");
            return JSON.toJSONString(dm);
        }

        try {
            String path1 = "./fufiles/";
            path1 = path1 + "upload";
            //?????????????????????
            Date date = new Date();
            //??????????????????
            String path2 = new SimpleDateFormat("yyyyMMdd").format(date);
            String paths = path1 + path2;

            //???????????????,???????????????
            File f = new File(paths);

            fileName = file.getOriginalFilename(); // ???????????????
            fileNowName = UUIDUtil.getUUID2() + "." + fileName.substring(fileName.lastIndexOf(".") + 1); // ????????????????????? ??????????????????
            File dest = new File(paths + "/" + fileNowName);
            relativePath = paths + "/" + fileNowName;
            dest = new File(dest.getAbsolutePath());

            long lastModified = dest.lastModified();

            if (!f.exists()) {
                f.mkdirs();
                //????????????????????????????????????
                file.transferTo(dest);
            } else {
                file.transferTo(dest);
            }

            WechatFileModel wfm = new WechatFileModel();

            wfm.setUserId(bus.getUserId());
            wfm.setPath(relativePath);
            wfm.setUploadTime(date);
            wfm.setType(type);
            wfm.setNumber(question);
            wechatFile2Service.insert(wfm);

        } catch (Exception e) {
            e.printStackTrace();
            dm.setErrorMsg("????????????");
            dm.setErrorCode(505);
            return JSON.toJSONString(dm);
        }
        dm.setErrorCode(200);
        dm.setErrorMsg("succ");
        return JSON.toJSONString(dm);
    }

    @RequestMapping(value = "/submitscale", method = RequestMethod.POST)
    @ResponseBody
    public String submitData(HttpServletRequest request, @RequestBody SubmitScaleRequest userInfo) {
        BaseUserSession bus = baseUserService.findBySessionId(request.getHeader("sessionId"));
        SubmitScaleResponse dm = new SubmitScaleResponse();

        NCovResultModel nc = new NCovResultModel();
        nc.setAnswerArray(userInfo.getAnswers());
        nc.setUserId(bus.getUserId());

        nc.setAnswerTime(new Date());
        nc.setLevel(ScaleResult2Service.getLevel(userInfo.getQuestionNaire(), ScaleResult2Service.getScore(userInfo.getQuestionNaire(), userInfo.getAnswers())));
        nc.setScore(ScaleResult2Service.getScore(userInfo.getQuestionNaire(), userInfo.getAnswers()));
        nc.setScaleId(userInfo.getQuestionNaire());
        nc.setText(userInfo.getText());
        scaleResult2Service.saveResult(nc);

        dm.setLevel(nc.getLevel());
        dm.setScore(nc.getScore());
        dm.setErrorCode(200);
        dm.setErrorMsg("succ");
        return JSON.toJSONString(dm);
    }

    /**
     * ??????????????????????????????
     *
     * @param request
     * @param obj
     * @return
     */
    @RequestMapping(value = "/deleteScaleResult", method = RequestMethod.POST)
    @ResponseBody
    public String submitData(HttpServletRequest request, @RequestBody JSONObject obj) {
        BaseUserSession bus = baseUserService.findBySessionId(request.getHeader("sessionId"));
        scaleResult2Service.deleteResult(bus.getUserId(), obj.getLong("count"));
        AnswerResponse dm = new AnswerResponse();
        dm.setErrorCode(200);
        dm.setErrorMsg("succ");
        return JSON.toJSONString(dm);
    }

    /*
     * ??????????????????
     */
    @RequestMapping(value = "/getresults", method = RequestMethod.POST)
    @ResponseBody
    public String getResult(HttpServletRequest request, @RequestBody NormalUserRequest userInfo) {
        BaseUserSession bus = baseUserService.findBySessionId(request.getHeader("sessionId"));
        DealMessageResponse dm = new DealMessageResponse();

        GetScaleResultResponse gsr = scaleResult2Service.getRecentScales(bus.getUserId());

        gsr.setErrorCode(200);
        gsr.setErrorMsg("succ");
        return JSON.toJSONString(gsr);
    }

    /*
     * ??????????????????
     */
    @RequestMapping(value = "/getInfo")
    @ResponseBody
    public StudentInfo getInfo(HttpServletRequest request) {
        BaseUserSession bus = baseUserService.findBySessionId(request.getHeader("sessionId"));

        StudentInfo s = this.baseUserService.getStudentInfoByUserID(bus.getUserId());
        if (s == null) {
            return null;
        } else {
            return s;
        }
    }

    /**
     * ????????????100???????????????????????????
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/history")
    @ResponseBody
    public JSONObject getHistoryTime(HttpServletRequest request) {
        System.out.println("history????????????");
        BaseUserSession bus = baseUserService.findBySessionId(request.getHeader("sessionId"));
        List<Map<String, String>> timeList = scaleResult2Service.getHistoryDate(bus.getUserId());
        JSONObject res = new JSONObject();
        if (timeList == null || timeList.isEmpty()) {
            res.put("errorCode", 502);
            res.put("errorMsg", "??????????????????");
        } else {
            res.put("errorCode", 200);
            res.put("errorMsg", "????????????");
            res.put("data", timeList);
        }
        return res;
    }

    /**
     * ???????????????????????????????????????
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/getResultByDate")
    @ResponseBody
    public String getResultByDate(HttpServletRequest request, @RequestBody JSONObject obj) {
        BaseUserSession bus = baseUserService.findBySessionId(request.getHeader("sessionId"));
        String date = obj.getString("date");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = null;
        try {
            now = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // ????????????????????????????????????
        Date afterDate = new Date(now.getTime() + 120000);
        Date beforeDate = new Date(now.getTime() - 5000);

        GetScaleResultResponse gsr = scaleResult2Service.getResultByDate(bus.getUserId(), sdf.format(beforeDate) + ":00", sdf.format(afterDate) + ":00");
        gsr.setErrorCode(200);
        gsr.setErrorMsg("succ");
        return JSON.toJSONString(gsr);
    }


    @PostMapping("/question")
    @ResponseBody
    public String question(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject obj) {
        Cookie[] a = request.getCookies();
        String value = "";
        if (a != null) {
            for (Cookie c : a) {
                if ("session".equals(c.getName())) {
                    value = c.getValue();
                    break;
                }
            }
        }
        System.out.println("cookie????????????" + value);
        System.out.println("question????????????");
        String q = obj.getString("q");
        String pattern = obj.getString("pattern");
        String location = obj.getString("location");
        HttpEntity<String> repFromRemote = this.knowledgeMapService.knowledgeMapAnswer(value, q, pattern, location);
        HttpHeaders headers = repFromRemote.getHeaders();
        List<String> cookies = headers.get("Set-Cookie");
        if (cookies != null) {
            for (String c : cookies) {
                response.addHeader("Set-Cookie", c);

            }
        }
        return repFromRemote.getBody();
    }

    // CCBT ????????????
    @PostMapping(value = "/saveAccessAnswer")
    @ResponseBody
    public String submitData(HttpServletRequest request, @RequestBody CCBTAccessAnswerRequest ccbt) {
        BaseUserSession bus = baseUserService.findBySessionId(request.getHeader("sessionId"));
        // 0: ?????? 1??? ?????? 2??? ?????? 3????????? 4???????????????
        int type = ccbt.getType();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String date = formatter.format(ccbt.getDate());
        CCBTAccessAnswerModel model = ccbtAccessAnswerService.getAnswerByUserIdAndDate(bus.getUserId(), date);


        if (model == null) {
            // ????????????
            model = new CCBTAccessAnswerModel();
            model.setCreateTime(ccbt.getDate());
            model.setUserId(bus.getUserId());
        }
        if (type == 0) {
            model.setDepressAnswer(StringUtils.join(ccbt.getAnswer(), ','));
            model.setDepressScore(ccbt.getScore());
        } else if (type == 1) {
            model.setAnxiousAnswer(StringUtils.join(ccbt.getAnswer(), ','));
            model.setAnxiousScore(ccbt.getScore());
        } else if (type == 2) {
            model.setSleepAnswer(StringUtils.join(ccbt.getAnswer(), ','));
            model.setSleepScore(ccbt.getScore());
        } else if (type == 3) {
            model.setCognitionAnswer(StringUtils.join(ccbt.getCognitionAnswer(), ','));
        } else if (type == 4) {
            model.setRelationshipAnswer(StringUtils.join(ccbt.getAnswer(), ','));
        }
        model.setStatus(type);
        ccbtAccessAnswerService.saveAccessAnswer(model);

        DealMessageResponse dm = new DealMessageResponse();
        dm.setErrorCode(200);
        dm.setErrorMsg("succ");
        return JSON.toJSONString(dm);
    }

    @PostMapping(value = "/getLastestHistory")
    @ResponseBody
    public JSONObject getLastestHistory(HttpServletRequest request, @RequestBody(required=false) JSONObject param) {
        BaseUserSession bus = baseUserService.findBySessionId(request.getHeader("sessionId"));
        int num = 0;
        if (param == null || param.getInteger("num") == null) {
            num = 5;
        } else {
            num = param.getInteger("num");
        }
        List<Map<String, Object>> model = ccbtAccessAnswerService.getLastestHistory(bus.getUserId(), num);
        JSONObject res = new JSONObject();
        if (model == null || model.size() == 0) {
            res.put("errorCode", 502);
            res.put("errorMsg", "??????????????????");
        } else {
            res.put("errorCode", 200);
            res.put("errorMsg", "????????????");
            res.put("data", model);
        }
        return res;
    }

    // CCBT ??????/?????? ????????????????????????????????????????????????
    @PostMapping(value = "/ccbt/data")
    @ResponseBody
    public JSONObject ccbtData(HttpServletRequest request, @RequestBody JSONObject param) {

        Integer status = param.getInteger("status");
        BaseUserSession session = baseUserService.findBySessionId(request.getHeader("sessionId"));

        JSONObject res;

        if (status == 0) {
            // 0???????????????
            res = wechatCCBTService.saveCCBTData(param, session);
        } else {
            // 1???????????????
            res = wechatCCBTService.getCCBTData(param, session);
        }

        return res;
    }

    // CCBT ??????/?????? ?????????????????????????????????
    @PostMapping(value = "/ccbt/progress")
    @ResponseBody
    public JSONObject ccbtProgess(HttpServletRequest request, @RequestBody JSONObject param) {

        Integer status = param.getInteger("status");
        BaseUserSession session = baseUserService.findBySessionId(request.getHeader("sessionId"));

        JSONObject res;

        if (status == 0) {
            // 0???????????????
            res = wechatCCBTService.saveCCBTProgess(param, session);
        } else {
            // 1???????????????
            res = wechatCCBTService.getCCBTProgess(param, session);
        }

        return res;
    }
}
