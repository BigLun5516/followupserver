package com.epic.followup.temporary.app.doctor;

import com.epic.followup.temporary.DealMessageResponse;

public class DoctorInfoResponse extends DealMessageResponse {
    private DoctorInfo data;

    public DoctorInfo getData() {
        return data;
    }

    public void setData(DoctorInfo data) {
        this.data = data;
    }
}
