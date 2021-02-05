package com.epic.followup.temporary.ncov;

import com.alibaba.fastjson.JSONArray;
import com.epic.followup.temporary.DealMessageResponse;

/**
 * @author : zx
 * @version V1.0
 */
public class GetScaleResponse extends DealMessageResponse {
    private int questionNum;
    private JSONArray question;

    public int getQuestionNum() {
        return questionNum;
    }

    public void setQuestionNum(int questionNum) {
        this.questionNum = questionNum;
    }

    public JSONArray getQuestion() {
        return question;
    }

    public void setQuestion(JSONArray question) {
        this.question = question;
    }
}
