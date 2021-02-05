package com.epic.followup.service.followup2.student.impl;

import com.epic.followup.conf.WeChatConfig;
import com.epic.followup.model.followup2.student.AnswerModel;
import com.epic.followup.repository.followup2.student.Answer2Repository;
import com.epic.followup.service.followup2.student.Answer2Service;
import com.epic.followup.util.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author : zx
 * @version V1.0
 */

@Service
public class Answer2SericeImpl implements Answer2Service {

    @Autowired
    Answer2Repository answer2Repository;
    @Autowired
    private WeChatConfig weChatConfig;

    public static final String[] LEVEL = new String[]{"无抑郁倾向" , "轻度抑郁", "中度抑郁", "重度抑郁"};

    @Override
    public AnswerModel getLastAnswer(Long openid) {
        List<AnswerModel> l;
        Date today = new Date();
        l = answer2Repository.findLastAnswerByOpenid(openid, DateTimeUtils.getStartOfDay(today),
                DateTimeUtils.getEndOfDay(today));
        if (l.isEmpty()){
            return null;
        }else {
            return l.get(0);
        }
    }

    @Override
    public boolean insert(AnswerModel answer) {
        return (answer.getNumber() == answer2Repository.save(answer).getNumber());
    }

    @Override
    public void deleteOutData(Long openId) {
        Date today = new Date();
        answer2Repository.deleteByDateAndOpenID(openId, DateTimeUtils.getStartOfDay(today),
                DateTimeUtils.getEndOfDay(today));
    }

    @Override
    public void updateSucc(Long openId) {
        Date today = new Date();
        answer2Repository.updateSucc(openId, DateTimeUtils.getStartOfDay(today),
                DateTimeUtils.getEndOfDay(today));
    }

    @Override
    public List<AnswerModel> getAppointDayData(Long openId, int day) {

        Date dNow = new Date();
        Date dBefore = new Date();

        Calendar calendar = Calendar.getInstance();

        //把当前时间赋给日历
        calendar.setTime(dNow);
        //设置为前一天
        calendar.add(Calendar.DAY_OF_MONTH, -day);
        // 得到前一天的时间
        dBefore = calendar.getTime();

        return answer2Repository.getAnswerByOpenIdAndDate(openId, DateTimeUtils.getStartOfDay(dBefore),
                DateTimeUtils.getEndOfDay(dBefore));
    }

    @Override
    public List<AnswerModel> getAppointDayUnsuccData(Long openId, int day) {
        Date dNow = new Date();
        Date dBefore = new Date();

        Calendar calendar = Calendar.getInstance();

        //把当前时间赋给日历
        calendar.setTime(dNow);
        //设置为前一天
        calendar.add(Calendar.DAY_OF_MONTH, -day);
        // 得到前一天的时间
        dBefore = calendar.getTime();

        return answer2Repository.findUnsuccAnswersByDateAndOpenID(openId, DateTimeUtils.getStartOfDay(dBefore),
                DateTimeUtils.getEndOfDay(dBefore));
    }

    @Override
    public List<AnswerModel> getLastSucces(Long openId, int answerNum) {
        return answer2Repository.getLastSuccByOpenId(openId, answerNum);
    }

    // 计算得分 v2.0
    public int countScore(List<AnswerModel> l){
//        double sum = 0;
//        for (AnswerModel a : l){
//            if (a.getAnalyseResult() > 0){
//                sum += 1;
//            }
//        }
//        return (int)(sum/(weChatConfig.getMaxScore()*weChatConfig.getAnswerNums())*100);

        l.sort((o1, o2) -> o1.getNumber() - o2.getNumber());
        int[] score = new int[weChatConfig.getAnswerNums()];
        for (int i = 0; i < score.length; i++){
            score[i] = (l.get(i).getAnalyseResult() > 0 ? 1 : 0);
        }

        float keyques_1 = score[0] > 0 && score[1] > 0 ? 1 : 0;
        float keyques_2 = score[2] > 0 || score[3] > 0 ? 1 : 0;
        float mainques_1 = score[5] > 0 && score[6] > 0 ? 1 : 0;
        float mainques_2 = score[12] > 0 && score[13] > 0 && score[14] > 0 ? 1 : 0;
        float score_key = keyques_1 + keyques_2 + score[4];
        float score_main = sumIntArray(score, 7, 12) + sumIntArray(score, 15, 17) + mainques_1 + mainques_2;
        if(score_key >= 3 && score_main >= 5){
            return (int) ((score_key * 18 + score_main*5)/99*100);
        }else if(score_key >= 2 && score_main >= 4){
            return (int) ((score_key * 18 + score_main*5)/99*100);
        }else if(score_key >= 2 && score_main >= 2){
            return (int) ((score_key * 18 + score_main*5)/99*100);
        }else {
            return (int) ((score_key * 8 + score_main*5)/99*100);
        }
    }

    // 计算抑郁程度
    /*
     * 根据各项问题得分计算总分和抑郁程度
     * :param score: 各项问题得分 1*20
     * :return: score，level （得分，抑郁程度）
     * 0 - 无  1 - 轻  2 - 中 3 - 重
     */
    public String getDPLevel(List<AnswerModel> l){
        l.sort((o1, o2) -> o1.getNumber() - o2.getNumber());
        int[] score = new int[weChatConfig.getAnswerNums()];
        for (int i = 0; i < score.length; i++){
            score[i] = (l.get(i).getAnalyseResult() > 0 ? 1 : 0);
        }

        int keyques_1 = score[0] > 0 && score[1] > 0 ? 1 : 0;
        int keyques_2 = score[2] > 0 || score[3] > 0 ? 1 : 0;
        int mainques_1 = score[5] > 0 && score[6] > 0 ? 1 : 0;
        int mainques_2 = score[12] > 0 && score[13] > 0 && score[14] > 0 ? 1 : 0;
        int score_key = keyques_1 + keyques_2 + score[4];
        int score_main = sumIntArray(score, 7, 12) + sumIntArray(score, 15, 17) + mainques_1 + mainques_2;
        if(score_key >= 3 && score_main >= 5){
            return LEVEL[3];
        }else if(score_key >= 2 && score_main >= 4){
            return LEVEL[2];
        }else if(score_key >= 2 && score_main >= 2){
            return LEVEL[1];
        }else {
            return LEVEL[0];
        }
    }

    public static int convertLevel(String s){
        if (s.equals(LEVEL[0])){
            return 0;
        }else if (s.equals(LEVEL[1])){
            return 1;
        }else if (s.equals(LEVEL[2])){
            return 2;
        }else if (s.equals(LEVEL[3])){
            return 3;
        }else {
            return -1;
        }
    }

    public static int sumIntArray(int[]array, int start, int end){
        int sum = 0;
        for (int i = start; i < end; i++){
            sum += array[i];
        }
        return sum;
    }

}
