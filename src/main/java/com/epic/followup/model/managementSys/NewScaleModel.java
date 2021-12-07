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

    // 回答时间
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "answer_time")
    private Date answerTime;
}
