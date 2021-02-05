package com.epic.followup.temporary;

/**
 * @author : zx
 * @version V1.0
 */
public class AnswerResponse extends DealMessageResponse {

    private int nextQuestion;

    public void setNextQuestion(int nextQuestion) {
        this.nextQuestion = nextQuestion;
    }

    public int getNextQuestion() {
        return nextQuestion;
    }
}
