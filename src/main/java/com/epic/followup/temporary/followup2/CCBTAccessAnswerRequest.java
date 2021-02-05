package com.epic.followup.temporary.followup2;

import java.util.Date;

public class CCBTAccessAnswerRequest {

    // 0: 抑郁 1： 焦虑 2： 失眠 3：认知 4：人际关系
    public int type;
    public int[] answer;
    public Date date;
    public int score;
    public String[] cognitionAnswer;

    public String[] getCognitionAnswer() {
        return cognitionAnswer;
    }

    public void setCognitionAnswer(String[] cognitionAnswer) {
        this.cognitionAnswer = cognitionAnswer;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int[] getAnswer() {
        return answer;
    }

    public void setAnswer(int[] answer) {
        this.answer = answer;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
