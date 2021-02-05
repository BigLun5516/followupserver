package com.epic.followup.model.followup2;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * 随访系统2.0基础用户表，用来存储用户的基础信息，包括电话号码，学工号，
 * @author : zx
 * @version V1.0
 */

@Entity
@Table(name = "aidoctor_baseuser",
        indexes = {@Index(columnList = "tel")})  // 为字段openid加上索引
@EntityListeners(AuditingEntityListener.class)
public class BaseUserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid", nullable = false, columnDefinition = "BIGINT COMMENT 'aidoctor_baseuser主键'")
    private Long userId;

    // 用户昵称 默认为'默认昵称'
    @Column(name = "username", length = 32, nullable = true)
    private String userName;

    // 平台密码 初始化为工号id后6位
    @Column(name = "password", length = 32, nullable = false)
    private String password;

    // 电话号码
    @Column(name = "tel", length = 11, nullable = false)
    private String tel;

    // 创建时间
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time", nullable = false)
    private java.util.Date createTime;

    //  学工号
    @Column(name = "stid", length = 32, nullable = false)
    private String stid;

    // 学校 用'/'划分层级关系
    @Column(name = "department", length = 256, nullable = false)
    private String department;

    // 权限 0 超级管理员， 1 领导， 2 普通教师， 3 学生
    @Column(name = "type", nullable = false)
    private int type;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDepartment() {
        return department;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getUserName() {
        return userName;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStid() {
        return stid;
    }

    public void setStid(String stid) {
        this.stid = stid;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
