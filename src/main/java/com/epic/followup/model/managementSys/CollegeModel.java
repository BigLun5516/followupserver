package com.epic.followup.model.managementSys;


import lombok.Data;

import javax.persistence.*;

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


}
