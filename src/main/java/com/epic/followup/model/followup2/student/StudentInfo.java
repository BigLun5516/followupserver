package com.epic.followup.model.followup2.student;

import javax.persistence.*;
import java.util.Date;

/**
 * @author : zx
 * @version V1.0
 */
@Entity
@Table(name = "aidoctor_studentinfo",
        indexes = {@Index(columnList = "userid")})  // 为字段openid加上索引
public class StudentInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "infoid", nullable = false, columnDefinition = "BIGINT COMMENT 'aidoctor_studentinfo主键'")
    private Long infoId;

    // 创建时间
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time", nullable = true)
    private java.util.Date createTime;

    //  姓名
    @Column(name = "stname", length = 16, nullable = true)
    private String stname;

    //  学工号
    @Column(name = "stid", length = 32, nullable = false)
    private String stid;

    // 学校 用'/'划分层级关系
    @Column(name = "department", length = 256, nullable = false)
    private String department;

    // 学校id
    @Column(name = "university_id", nullable = false)
    private Integer universityId;

    // 关联用户表
    @Column(name = "userid", nullable = true)
    private Long userId = -1L;

    // 生源地 省份
    @Column(name = "province", length = 32, nullable = false)
    private String province;

    // 学院
    @Column(name = "college", length = 32, nullable = false)
    private String college;

    // 照片路径
    @Column(name = "imgpath", length = 256, nullable = true)
    private String imgPath;

    // 年龄
    @Column(name = "age", nullable = false)
    private int age;

    // 类别 0本科 1硕士 2博士
    @Column(name = "stype", nullable = false)
    private int stype;

    // 年级 2015
    @Column(name = "year", length = 10, nullable = false)
    private String year;

    // 性别
    @Column(name = "gender", length = 2, nullable = false)
    private String gender;

    // 学院id
    @Column(name = "college_id", nullable = false)
    private Integer collegeId;

    public String getStname() {
        return stname;
    }

    public void setStname(String stname) {
        this.stname = stname;
    }

    public int getAge() {
        return age;
    }

    public String getYear() {
        return year;
    }

    public int getStype() {
        return stype;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setStype(int stype) {
        this.stype = stype;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public Long getUserId() {
        return userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public String getDepartment() {
        return department;
    }

    public String getStid() {
        return stid;
    }

    public Long getInfoId() {
        return infoId;
    }

    public String getCollege() {
        return college;
    }

    public String getProvince() {
        return province;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setStid(String stid) {
        this.stid = stid;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public void setInfoId(Long infoId) {
        this.infoId = infoId;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Integer getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(Integer collegeId) {
        this.collegeId = collegeId;
    }

    public Integer getUniversityId() {
        return universityId;
    }

    public void setUniversityId(Integer universityId) {
        this.universityId = universityId;
    }
}

