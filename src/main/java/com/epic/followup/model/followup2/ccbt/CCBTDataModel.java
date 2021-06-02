package com.epic.followup.model.followup2.ccbt;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * ccbt中的用户提交的内容(所有提交整合成了统一的格式)
 */
@Entity
@Table(name = "ccbt_data")
@Data
public class CCBTDataModel {

    // 无意义的主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 提交的类型，用于定位提交的是什么内容
    @Column(name = "commit_type")
    private String commitType;

    // 提交的内容(整合成一个字符串)
    @Column(name = "commit")
    private String commit;

    // 提交时间
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    // 外键
    @Column(name = "userid")
    private Long userId;


}
