package com.epic.followup.temporary.app.manager;

public class BaseManagerSession {
    private Long managerId;
    private String username;

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
