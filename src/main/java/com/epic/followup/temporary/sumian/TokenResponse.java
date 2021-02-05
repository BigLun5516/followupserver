package com.epic.followup.temporary.sumian;

import com.epic.followup.temporary.DealMessageResponse;

/**
 * @author : zx
 * @version V1.0
 */
public class TokenResponse extends DealMessageResponse {

    private String token;
    private String expired_at;

    public String getExpired_at() {
        return expired_at;
    }

    public void setExpired_at(String expired_at) {
        this.expired_at = expired_at;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
