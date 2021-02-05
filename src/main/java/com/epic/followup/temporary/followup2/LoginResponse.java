package com.epic.followup.temporary.followup2;

import com.epic.followup.temporary.DealMessageResponse;

/**
 * @author : zx
 * @version V1.0
 */
public class LoginResponse extends DealMessageResponse {

    private String sessionId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
