package com.epic.followup.model.school;


import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "school_college")
@EntityListeners(AuditingEntityListener.class)
public class SchoolCollegeModel {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT 'school_college主键'")
    private Long Id;

    // 姓名
    @Column(name = "name", length = 32, nullable = true)
    private String name;

    // 简称
    @Column(name = "sname", length = 32, nullable = true)
    private String sname;

    // 编号
    @Column(name = "jk", length = 32, nullable = true)
    private String jk;

    // 创建时间
    @Column(name = "create_time", length = 128, nullable = true)
    private String create_time;

    // 简介
    @Column(name = "introduction", length = 255, nullable = true)
    private String introduction;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getJk() {
        return jk;
    }

    public void setJk(String jk) {
        this.jk = jk;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
