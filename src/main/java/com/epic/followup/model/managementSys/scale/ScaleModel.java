package com.epic.followup.model.managementSys.scale;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Data
@Table(name = "management_scale")
public class ScaleModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scale_id", nullable = false, columnDefinition = "BIGINT COMMENT 'management_scale主键'")
    private Long scaleId;

    @Column(name = "scale_name")
    private String scaleName;

    // 是否内置
    @Column(name = "isnz")
    private int isnz;

    // 量表分类
    @Column(name = "classify")
    private String classify;

    // 评测人次
    @Column(name = "num", columnDefinition = "tinyint default 0")
    private int num;

    // 高校名称
    @Column(name = "school_name")
    private String schoolName;

    // 中心名称
    @Column(name = "organization")
    private String organization;

    // 量表状态 0使用中，1编辑中，2待审核，3已禁用，4已注销
    @Column(name = "status")
    private int status;
}
