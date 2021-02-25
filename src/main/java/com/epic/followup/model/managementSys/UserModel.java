package com.epic.followup.model.managementSys;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Data
@Table(name = "management_user")
@EntityListeners(AuditingEntityListener.class)
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT 'manager_user主键'")
    private Long userId;

    // 密码
    @Column(name = "password", length = 32, nullable = false)
    private String password;

    // 电话
    @Column(name = "tel", length = 11, nullable = true)
    private String tel;

    // 头像
    @Column(name = "imageUrl", nullable = true)
    private String imageUrl;

    // 姓名
    @Column(name = "userName", length = 32, nullable = true)
    private String userName;

    // 学校id
    @Column(name = "university_id")
    private Integer universityId;

    // 用户类型
    @Column(name = "userType", nullable = false)
    private Long userType;

    // 高校id
    @Column(name = "universityId", nullable = false)
    private Integer universityId;

}
