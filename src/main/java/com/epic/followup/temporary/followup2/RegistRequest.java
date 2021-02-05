package com.epic.followup.temporary.followup2;

/**
 * @author : zx
 * @version V1.0
 */
public class RegistRequest {

    // 验证码
    private String code;

    // 平台密码 初始化为工号id后6位
    private String password;

    // 电话号码
    private String tel;

    //  学工号
    private String stid;

    // 部门 用'/'划分层级关系
    private String department;

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getStid() {
        return stid;
    }

    public void setStid(String stid) {
        this.stid = stid;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
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
