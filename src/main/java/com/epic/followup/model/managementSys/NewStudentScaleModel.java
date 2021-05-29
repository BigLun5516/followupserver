package com.epic.followup.model.managementSys;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "new_student_scale")
@EntityListeners(AuditingEntityListener.class)
@Data
public class NewStudentScaleModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT 'new_student_scale主键'")
    private Long id;

    // 回答时间
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "answer_time")
    private Date answerTime;

    // 新生量表回答答案
    @Column(name = "answer_array", length = 1024)
    private String answerArray;

    // 新生量表评测结果
    @Column(name = "answer_result", length = 256)
    private String answerResult;

    // 新生量表类型（0代表症状自测，1代表人格测试）
    @Column(name = "scale_id", nullable = false)
    private Integer scaleId;

    // 用户id
    @Column(name = "userid")
    private Long userId;

}
