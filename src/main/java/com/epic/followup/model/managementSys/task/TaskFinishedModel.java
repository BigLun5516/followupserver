package com.epic.followup.model.managementSys.task;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "management_task_finished")
@EntityListeners(AuditingEntityListener.class)
public class TaskFinishedModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT 'management_task_finished主键'")
    private Long Id;

    // 用户id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // 任务id
    @Column(name = "task_id", nullable = false)
    private Long taskId;


    // 更新时间
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time")
    private Date updateTime;


}
