package com.epic.followup.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * @author : zx
 * @version V1.0
 */

@Entity
@Table(name = "answer",
        indexes = {@Index(columnList = "openid, answer_time")})  // 为字段加上索引
@EntityListeners(AuditingEntityListener.class)
public class AnswerModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT 'answer主键'")
    private Long Id;

    // 问题序号
    @Column(name = "answer_number", nullable = false)
    private int number;

    // 回答时间
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "answer_time", nullable = false)
    private java.util.Date answerTime;

    // 回答结果
    @Column(name = "answer_result", length = 256, nullable = false)
    private String answerResult;

    // 评测结果
    @Column(name = "analyse_result", nullable = false)
    private double analyseResult;

    // 用户id
    @Column(name = "openid", length = 32, nullable = false)
    private String openId;

    // 标志位 用来表示数据是否完整，不完整的数据可以删除
    @Column(name = "succ", nullable = false)
    private int succ = 0;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Date getAnswerTime() {
        return answerTime;
    }

    public void setId(Long id) {
        Id = id;
    }

    public double getAnalyseResult() {
        return analyseResult;
    }

    public void setAnalyseResult(double analyseResult) {
        this.analyseResult = analyseResult;
    }

    public int getNumber() {
        return number;
    }

    public void setAnswerResult(String answerResult) {
        this.answerResult = answerResult;
    }

    public Long getId() {
        return Id;
    }

    public void setAnswerTime(Date answerTime) {
        this.answerTime = answerTime;
    }

    public String getAnswerResult() {
        return answerResult;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
