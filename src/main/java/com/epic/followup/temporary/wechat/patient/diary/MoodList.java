package com.epic.followup.temporary.wechat.patient.diary;


public class MoodList {
    private String mood;
    private String time;

    public MoodList(String mood, String time) {
        this.mood = mood;
        this.time = time;
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
}
