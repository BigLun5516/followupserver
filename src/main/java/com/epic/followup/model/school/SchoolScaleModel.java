package com.epic.followup.model.school;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "school_template")
@EntityListeners(AuditingEntityListener.class)
public class SchoolScaleModel {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT 'school_template主键'")
    private Long Id;

    // 使用该量表的用户
    @Column(name = "userId", nullable = false)
    private Long userId;

    // 量表名字
    @Column(name = "name", length = 32, nullable = false)
    private String name;

    // 适用人群
    @Column(name = "targetUserType", nullable = false)
    private Long targetUserType;

    // 评测说明
    @Column(name = "comment", nullable = true)
    private String comment;
}
