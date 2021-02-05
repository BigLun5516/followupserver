package com.epic.followup.model.managementSys;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 心理咨询信息表
 */
@Entity
@Table(name = "management_psychological_info")
@Data
public class PsychologicalInfoModel {

    // id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "psychological_info_id")
    private Long psychologicalInfoId;

    // 心理中心id
    @Column(name = "center_id")
    private Integer centerId;

    // 心理咨询师id
    @Column(name = "consultant_id")
    private Integer consultantId;

    // 心理咨询师名字
    @Column(name = "consultant_name", length = 64, nullable = false)
    private String consultantName;

    //  姓名
    @Column(name = "stname", length = 16, nullable = true)
    private String stname;

    //  学工号
    @Column(name = "stid", length = 32, nullable = false)
    private String stid;

    // 咨询开始时间
    @Column(name = "consultation_time_start")
    private String consultationStart;

    // 咨询结束时间
    @Column(name = "consultation_time_end")
    private String consultationEnd;

    // 咨询时长(小时为单位)
    @Column(name = "consultation_time_length")
    private Integer consultationLength;
}
