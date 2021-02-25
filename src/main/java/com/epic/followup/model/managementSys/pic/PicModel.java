package com.epic.followup.model.managementSys.pic;

import lombok.Data;

import javax.persistence.*;
import java.sql.Blob;

@Entity
@Data
@Table(name = "management_pic")
public class PicModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pic_id")
    private Long picId;

    @Column(name = "pic_name")
    private String picName;

    @Column(name = "pic_type")
    private String picType;

    @Column(name = "pic_status")
    private String picStatus;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "pic_details")
    private byte[] picDetails;
}
