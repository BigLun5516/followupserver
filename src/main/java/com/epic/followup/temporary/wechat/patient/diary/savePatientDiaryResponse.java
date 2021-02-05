package com.epic.followup.temporary.wechat.patient.diary;

import com.epic.followup.temporary.DealMessageResponse;

public class savePatientDiaryResponse extends DealMessageResponse {
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
