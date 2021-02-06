package com.epic.followup.model.managementSys;

import lombok.Data;

import javax.annotation.sql.DataSourceDefinition;
import javax.persistence.*;
import java.util.Date;


/**
 * 高校表
 */
@Entity
@Data
@Table(name = "management_university")
public class UniversityModel {

    // 高校id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "university_id")
    private Integer universityId;

    // 所在省
    @Column(name = "province")
    private String province;

    // 所在市
    @Column(name = "city")
    private String city;

    // 高校名称
    @Column(name = "university_name", length = 64)
    private String universityName;

    // 活跃人数占比（%）
    @Column(name = "active_num")
    private Integer activeNum;

    // 总人数
    @Column(name = "all_num")
    private Integer allNum;

    // 学生人数
    @Column(name = "student_num")
    private Integer studentNum;

    // 教师人数
    @Column(name = "teacher_num")
    private Integer teacherNum;

    // 学校负责人
    @Column(name = "university_manager")
    private String universityManager;

    // 学校加入系统日期
    @Temporal(TemporalType.DATE)
    @Column(name = "join_date")
    private Date joinDate;

    // 心理咨询中心id
    @Column(name = "center_id")
    private Integer centerId;

    // 星期评分
    @Column(name = "university_star")
    private Integer star;

//    // 高校校区
//    @Column(name = "campus", length = 64)
//    private String campus;

    // 地址
    @Column(name = "address")
    private String address;

    // 状态
    @Column(name = "university_status")
    private Integer universityStatus;

    // 公告
    @Column(name = "notice")
    private String notice;

    // 负责人手机号
    @Column(name = "manager_phone", length = 32)
    private String managerPhone;
}
