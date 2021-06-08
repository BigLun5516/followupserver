package com.epic.followup.model.followup2.ccbt;

import lombok.Data;
import net.sf.cglib.core.GeneratorStrategy;

import javax.persistence.*;

/**
 * 记录ccbt中的用户的进度
 */
@Table(name = "ccbt_progress")
@Data
@Entity
public class CCBTProgressModel {

    // 无实际意义的主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 目前进行的模块标号
    @Column(name = "current_index")
    private Integer currentIndex;

    // 进度总长度
    @Column(name = "total")
    private Integer total;

    // 用户id
    @Column(name = "userid")
    private Long userid;



}
