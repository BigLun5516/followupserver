package com.epic.followup.temporary.wechat.patient.diary;

import com.epic.followup.temporary.DealMessageResponse;

public class getPatientDiaryResponse extends DealMessageResponse {

    private Long Id;


    private String mood;


    private String time;


    private String html;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
