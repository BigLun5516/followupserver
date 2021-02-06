package com.epic.followup.model.managementSys;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 学院表
 */
@Entity
@Data
@Table(name = "management_college")
public class CollegeModel {

    // 学院id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "college_id")
    private Integer collegeId;

    // 所属高校id
    @Column(name = "university_id")
    private Integer universityId;

    // 学院名字
    @Column(name = "college_name", length = 64)
    private String collegeName;

    // 学院负责人
    @Column(name = "college_manager", length = 64)
    private String collegeManager;

    // 学院辅导员
    @Column(name = "college_counselor", length = 64)
    private String collegeCounselor;

    // 学院老师人数
    @Column(name = "teacher_num")
    private Integer teacherNum;

    // 学院状态
    @Column(name = "college_status")
    private Integer collegeStatus;

    // 联系方式
    @Column(name = "college_phone", length = 32)
    private String collegePhone;

    // 学院学生人数
    @Column(name = "student_num")
    private Integer studentNum;

    // 来访人次
    @Column(name = "arrive_num")
    private Integer arriveNum;

    // 创建时间
    @Temporal(TemporalType.DATE)
    @Column(name = "create_time")
    private Date createTime;
}
