package com.epic.followup.temporary.web;

import javax.validation.constraints.NotNull;

/**
 * @author : zx
 * @version V1.0
 */
public class LoginRequest {
    @NotNull
    public String username;

    @NotNull
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
