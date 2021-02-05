package com.epic.followup.temporary.followup2;

/**
 * @author : zx
 * @version V1.0
 */
public class LoginRequest {

    private String tel;
    private String password;
    private String openID;

    public String getOpenID() {
        return openID;
    }

    public void setOpenID(String openID) {
        this.openID = openID;
    }

    public String getPassword() {
        return password;
    }

    public String getTel() {
        return tel;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
