package com.epic.followup.model.managementSys;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 心理咨询中心表
 */
@Entity
@Data
@Table(name = "management_psychological_center")
public class PsychologicalCenterModel {

    // 心理中心id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "center_id")
    private Integer centerId;

    // 心理中心名字
    @Column(name = "center_name", nullable = false)
    private String centerName;

    // 心理中心负责人
    @Column(name = "center_manager", length = 64)
    private String centerManager;

    // logo路径
    @Column(name = "logo_url")
    private String logoUrl;

    // 联系电话
    @Column(name = "tel", length = 32)
    private String tel;

    // 创建日期
    @Temporal(TemporalType.DATE)
    @Column(name = "create_time")
    private Date createDate;

    // 心理中心总人数
    @Column(name = "total_num")
    private Integer totalNum;

}
