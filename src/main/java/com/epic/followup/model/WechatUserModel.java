package com.epic.followup.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * @author : zx
 * @version V1.0
 */


@Entity
@Table(name = "wechat_user",
        indexes = {@Index(columnList = "openid")})  // 为字段openid加上索引
@EntityListeners(AuditingEntityListener.class)
public class WechatUserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, columnDefinition = "BIGINT COMMENT 'wechat_user主键'")
    private Long userId;

    // 微信平台用户身份id
    @Column(name = "openid", length = 32, nullable = false)
    private String openId;

    // 微信平台用户身份id
    @Column(name = "sessionkey", length = 256, nullable = true)
    private String sessionKey;

    // 性别
    @Column(name = "gender", length = 2, nullable = true)
    private String gender;

    // 标签
    @Column(name = "tabs", length = 8, nullable = true)
    private String tabs;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "birthday", nullable = true)
    private java.util.Date birthday;

    // 标签
    @Column(name = "username", length = 128, nullable = true)
    private String userName;

    // 是否注册了速眠平台
    @Column(name = "sumian_registed", nullable = true)
    private int sumianRegisted = 0;

    // 用户类型
    // 正常 1 疑似 2  感染 3  医护 4
    @Column(name = "doctor_tyoe", nullable = true,columnDefinition="int default 0")
    private int doctorType  = 0;

    // unionid 用户唯一标识
    @Column(name = "unionid", length = 500, nullable = true)
    private String unionId;

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public int getSumianRegisted() {
        return sumianRegisted;
    }

    public void setSumianRegisted(int sumianRegisted) {
        this.sumianRegisted = sumianRegisted;
    }

    public String getTabs() {
        return tabs;
    }

    public String getGender() {
        return gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public Long getUserId() {
        return userId;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public String getOpenId() {
        return openId;
    }

    public void setTabs(String tabs) {
        this.tabs = tabs;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getDoctorType() {
        return doctorType;
    }

    public void setDoctorType(int doctorType) {
        this.doctorType = doctorType;
    }
}
