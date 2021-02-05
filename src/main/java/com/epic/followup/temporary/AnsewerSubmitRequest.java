package com.epic.followup.temporary;

/**
 * @author : zx
 * @version V1.0
 */
public class AnsewerSubmitRequest {

    private String openid;
    private String session_key;
    private int question;
    private String answer;
    private int result;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getSession_key() {
        return session_key;
    }

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public int getQuestion() {
        return question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setQuestion(int question) {
        this.question = question;
    }
}
