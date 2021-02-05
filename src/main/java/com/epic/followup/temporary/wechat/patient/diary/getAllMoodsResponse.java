package com.epic.followup.temporary.wechat.patient.diary;

import com.epic.followup.temporary.DealMessageResponse;

import java.util.List;

public class getAllMoodsResponse extends DealMessageResponse {
    private List<MoodList> moods;

    public List<MoodList> getMoods() {
        return moods;
    }

    public void setMoods(List<MoodList> moods) {
        this.moods = moods;
    }
}
