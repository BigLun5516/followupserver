package com.epic.followup.model.managementSys.pic;

import lombok.Data;

import javax.persistence.*;
import java.sql.Blob;
import java.util.Date;

@Entity
@Data
@Table(name = "management_pic")
public class PicModel {


    // 图文id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pic_id")
    private Long picId;

    // 图文名称
    @Column(name = "pic_name")
    private String picName;

    // 图文类型
    @Column(name = "pic_type")
    private String picType;

    // 图文状态
    @Column(name = "pic_status")
    private String picStatus;

    // 创建日期
    @Temporal(TemporalType.DATE)
    @Column(name = "create_time")
    private Date createTime;

    // 图文内容
    @Column(name = "pic_details")
    private byte[] picDetails;


}
