package com.epic.followup.temporary.followup2;

/**
 * @author : zx
 * @version V1.0
 */
public class ResetPasswordRequest {

    private String tel;
    private String code;
    private String password;

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
