package com.epic.followup.model.managementSys.task;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "management_task_status")
@EntityListeners(AuditingEntityListener.class)
public class TaskStatusModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT 'management_task_status主键'")
    private Long Id;

    // 用户id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // 任务id
    @Column(name = "task_id", nullable = false)
    private Long taskId;

    // 完成状态
    @Column(name = "status",  nullable = false)
    private Integer status;

    // 更新时间
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time")
    private Date updateTime;


}
