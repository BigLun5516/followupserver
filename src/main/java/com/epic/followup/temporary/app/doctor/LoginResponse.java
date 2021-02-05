package com.epic.followup.temporary.app.doctor;

import com.epic.followup.temporary.DealMessageResponse;

public class LoginResponse extends DealMessageResponse {
    private String sessionId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
