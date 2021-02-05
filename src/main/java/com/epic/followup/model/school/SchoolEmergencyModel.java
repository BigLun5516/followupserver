package com.epic.followup.model.school;


import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "school_emergency")
@EntityListeners(AuditingEntityListener.class)
public class SchoolEmergencyModel {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT 'school_emergency主键'")
    private Long Id;

    //紧急事件对应的辅导员id
    @Column(name = "c_id", length = 10, nullable = false)
    private Long cId;

    //紧急事件对应的学生id
    @Column(name = "s_id", length = 10, nullable = false)
    private Long sId;

    // 紧急事件
    @Column(name = "event", length = 255, nullable = true)
    private String event;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Long getcId() {
        return cId;
    }

    public void setcId(Long cId) {
        this.cId = cId;
    }

    public Long getsId() {
        return sId;
    }

    public void setsId(Long sId) {
        this.sId = sId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
