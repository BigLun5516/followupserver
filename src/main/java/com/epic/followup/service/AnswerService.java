package com.epic.followup.service;

import com.epic.followup.model.AnswerModel;
import com.epic.followup.temporary.AnsewerSubmitRequest;

import java.util.Date;
import java.util.List;

/**
 * @author : zx
 * @version V1.0
 */

public interface AnswerService {

    AnswerModel getLastAnswer(String openid);
    boolean insert(AnswerModel answer);
    void deleteOutData(String openId);
    void updateSucc(String openid);

    // 获取n天前的所有数据
    List<AnswerModel> getAppointDayData(String openId, int day);

    // 获取n天前未提交数据
    // 获取未提交数据
    List<AnswerModel> getAppointDayUnsuccData(String openId, int day);

    // 最近一次成功的测评
    List<AnswerModel> getLastSucces(String openId, int answerNum);

    // 获取抑郁程度
    String getDPLevel(List<AnswerModel> l);

    // 获取得分
    int countScore(List<AnswerModel> l);


    public static AnswerModel transformAnswerRequest(AnsewerSubmitRequest r){
        AnswerModel a = new AnswerModel();
        a.setOpenId(r.getOpenid());
        a.setAnswerResult(r.getAnswer());
        a.setAnswerTime(new Date());
        a.setNumber(r.getQuestion());
        return a;
    }
}
