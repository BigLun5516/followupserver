package com.epic.followup.model.app;


import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "doctor_manager")
@EntityListeners(AuditingEntityListener.class)
public class DoctorManagerModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT 'app_doctor主键'")
    private Long managerId;

    // 用户名
    @Column(name = "username", length = 32, nullable = true)
    private String username;

    // 密码
    @Column(name = "password", length = 32, nullable = false)
    private String password;

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
