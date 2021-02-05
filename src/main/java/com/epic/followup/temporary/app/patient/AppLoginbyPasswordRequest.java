package com.epic.followup.temporary.app.patient;

/**
 * @author : zx
 * @version V1.0
 */
public class AppLoginbyPasswordRequest {

    private String tel;
    private String password;



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
