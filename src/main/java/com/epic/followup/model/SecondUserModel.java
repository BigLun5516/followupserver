package com.epic.followup.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * @author : zx
 * @version V1.0
 */

@Entity
@Table(name = "second_user",
        indexes = {@Index(columnList = "openid")})  // 为字段openid加上索引
@EntityListeners(AuditingEntityListener.class)
public class SecondUserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, columnDefinition = "BIGINT COMMENT 'second_user主键'")
    private Long userId;

    // 微信平台用户身份id
    @Column(name = "openid", length = 32, nullable = true)
    private String openId;

    // 平台密码
    @Column(name = "username", length = 32, nullable = false)
    private String userName;

    // 平台密码
    @Column(name = "password", length = 32, nullable = false)
    private String password;

    // 部门
    @Column(name = "department", length = 32, nullable = true)
    private String department;

    // 学院
    @Column(name = "college", length = 32, nullable = true)
    private String college;

    // 权限 0 超级管理员， 1 领导， 2 普通教师， 3 学生
    @Column(name = "type", nullable = false)
    private int type;

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
}
