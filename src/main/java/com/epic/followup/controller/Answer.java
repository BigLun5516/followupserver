package com.epic.followup.controller;

import com.alibaba.fastjson.JSON;
import com.epic.followup.conf.WeChatConfig;
import com.epic.followup.model.AnswerModel;
import com.epic.followup.model.ResultModel;
import com.epic.followup.model.WechatFileModel;
import com.epic.followup.service.*;
import com.epic.followup.temporary.AnsewerSubmitRequest;
import com.epic.followup.temporary.AnswerQueryRequest;
import com.epic.followup.temporary.AnswerResponse;
import com.epic.followup.temporary.DealMessageResponse;
import org.apache.xmlrpc.XmlRpcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author : zx
 * @version V1.0
 */

@Controller
@RequestMapping("/answer")
public class Answer {

    @Autowired
    private NLPService nlpService;
    @Autowired
    private WechatUserService wechatUserService;
    @Autowired
    private AnswerService answerService;
    @Autowired
    private WeChatConfig weChatConfig;
    @Autowired
    private WechatFileService wechatFileService;
    @Autowired
    private ResultService resultService;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public int rpcServerTest(){
        int i = nlpService.fun_add();
        System.out.println(i);
        return i;
    }

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public String getLoginStatus(@RequestBody AnsewerSubmitRequest answerInfo){

        AnswerResponse dm = new AnswerResponse();

        // 校验用户身份合法性
        if (!wechatUserService.checkUser(answerInfo.getOpenid(), answerInfo.getSession_key())){
            dm.setErrorCode(500);
            dm.setErrorMsg("用户登录状态异常");
            return JSON.toJSONString(dm);
        }

        // 判断上传合法性（1.是否为同一天小于5分钟且问题序号为上一条加一 2.是否为当天最近数据）
        AnswerModel lastAnswer = answerService.getLastAnswer(answerInfo.getOpenid());
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
        AnswerModel submitAnswer = AnswerService.transformAnswerRequest(answerInfo);
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
        if (answerService.insert(submitAnswer)){
            dm.setErrorCode(200);
            dm.setErrorMsg("succ");
            return JSON.toJSONString(dm);
        }else {
            dm.setErrorCode(505);
            dm.setErrorMsg("伺服器错误");
            return JSON.toJSONString(dm);
        }
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public String getLoginStatus(@RequestBody AnswerQueryRequest answerInfo) throws Exception{

        AnswerResponse dm = new AnswerResponse();

        // 校验用户身份合法性
        if (!wechatUserService.checkUser(answerInfo.getOpenid(), answerInfo.getSession_key())){
            dm.setErrorCode(500);
            dm.setErrorMsg("用户登录状态异常");
            return JSON.toJSONString(dm);
        }

        //  checkOrEnd : 1 查询 ；2 测评失败删除0； 3 测评成功设置1；
        if (answerInfo.getCheckOrEnd() == 1){
            AnswerModel lastAnswer = answerService.getLastAnswer(answerInfo.getOpenid());

            if (lastAnswer == null){
                dm.setErrorCode(200);
                dm.setErrorMsg("无数据");
                dm.setNextQuestion(1);
                return JSON.toJSONString(dm);
            }
            Date nowDate=new Date();
            if (nowDate.getTime() - lastAnswer.getAnswerTime().getTime() > (weChatConfig.getDelay()*60*1000)){
                // 超时直接删除
                answerService.deleteOutData(answerInfo.getOpenid());
                wechatFileService.deleteOutData(answerInfo.getOpenid());
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
            answerService.deleteOutData(answerInfo.getOpenid());
            wechatFileService.deleteOutData(answerInfo.getOpenid());
            dm.setErrorCode(200);
            dm.setErrorMsg("succ，已删除");
            dm.setNextQuestion(1);
            return JSON.toJSONString(dm);
        }else if (answerInfo.getCheckOrEnd() == 3){
            // 添加result
            List<AnswerModel> al = answerService.getAppointDayUnsuccData(answerInfo.getOpenid(), 0);
            List<WechatFileModel> fl = wechatFileService.getAppointDayUnsuccData(answerInfo.getOpenid(), 0);
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
            rm.setLevel(answerService.getDPLevel(al));
            rm.setOpenId(answerInfo.getOpenid());
            rm.setScore(answerService.countScore(al));
            rm.setAnswerTime(al.get(0).getAnswerTime());
            resultService.saveResult(rm);

            answerService.updateSucc(answerInfo.getOpenid());
            wechatFileService.updateSucc(answerInfo.getOpenid());
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
}
