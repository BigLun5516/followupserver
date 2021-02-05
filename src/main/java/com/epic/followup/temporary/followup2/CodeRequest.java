package com.epic.followup.temporary.followup2;

/**
 * @author : zx
 * @version V1.0
 */
public class CodeRequest {

    private String tel;

 //原始 0注册 1重置密码
//app中 0登录，1注册，2修改密码，3绑定手机号
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
