package com.epic.followup.temporary.app.manager;

import com.epic.followup.temporary.DealMessageResponse;

public class LoginManagerResponse extends DealMessageResponse {

    private String sessionId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
