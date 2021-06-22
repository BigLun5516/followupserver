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
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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
    WechatCCBTService wechatCCBTService; // 小程序CCBT

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
                               ){
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
    public String getLoginStatus(@RequestParam(value = "code", required = true) String code){

        Map<String, String> params = new HashMap<String, String>();
        params.put("secret", weChatConfig.getAppSecert());
        params.put("appid", weChatConfig.getAppID());
        params.put("js_code", code);
        params.put("grant_type", weChatConfig.getGrantType());

        String result = null;
        try {
            result = get(weChatConfig.getUrl(), params, null);
        }catch (Exception e){
            e.printStackTrace();
        }

        JSONObject r = JSON.parseObject(result);
        if (r.getString("openid") != null){

            WechatAppUserModel u = wechatAppUserService.findByOpenId(r.getString("openid"));
            if (u!=null){
                u.setSessionKey(r.getString("session_key"));
                u.setOpenId(r.getString("openid"));
            }else {
                u = new WechatAppUserModel();
                u.setSessionKey(r.getString("session_key"));
                u.setOpenId(r.getString("openid"));
                u.setCreateTime(new Date());
            }

            wechatAppUserService.updateUser(u);

            r.put("errorCode", 200);
            r.put("sessionId" ,u.getSessionId() == null ? "-" : u.getSessionId());
            return JSON.toJSONString(r);
        }else {
            return result;
        }
    }

    @RequestMapping(value = "/answer/test", method = RequestMethod.GET)
    @ResponseBody
    public int rpcServerTest(HttpServletRequest request){
//        BaseUserSession bus = baseUserService.findBySessionId(request.getHeader("sessionId"));
        int i = nlpService.fun_add();
        System.out.println(i);
        return i;
    }

    @RequestMapping(value = "/answer/submit", method = RequestMethod.POST)
    @ResponseBody
    public String getLoginStatus(HttpServletRequest request, @RequestBody AnsewerSubmitRequest answerInfo){
        BaseUserSession bus = baseUserService.findBySessionId(request.getHeader("sessionId"));
        AnswerResponse dm = new AnswerResponse();

        // 判断上传合法性（1.是否为同一天小于5分钟且问题序号为上一条加一 2.是否为当天最近数据）
        AnswerModel lastAnswer = answer2Service.getLastAnswer(bus.getUserId());
        if (lastAnswer != null ){
            // 判断是否为今天数据
            Date nowDate = new Date();
            if ((int)((nowDate.getTime() - lastAnswer.getAnswerTime().getTime())/(24*60*60*1000)) == 0){
                // 今天有数据，判断是否为下一题
                if (answerInfo.getQuestion() - lastAnswer.getNumber() != 1){
                    dm.setErrorCode(504);
                    dm.setErrorMsg("题目序号错误，下一题应为["+(lastAnswer.getNumber()+1)+"].");
                    dm.setNextQuestion(lastAnswer.getNumber()+1);
                    return JSON.toJSONString(dm);
                }
                // 判断是否超时
                if (nowDate.getTime() - lastAnswer.getAnswerTime().getTime() > (weChatConfig.getDelay()*60*1000)){
                    dm.setErrorCode(504);
                    dm.setErrorMsg("回答超时.");
                    return JSON.toJSONString(dm);
                }
            }
            // 今天无数据，直接插入
        }

        // 通过校验可以插入
        AnswerModel submitAnswer = Answer2Service.transformAnswerRequest(answerInfo, bus.getUserId());
        if (answerInfo.getResult() == -1){
            double result;
            try {
                result = nlpService.questionFunSelect(submitAnswer.getNumber(), submitAnswer.getAnswerResult());
            }catch (XmlRpcException e){
                e.printStackTrace();
                dm.setErrorCode(505);
                dm.setErrorMsg("伺服器错误,RPC服务异常");
                return JSON.toJSONString(dm);
            }

            submitAnswer.setAnalyseResult(result);
        }else {
            // 大于等于2 视为有症状

//            submitAnswer.setAnalyseResult(answerInfo.getResult() >= 2 ? 1 : 0);
            submitAnswer.setAnalyseResult(answerInfo.getResult() >= 0 ? answerInfo.getResult() : 0);
        }

        if (answer2Service.insert(submitAnswer)){
            dm.setErrorCode(200);
            dm.setErrorMsg("succ");
            return JSON.toJSONString(dm);
        }else {
            dm.setErrorCode(505);
            dm.setErrorMsg("伺服器错误");
            return JSON.toJSONString(dm);
        }
    }

    @RequestMapping(value = "/answer/query", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public String getLoginStatus(HttpServletRequest request, @RequestBody AnswerQueryRequest answerInfo) throws Exception{
        BaseUserSession bus = baseUserService.findBySessionId(request.getHeader("sessionId"));
        AnswerResponse dm = new AnswerResponse();

        //  checkOrEnd : 1 查询 ；2 测评失败删除0； 3 测评成功设置1；
        if (answerInfo.getCheckOrEnd() == 1){
            AnswerModel lastAnswer = answer2Service.getLastAnswer(bus.getUserId());

            if (lastAnswer == null){
                dm.setErrorCode(200);
                dm.setErrorMsg("无数据");
                dm.setNextQuestion(1);
                return JSON.toJSONString(dm);
            }
            Date nowDate=new Date();
            if (nowDate.getTime() - lastAnswer.getAnswerTime().getTime() > (weChatConfig.getDelay()*60*1000)){
                // 超时直接删除
                answer2Service.deleteOutData(bus.getUserId());
                wechatFile2Service.deleteOutData(bus.getUserId());
                dm.setErrorCode(504);
                dm.setErrorMsg("回答超时.");
                dm.setNextQuestion(-1);
                return JSON.toJSONString(dm);
            }
            dm.setErrorCode(200);
            dm.setErrorMsg("succ");
            dm.setNextQuestion(lastAnswer.getNumber() + 1);
            return JSON.toJSONString(dm);
        }else if (answerInfo.getCheckOrEnd() == 2){
            answer2Service.deleteOutData(bus.getUserId());
            wechatFile2Service.deleteOutData(bus.getUserId());
            dm.setErrorCode(200);
            dm.setErrorMsg("succ，已删除");
            dm.setNextQuestion(1);
            return JSON.toJSONString(dm);
        }else if (answerInfo.getCheckOrEnd() == 3){
            // 添加result
            List<AnswerModel> al = answer2Service.getAppointDayUnsuccData(bus.getUserId(), 0);
            List<WechatFileModel> fl = wechatFile2Service.getAppointDayUnsuccData(bus.getUserId(), 0);
            long[] aa = new long[al.size()];
            long[] fa = new long[fl.size()];
            for (int i = 0; i < al.size(); i++){
                aa[i] = al.get(i).getId();
            }
            for (int i = 0; i < fl.size(); i++){
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
             * 添加student result
             */
            StudentInfo stinfo = this.baseUserService.getStudentInfoByUserID(bus.getUserId());

            /* 计算sym 依次为 从1到20题
  `            * 症状
                抑郁/不开心 时长 兴趣
                兴趣缺失 注意力不能集中 犹豫 选择困难 异常表现
                无价值感 自残倾向 自杀倾向 自杀计划 睡眠时长 睡眠质量
                睡眠障碍 食欲异常 动作缓慢、容易烦躁 时而躁狂 社会关系、不安全感 焦虑、抑郁
            */
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < al.size(); i++){
//                log.info(Integer.toString((int)al.get(i).getAnalyseResult()));
                sb.append(Integer.toString((int)al.get(i).getAnalyseResult()));
            }
            this.studentResultService.addResult(bus.getUserId(), stinfo, sb.toString(), rm.getScore(), rm.getLevel());


            answer2Service.updateSucc(bus.getUserId());
            wechatFile2Service.updateSucc(bus.getUserId());
            dm.setErrorCode(200);
            dm.setErrorMsg("succ,已添加");
            dm.setNextQuestion(0);
            return JSON.toJSONString(dm);
        }else {
            dm.setErrorCode(503);
            dm.setErrorMsg("错误参数.");
            return JSON.toJSONString(dm);
        }
    }

    @RequestMapping(value = "/chart", method = RequestMethod.POST)
    @ResponseBody
    public String getData(HttpServletRequest request, @RequestBody NormalUserRequest userInfo){
        BaseUserSession bus = baseUserService.findBySessionId(request.getHeader("sessionId"));
        ChartResponse dm = new ChartResponse();

        // 计算最近得分
        List<AnswerModel> lastSuccs = answer2Service.getLastSucces(bus.getUserId(), weChatConfig.getAnswerNums());
        if (lastSuccs.size() != weChatConfig.getAnswerNums()){
            dm.setErrorCode(501);
            dm.setErrorMsg("/wechat/chart: 无最近测评数据");
            return JSON.toJSONString(dm);
        }else {
            dm.setScore(answer2Service.countScore(lastSuccs));
        }

        // 建议
        dm.setSuggestion("吃好喝好");

        // 计算level
        dm.setLevel(answer2Service.getDPLevel(lastSuccs));

        // 计算每日得分
        ChartData[] cd = new ChartData[7];
        for (int i = 0; i < 7; i++){
            List<AnswerModel> l = answer2Service.getAppointDayData(bus.getUserId(), i);
            cd[i] = new ChartData();
            // 星期几
            cd[i].setDay(DateTimeUtils.getDayOfWeek(i));
            // 缓存数据
            List<Integer> dataList = new ArrayList<Integer>();
            for (int z = 0; z < l.size(); z+=weChatConfig.getAnswerNums()){

                // 无数据 或 数据不足
                if (l.size()+1 - z < weChatConfig.getAnswerNums()){
                    break;
                }else {
                    cd[i].setDay(DateTimeUtils.getDayOfWeek(i));
                    cd[i].setTime(l.get(z).getAnswerTime());
                    dataList.add(answer2Service.countScore(l.subList(z, z+weChatConfig.getAnswerNums())));
                    cd[i].setLevel(answer2Service.getDPLevel(l.subList(z, z+weChatConfig.getAnswerNums())));
                }
            }
            // 取均值
            if (!dataList.isEmpty()){
                int sum = 0;
                int tmp[] = new int[dataList.size()];
                for (int y = 0; y < dataList.size(); y ++){
                    tmp[y] = dataList.get(y);
                    sum += tmp[y];
                }
                cd[i].setData(tmp);
                cd[i].setTitle(sum/dataList.size());

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
        // 获取系统时间
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        if (file.isEmpty()) {
            dm.setErrorCode(504);
            dm.setErrorMsg("文件不能为空");
            return JSON.toJSONString(dm);
        }

        try {
            String path1 = "./fufiles/";
            path1 = path1 + "upload";
            //创建一个文件夹
            Date date = new Date();
            //文件夹的名称
            String path2 = new SimpleDateFormat("yyyyMMdd").format(date);
            String paths = path1 + path2;

            //如果不存在,创建文件夹
            File f = new File(paths);

            fileName = file.getOriginalFilename(); // 获取原名称
            fileNowName = UUIDUtil.getUUID2() + "." + fileName.substring(fileName.lastIndexOf(".") + 1); // 生成唯一的名字 没有需要的包
            File dest = new File(paths + "/" + fileNowName);
            relativePath = paths + "/" + fileNowName;
            dest=new File(dest.getAbsolutePath());

            long lastModified=dest.lastModified();

            if (!f.exists()) {
                f.mkdirs();
                //如果文件夹不存在创建一个
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
            dm.setErrorMsg("上传失败");
            dm.setErrorCode(505);
            return JSON.toJSONString(dm);
        }
        dm.setErrorCode(200);
        dm.setErrorMsg("succ");
        return JSON.toJSONString(dm);
    }

    @RequestMapping(value = "/submitscale", method = RequestMethod.POST)
    @ResponseBody
    public String submitData(HttpServletRequest request, @RequestBody SubmitScaleRequest userInfo){
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
     * 删除未做完的测评结果
     * @param request
     * @param obj
     * @return
     */
    @RequestMapping(value = "/deleteScaleResult", method = RequestMethod.POST)
    @ResponseBody
    public String submitData(HttpServletRequest request, @RequestBody JSONObject obj){
        BaseUserSession bus = baseUserService.findBySessionId(request.getHeader("sessionId"));
        scaleResult2Service.deleteResult(bus.getUserId(), obj.getLong("count"));
        AnswerResponse dm = new AnswerResponse();
        dm.setErrorCode(200);
        dm.setErrorMsg("succ");
        return JSON.toJSONString(dm);
    }

    /*
     * 获取评测结果
     */
    @RequestMapping(value = "/getresults", method = RequestMethod.POST)
    @ResponseBody
    public String getResult(HttpServletRequest request, @RequestBody NormalUserRequest userInfo){
        BaseUserSession bus = baseUserService.findBySessionId(request.getHeader("sessionId"));
        DealMessageResponse dm = new DealMessageResponse();

        GetScaleResultResponse gsr = scaleResult2Service.getRecentScales(bus.getUserId());

        gsr.setErrorCode(200);
        gsr.setErrorMsg("succ");
        return JSON.toJSONString(gsr);
    }

    /*
     * 获取评测结果
     */
    @RequestMapping(value = "/getInfo")
    @ResponseBody
    public StudentInfo getInfo(HttpServletRequest request){
        BaseUserSession bus = baseUserService.findBySessionId(request.getHeader("sessionId"));

        StudentInfo s = this.baseUserService.getStudentInfoByUserID(bus.getUserId());
        if (s == null){
            return null;
        }else {
            return s;
        }
    }

    /**
     * 查询最近100天的历史记录的日期
     * @param request
     * @return
     */
    @PostMapping(value = "/history")
    @ResponseBody
    public JSONObject getHistoryTime(HttpServletRequest request){
        System.out.println("history接受到了");
        BaseUserSession bus = baseUserService.findBySessionId(request.getHeader("sessionId"));
        List<Map<String, String>> timeList = scaleResult2Service.getHistoryDate(bus.getUserId());
        JSONObject res = new JSONObject();
        if (timeList == null || timeList.isEmpty()) {
            res.put("errorCode",502);
            res.put("errorMsg","未查找到信息");
        }else{
            res.put("errorCode",200);
            res.put("errorMsg","查找成功");
            res.put("data",timeList);
        }
        return res;
    }

    /**
     * 根据日期查询当天的评测结果
     * @param request
     * @return
     */
    @PostMapping(value = "/getResultByDate")
    @ResponseBody
    public String getResultByDate(HttpServletRequest request, @RequestBody JSONObject obj){
        BaseUserSession bus = baseUserService.findBySessionId(request.getHeader("sessionId"));
        String date = obj.getString("date");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = null;
        try {
            now = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 取前后两分钟范围内的记录
        Date afterDate = new Date(now .getTime() + 120000);
        Date beforeDate = new Date(now.getTime() - 5000);

        GetScaleResultResponse gsr = scaleResult2Service.getResultByDate(bus.getUserId(), sdf.format(beforeDate)+":00", sdf.format(afterDate)+":00");
        gsr.setErrorCode(200);
        gsr.setErrorMsg("succ");
        return JSON.toJSONString(gsr);
    }


    @PostMapping("/question")
    @ResponseBody
    public String question(HttpServletRequest request, @RequestBody JSONObject obj){
        System.out.println("question接受到了");
        String q=obj.getString("q");
        String pattern=obj.getString("pattern");
        String location=obj.getString("location");
        return this.knowledgeMapService.knowledgeMapAnswer(request.getHeader("sessionId"), q,pattern,location);
    }

    // CCBT 评估模块
    @PostMapping(value = "/saveAccessAnswer")
    @ResponseBody
    public String submitData(HttpServletRequest request, @RequestBody CCBTAccessAnswerRequest ccbt){
        BaseUserSession bus = baseUserService.findBySessionId(request.getHeader("sessionId"));
        // 0: 抑郁 1： 焦虑 2： 失眠 3：认知 4：人际关系
        int type = ccbt.getType();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String date = formatter.format(ccbt.getDate());
        CCBTAccessAnswerModel model = ccbtAccessAnswerService.getAnswerByUserIdAndDate(bus.getUserId(), date);

        
        if(model == null){
            // 创建记录
            model = new CCBTAccessAnswerModel();
            model.setCreateTime(ccbt.getDate());
            model.setUserId(bus.getUserId());
        }
        if(type == 0){
            model.setDepressAnswer(StringUtils.join(ccbt.getAnswer(), ','));
            model.setDepressScore(ccbt.getScore());
        }else if(type == 1){
            model.setAnxiousAnswer(StringUtils.join(ccbt.getAnswer(), ','));
            model.setAnxiousScore(ccbt.getScore());
        }else if(type == 2){
            model.setSleepAnswer(StringUtils.join(ccbt.getAnswer(),','));
            model.setSleepScore(ccbt.getScore());
        }else if(type == 3){
            model.setCognitionAnswer(StringUtils.join(ccbt.getCognitionAnswer(),','));
        }else if(type == 4){
            model.setRelationshipAnswer(StringUtils.join(ccbt.getAnswer(),','));
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
    public JSONObject getLastestHistory(HttpServletRequest request){
        BaseUserSession bus = baseUserService.findBySessionId(request.getHeader("sessionId"));
        CCBTAccessAnswerModel model = ccbtAccessAnswerService.getLastestHistory(bus.getUserId());
        JSONObject res = new JSONObject();
        if (model == null) {
            res.put("errorCode",502);
            res.put("errorMsg","未查找到信息");
        }else{
            res.put("errorCode",200);
            res.put("errorMsg","查找成功");
            res.put("data",model);
        }
        return res;
    }

    // CCBT 查询/提交 数据统一接口（数据是用户的回答）
    @PostMapping(value = "/ccbt/data")
    @ResponseBody
    public JSONObject ccbtData(HttpServletRequest request, @RequestBody JSONObject param) {

        Integer status = param.getInteger("status");
        BaseUserSession session = baseUserService.findBySessionId(request.getHeader("sessionId"));

        JSONObject res;

        if (status == 0) {
            // 0为存储数据
            res = wechatCCBTService.saveCCBTData(param, session);
        } else {
            // 1为获取数据
            res = wechatCCBTService.getCCBTData(param, session);
        }

        return res;
    }

    // CCBT 查询/提交 用户目前进行的模块标号
    @PostMapping(value = "/ccbt/progress")
    @ResponseBody
    public JSONObject ccbtProgess(HttpServletRequest request, @RequestBody JSONObject param) {

        Integer status = param.getInteger("status");
        BaseUserSession session = baseUserService.findBySessionId(request.getHeader("sessionId"));

        JSONObject res;

        if (status == 0) {
            // 0为存储数据
            res = wechatCCBTService.saveCCBTProgess(param, session);
        } else {
            // 1为获取数据
            res = wechatCCBTService.getCCBTProgess(param, session);
        }

        return res;
    }
}
