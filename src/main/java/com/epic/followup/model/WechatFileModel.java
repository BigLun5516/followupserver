package com.epic.followup.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * @author : zx
 * @version V1.0
 */

@Entity
@Table(name = "wechat_file",
        indexes = {@Index(columnList = "openid")})  // 为字段加上索引
@EntityListeners(AuditingEntityListener.class)
public class WechatFileModel {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT 'wechat_file主键'")
    private Long Id;

    // 标志位 1:图片，2:视频
    @Column(name = "type", nullable = false)
    private int type;

    // 用户id
    @Column(name = "openid", length = 32, nullable = false)
    private String openId;

    // 文件保存路径
    @Column(name = "path", length = 64, nullable = false)
    private String path;

    // 上传时间
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "answer_time", nullable = false)
    private java.util.Date answerTime;

    // 标志位 用来表示数据是否完整，不完整的数据可以删除
    @Column(name = "succ", nullable = false)
    private int succ = 0;

    // 问题序号
    @Column(name = "answer_number", nullable = false)
    private int number;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getUploadTime() {
        return answerTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.answerTime = uploadTime;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSucc() {
        return succ;
    }

    public void setSucc(int succ) {
        this.succ = succ;
    }
}
