package com.epic.followup.temporary.wechat.patient;

public class WechatLoginbyCodeRequest {

    private String tel;

    private String code;


    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
