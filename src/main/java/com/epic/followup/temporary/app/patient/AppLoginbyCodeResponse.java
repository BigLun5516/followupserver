package com.epic.followup.temporary.app.patient;

import com.epic.followup.temporary.DealMessageResponse;

public class AppLoginbyCodeResponse extends DealMessageResponse {
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
