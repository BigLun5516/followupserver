package com.epic.followup.model.managementSys.task;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "management_task")
@EntityListeners(AuditingEntityListener.class)
public class TaskModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id", nullable = false, columnDefinition = "BIGINT COMMENT 'management_task主键'")
    private Long taskId;

    // 标题
    @Column(name = "title", length = 255, nullable = true)
    private String title;

    // 标题
    @Column(name = "content", length = 255, nullable = true)
    private String content;

    // 入学年级
    @Column(name = "grade", length = 10, nullable = true)
    private String grade;

    // 对应量表
    @Column(name = "scale_id",  nullable = false)
    private Long scaleId;

    // 任务状态
    @Column(name = "task_status",  nullable = false)
    private Integer taskStatus;

    // 创建时间
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    private Date createTime;


}
