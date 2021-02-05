package com.epic.followup.model.managementSys;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;


/**
 * 心理咨询师表
 */
@Entity
@Data
@Table(name = "management_psychological_consultant")
@ToString
public class PsychologicalConsultantModel {

    // 心理咨询师id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consultant_id")
    private Integer consultantId;

    // 心理咨询师名字
    @Column(name = "consultant_name", length = 64, nullable = false)
    private String consultantName;

    // 所属心理咨询中心id
    @Column(name = "center_id")
    private Integer centerId;
}
