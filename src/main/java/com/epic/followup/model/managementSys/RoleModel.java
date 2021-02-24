package com.epic.followup.model.managementSys;


import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Data
@Table(name = "management_role")
@EntityListeners(AuditingEntityListener.class)
public class RoleModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT 'management_role主键'")
    private Long Id;

    // 角色名
    @Column(name = "name", length = 128)
    private String name;

    // 角色备注
    @Column(name = "remark", length = 255)
    private String remark;

    // 权限（全部）
    @Column(name = "limit1", length = 255)
    private String limit1;

    // 权限（只有选中）
    @Column(name = "limit2", length = 255)
    private String limit2;
}
