package com.epic.followup.temporary.wechat.patient;

import com.epic.followup.temporary.DealMessageResponse;

public class WechatLoginbyCodeResponse extends DealMessageResponse {
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
