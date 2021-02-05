package com.epic.followup.model.followup2;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * @author : zx
 * @version V1.0
 */


@Entity
@Table(name = "aidoctor_wechatuser",
        indexes = {@Index(columnList = "openid")})  // 为字段openid加上索引
@EntityListeners(AuditingEntityListener.class)
public class WechatAppUserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, columnDefinition = "BIGINT COMMENT 'aidoctor_wechatuser主键'")
    private Long userId;

    // 微信平台用户身份id
    @Column(name = "openid", length = 32, nullable = false)
    private String openId;

    // 微信平台
    @Column(name = "sessionkey", length = 256, nullable = true)
    private String sessionKey;

    // 存储sessionid,用于免登陆验证
    @Column(name = "sessionid", length = 32, nullable = false)
    private String sessionId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createtime", nullable = true)
    private java.util.Date createTime;

    // unionid 用户唯一标识
    @Column(name = "unionid", length = 500, nullable = true)
    private String unionId;

    public String getSessionId() {
        return sessionId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Long getUserId() {
        return userId;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public String getOpenId() {
        return openId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
