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


    // 创建时间
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    private Date createTime;

    // 任务对应学校
    @Column(name = "university_id",  nullable = false)
    private Integer universityId;

    // 任务对应学校的学院
    @Column(name = "college_id",  nullable = false)
    private Integer collegeId;

}
