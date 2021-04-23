package com.epic.followup.model.managementSys;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Data
@Table(name = "management_data_permission")
@EntityListeners(AuditingEntityListener.class)
public class DataPermissionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT 'management_data_permission主键'")
    private Integer Id;

    // 学校id
    @Column(name = "university_id", nullable = false)
    private Integer universityId;

    // 学院id
    @Column(name = "college_id", nullable = false)
    private Integer collegeId;

    // 用户id
    @Column(name = "user_id", nullable = false)
    private Long userId;
}
