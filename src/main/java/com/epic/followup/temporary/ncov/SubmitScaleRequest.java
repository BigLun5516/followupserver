package com.epic.followup.temporary.ncov;

import com.epic.followup.temporary.NormalUserRequest;

import java.util.Map;

/**
 * @author : zx
 * @version V1.0
 */
public class SubmitScaleRequest extends NormalUserRequest {
    // 问卷编号
    private int questionNaire;

    private int[] answers;

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getQuestionNaire() {
        return questionNaire;
    }

    public int[] getAnswers() {
        return answers;
    }

    public void setAnswers(int[] answers) {
        this.answers = answers;
    }

    public void setQuestionNaire(int questionNaire) {
        this.questionNaire = questionNaire;
    }
}
