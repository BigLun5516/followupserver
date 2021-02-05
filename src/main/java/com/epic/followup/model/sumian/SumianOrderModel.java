package com.epic.followup.model.sumian;

import javax.persistence.*;
import java.util.Date;

/**
 * @author : zx
 * @version V1.0
 */
@Entity
@Table(name = "sumian_order",
        indexes = {@Index(columnList = "userid")})  // 为字段加上索引
public class SumianOrderModel {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT 'answer主键'")
    private Long Id;

    // 时间
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "order_time", nullable = false)
    private java.util.Date orderTime;

    // 用户id
    @Column(name = "userid", nullable = false)
    private Long userId;

    // 是否创建成功
    @Column(name = "issucceed", nullable = false)
    private int isSucceed = 0;

    public int getIsSucceed() {
        return isSucceed;
    }

    public void setIsSucceed(int isSucceed) {
        this.isSucceed = isSucceed;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getId() {
        return Id;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setId(Long id) {
        Id = id;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }
}
