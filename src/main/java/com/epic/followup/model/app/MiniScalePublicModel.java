package com.epic.followup.model.app;


import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * @author : zmm
 * @version V1.0
 */
@Entity
@Table(name = "mini_scale_public")
@EntityListeners(AuditingEntityListener.class)
public class MiniScalePublicModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT 'mini_scale_public主键'")
    private Long Id;

    // 回答时间
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "mini_time", nullable = false)
    private Date miniTime;

    // mini量表回答
    @Column(name = "mini_answer", length = 256, nullable = false)
    private String miniAnswer;

    // mini量表结果
    @Column(name = "mini_result", length = 256, nullable = false)
    private String miniResult;

    // 用户id
    @Column(name = "openid", length = 32, nullable = false)
    private String openId;

    // 用户姓名
    @Column(name = "userName", length = 32)
    private String userName;

    // 总共用时
    @Column(name = "total_time", length = 256)
    private String total_time;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
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

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTotal_time() {
        return total_time;
    }

    public void setTotal_time(String total_time) {
        this.total_time = total_time;
    }
}
