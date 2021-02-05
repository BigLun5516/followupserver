package com.epic.followup.model.app;


import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;


@Entity
@Table(name = "app_doctor")
@EntityListeners(AuditingEntityListener.class)
public class AppDoctorModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT 'app_doctor主键'")
    private Long doctorId;

    // 职工号
    @Column(name = "employee_num", length = 32, nullable = false)
    private String employeeNum;

    // 密码
    @Column(name = "password", length = 32, nullable = false)
    private String password;

    // 电话
    @Column(name = "tel", length = 11, nullable = true)
    private String tel;

    // 头像
    @Column(name = "photo", nullable = true)
    private byte[] photo;

    // 姓名
    @Column(name = "name", length = 32, nullable = true)
    private String name;

    // 科室
    @Column(name = "department", length = 128, nullable = true)
    private String department;

    // 职称
    @Column(name = "title", length = 128, nullable = true)
    private String title;

    // 专长
    @Column(name = "speciality", length = 128, nullable = true)
    private String speciality;

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getEmployeeNum() {
        return employeeNum;
    }

    public void setEmployeeNum(String employeeNum) {
        this.employeeNum = employeeNum;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }
}
