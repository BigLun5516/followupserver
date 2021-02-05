package com.epic.followup.temporary.wechat.patient.diary;

import com.epic.followup.temporary.DealMessageResponse;

import java.util.List;

public class getAllPatientDiaryResponse extends DealMessageResponse {
    private List<Diary> diaries;

    public List<Diary> getDiaries() {
        return diaries;
    }

    public void setDiaries(List<Diary> diaries) {
        this.diaries = diaries;
    }
}
