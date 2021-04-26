package com.epic.followup.model.managementSys.scale;

import lombok.Data;
import org.junit.experimental.theories.DataPoints;

import javax.persistence.*;

/**
 * 此表记录了量表的禁用情况
 * 一条记录对应着 某个学校禁用了某个量表
 */
@Entity
@Data
@Table(name = "management_scale_forbidden")
public class ScaleForbiddenModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "scale_id")
    private Long scaleId;

    @Column(name = "university_name")
    private String universityName;

}
