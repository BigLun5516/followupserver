package com.epic.followup.model.school;


import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "school_counselor")
@EntityListeners(AuditingEntityListener.class)
public class SchoolCounselorModel {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT 'school_counselor主键'")
    private Long Id;

    // 姓名
    @Column(name = "name", length = 32, nullable = true)
    private String name;

    // 教师编号
    @Column(name = "tch_number", length = 20, nullable = true)
    private String tch_number;

    // 性别
    @Column(name = "sex", length = 10, nullable = true)
    private String sex;

    // 电话
    @Column(name = "phone", length = 128, nullable = true)
    private String phone;

    // 学院
    @Column(name = "college", length = 128, nullable = true)
    private String college;

    // 班级
    @Column(name = "class", length = 128, nullable = true)
    private String bclass;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTch_number() {
        return tch_number;
    }

    public void setTch_number(String tch_number) {
        this.tch_number = tch_number;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getBclass() {
        return bclass;
    }

    public void setBclass(String bclass) {
        this.bclass = bclass;
    }
}
