package com.epic.followup.model.school;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "school_student")
@EntityListeners(AuditingEntityListener.class)
public class SchoolStudentModel {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT 'school_student主键'")
    private Long Id;

    // 姓名
    @Column(name = "name", length = 32, nullable = true)
    private String name;

    // 性别
    @Column(name = "sex", length = 10, nullable = true)
    private String sex;

    // 出生日期
    @Column(name = "birth", length = 128, nullable = true)
    private String birth;

    // 学号
    @Column(name = "stu_number", length = 20, nullable = true)
    private String stu_number;

    // 学院
    @Column(name = "college", length = 128, nullable = true)
    private String college;

    // 班级
    @Column(name = "class", length = 128, nullable = true)
    private String bclass;

    // 入学日期
    @Column(name = "sdate", length = 128, nullable = true)
    private String sdate;

    // 电话
    @Column(name = "phone", length = 128, nullable = true)
    private String phone;

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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getStu_number() {
        return stu_number;
    }

    public void setStu_number(String stu_number) {
        this.stu_number = stu_number;
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

    public String getSdate() {
        return sdate;
    }

    public void setSdate(String sdate) {
        this.sdate = sdate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
