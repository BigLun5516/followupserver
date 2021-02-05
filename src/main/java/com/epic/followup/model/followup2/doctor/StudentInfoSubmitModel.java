package com.epic.followup.model.followup2.doctor;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * @author : zx
 * @version V1.0
 */

@Entity
@Table(name = "aidoctor_studentinfosubmit_file")  // 为字段加上索引
@EntityListeners(AuditingEntityListener.class)
public class StudentInfoSubmitModel {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT 'aidoctor_studentinfosubmit_file主键'")
    private Long Id;

    // 标志位 1:excl
    @Column(name = "type", nullable = false)
    private int type;

    // 文件保存路径
    @Column(name = "path", length = 500, nullable = false)
    private String path;

    // 上传时间
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "answer_time", nullable = false)
    private Date answerTime;

    // 标志位 用来表示数据是否完整，不完整的数据可以删除
    @Column(name = "succ", nullable = false)
    private int succ = 0;


    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
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

    public int getSucc() {
        return succ;
    }

    public void setSucc(int succ) {
        this.succ = succ;
    }
}
