package com.epic.followup.model.school;


import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "school_user")
@EntityListeners(AuditingEntityListener.class)
public class SchoolUserModel {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT 'school_user主键'")
    private Long Id;

    // 用户名
    @Column(name = "username", length = 128, nullable = false)
    private String username;

    // 密码
    @Column(name = "password", length = 128, nullable = false)
    private String password;

    // 用户类型
    @Column(name = "type", length = 1, nullable = false)
    private int type;

    //关联辅导员或心理老师的id
    @Column(name = "contact_id", length = 10, nullable = false)
    private Long contact_id;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getName() {
        return username;
    }

    public void setName(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Long getContact_id() {
        return contact_id;
    }

    public void setContact_id(Long contact_id) {
        this.contact_id = contact_id;
    }
}
