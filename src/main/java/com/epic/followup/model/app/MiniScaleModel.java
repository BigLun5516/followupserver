package com.epic.followup.model.app;


import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * @author : zmm
 * @version V1.0
 */
@Entity
@Table(name = "mini_scale")
@EntityListeners(AuditingEntityListener.class)
public class MiniScaleModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT 'mini_scale主键'")
    private Long miniId;

    // 回答时间
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "mini_time")
    private Date miniTime;

    // mini量表回答
    @Column(name = "mini_answer", length = 256, nullable = false)
    private String miniAnswer;

    // mini量表结果
    @Column(name = "mini_result", length = 256, nullable = false)
    private String miniResult;

    // 用户id
    @Column(name = "userid")
    private Long userId;

    public Long getMiniId() {
        return miniId;
    }

    public void setMiniId(Long miniId) {
        this.miniId = miniId;
    }

    public Date getMiniTime() {
        return miniTime;
    }

    public void setMiniTime(Date miniTime) {
        this.miniTime = miniTime;
    }

    public String getMiniAnswer() {
        return miniAnswer;
    }

    public void setMiniAnswer(String miniAnswer) {
        this.miniAnswer = miniAnswer;
    }

    public String getMiniResult() {
        return miniResult;
    }

    public void setMiniResult(String miniResult) {
        this.miniResult = miniResult;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
