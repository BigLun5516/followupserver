package com.epic.followup.model.followup2.doctor;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * 存储分析结果
 * @author : zx
 * @version V1.0
 */

@Entity
@Table(name = "aidoctor_studentresult",
        indexes = {@Index(columnList = "userid, update_time")})
@EntityListeners(AuditingEntityListener.class)
public class StudentResultModel {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT 'aidoctor_studentresult主键'")
    private Long Id;

    // 统计时间
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time", nullable = false)
    private Date updateTime;

     // 学校
     @Column(name = "department", length = 32, nullable = false)
     private String department;

    // 学院
    @Column(name = "college", length = 32, nullable = false)
    private String college;

    // 学号
    @Column(name = "stid", length = 32, nullable = false)
    private String stid;

    // 姓名
    @Column(name = "stname", length = 32, nullable = false)
    private String stname;

    // 性别
    @Column(name = "gender", length = 2, nullable = false)
    private String gender;

    // 照片路径
    @Column(name = "imgpath", length = 256, nullable = true)
    private String imgPath;

    // 生源地
    @Column(name = "province", length = 32, nullable = false)
    private String province;

    // 类别 0本科 1硕士 2博士
    @Column(name = "stype", nullable = false)
    private int stype;

    // 年级 2015
    @Column(name = "year", length = 10, nullable = false)
    private String year;

    // 症状 0001000 有为1 没有为0
    /*
    计算sym 依次为 从1到20题
  `            *症状
                抑郁/不开心 时长 兴趣
                兴趣缺失 注意力不能集中 犹豫 选择困难 异常表现
                无价值感 自残倾向 自杀倾向 自杀计划 睡眠时长 睡眠质量
                睡眠障碍 食欲异常 动作缓慢、容易烦躁 时而躁狂 社会关系、不安全感 焦虑、抑郁
     */
    @Column(name = "symptoms", nullable = false)
    private String symptoms;

    // 抑郁程度得分
    @Column(name = "score", nullable = false)
    private int score;

    // 抑郁程度得分
    @Column(name = "level", length = 10, nullable = false)
    private String level;

    // 年龄
    @Column(name = "age", nullable = false)
    private int age;

    // 用户id
    @Column(name = "userid")
    private Long userId;

    public String getStname() {
        return stname;
    }

    public void setStname(String stname) {
        this.stname = stname;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDepartment() {
        return department;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getCollege() {
        return college;
    }

    public String getProvince() {
        return province;
    }

    public String getStid() {
        return stid;
    }

    public Long getId() {
        return Id;
    }

    public String getGender() {
        return gender;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public int getStype() {
        return stype;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public int getScore() {
        return score;
    }

    public Long getUserId() {
        return userId;
    }

    public String getYear() {
        return year;
    }

    public int getAge() {
        return age;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public void setStid(String stid) {
        this.stid = stid;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setId(Long id) {
        Id = id;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setStype(int stype) {
        this.stype = stype;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

