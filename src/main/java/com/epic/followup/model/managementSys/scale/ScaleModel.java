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

    // 是否内置  1:内置  0:自定义
    @Column(name = "isnz")
    private Integer isnz;

    @Column(name = "school_name")
    private String schoolName;
}
