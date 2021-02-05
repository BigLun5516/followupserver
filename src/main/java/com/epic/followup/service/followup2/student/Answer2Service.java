package com.epic.followup.service.followup2.student;

import com.epic.followup.model.followup2.student.AnswerModel;
import com.epic.followup.temporary.AnsewerSubmitRequest;

import java.util.Date;
import java.util.List;

/**
 * @author : zx
 * @version V1.0
 */

public interface Answer2Service {

    AnswerModel getLastAnswer(Long userId);
    boolean insert(AnswerModel answer);
    void deleteOutData(Long userId);
    void updateSucc(Long userId);

    // 获取n天前的所有数据
    List<AnswerModel> getAppointDayData(Long userId, int day);

    // 获取n天前未提交数据
    // 获取未提交数据
    List<AnswerModel> getAppointDayUnsuccData(Long userId, int day);

    // 最近一次成功的测评
    List<AnswerModel> getLastSucces(Long userId, int answerNum);

    // 获取抑郁程度
    String getDPLevel(List<AnswerModel> l);

    // 获取得分
    int countScore(List<AnswerModel> l);


    public static AnswerModel transformAnswerRequest(AnsewerSubmitRequest r, Long userId){
        AnswerModel a = new AnswerModel();
        a.setUserId(userId);
        a.setAnswerResult(r.getAnswer());
        a.setAnswerTime(new Date());
        a.setNumber(r.getQuestion());
        return a;
    }
}
