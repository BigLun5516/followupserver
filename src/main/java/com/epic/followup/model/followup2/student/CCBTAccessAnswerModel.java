package com.epic.followup.model.followup2.student;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;


/**
 * CCBT 评估模块答案
 *
 * @Author zhaoyiting
 */
@Entity
@Table(name = "aidoctor_ccbtaccess_answer",
        indexes = {@Index(columnList = "userid, createtime")})  // 为字段加上索引
@EntityListeners(AuditingEntityListener.class)
public class CCBTAccessAnswerModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT '主键'")
    private Long id;

    // 用户id
    @Column(name = "userid", nullable = false)
    private Long userId;

    // 抑郁情绪测验答案
    @Column(name = "depress_answer", length = 40, nullable = true)
    private String depressAnswer;

    // 抑郁情绪测验总得分
    @Column(name = "depress_score", nullable = true)
    private int depressScore;

    // 焦虑量表答案
    @Column(name = "anxious_answer", length = 40, nullable = true)
    private String anxiousAnswer;

    // 焦虑量表总得分
    @Column(name = "anxious_score", nullable = true)
    private int anxiousScore;

    // 失眠量表答案
    @Column(name = "sleep_answer", length = 40, nullable = true)
    private String sleepAnswer;

    // 失眠量表总得分
    @Column(name = "sleep_score", nullable = true)
    private int sleepScore;

    // 认知歪曲答案
    @Column(name = "cognition_answer", length = 100, nullable = true)
    private String cognitionAnswer;

    // 人际关系答案
    @Column(name = "relationship_answer", length = 40,nullable = true)
    private String relationshipAnswer;

    // 回答时间 起始时间
    @Temporal(TemporalType.DATE)
    @Column(name = "createtime", nullable = false)
    private Date createTime;

    // 状态
    @Column(name = "status", nullable = true)
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

    public String getDepressAnswer() {
        return depressAnswer;
    }

    public void setDepressAnswer(String depressAnswer) {
        this.depressAnswer = depressAnswer;
    }

    public int getDepressScore() {
        return depressScore;
    }

    public void setDepressScore(int depressScore) {
        this.depressScore = depressScore;
    }

    public String getAnxiousAnswer() {
        return anxiousAnswer;
    }

    public void setAnxiousAnswer(String anxiousAnswer) {
        this.anxiousAnswer = anxiousAnswer;
    }

    public int getAnxiousScore() {
        return anxiousScore;
    }

    public void setAnxiousScore(int anxiousScore) {
        this.anxiousScore = anxiousScore;
    }

    public String getSleepAnswer() {
        return sleepAnswer;
    }

    public void setSleepAnswer(String sleepAnswer) {
        this.sleepAnswer = sleepAnswer;
    }

    public int getSleepScore() {
        return sleepScore;
    }

    public void setSleepScore(int sleepScore) {
        this.sleepScore = sleepScore;
    }

    public String getCognitionAnswer() {
        return cognitionAnswer;
    }

    public void setCognitionAnswer(String cognitionAnswer) {
        this.cognitionAnswer = cognitionAnswer;
    }

    public String getRelationshipAnswer() {
        return relationshipAnswer;
    }

    public void setRelationshipAnswer(String relationshipAnswer) {
        this.relationshipAnswer = relationshipAnswer;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}

