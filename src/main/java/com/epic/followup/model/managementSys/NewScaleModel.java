package com.epic.followup.model.managementSys;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "new_scale")
public class NewScaleModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT 'new_scale主键'")
    private Long id;

    @Column(name = "answer")
    private String answer;

    @Column(name = "phone")
    private String phone;

    @Column(name = "scale_id")
    private Integer scaleId;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "finish_time")
    private String finishTime;

    @Column(name = "ip")
    private String ip;

    @Column(name = "region")
    private String region;




}
