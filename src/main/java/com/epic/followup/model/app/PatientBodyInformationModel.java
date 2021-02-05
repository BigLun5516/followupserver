package com.epic.followup.model.app;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "patient_binformation")
@EntityListeners(AuditingEntityListener.class)
public class PatientBodyInformationModel {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT 'patient_binformation主键'")
    private Long id;

    @Column(name = "pain", nullable = true)
    private String pain;

    @Column(name = "appetite", nullable = true)
    private String appetite;

    @Column(name = "sleep", nullable = true)
    private String sleep;

    @Column(name = "weight", nullable = true)
    private float weight;

    @Column(name = "datetime", nullable = true)
    private String time;

    @Column(name = "pid", nullable = false)
    private Long pid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPain() {
        return pain;
    }

    public void setPain(String pain) {
        this.pain = pain;
    }

    public String getAppetite() {
        return appetite;
    }

    public void setAppetite(String appetite) {
        this.appetite = appetite;
    }

    public String getSleep() {
        return sleep;
    }

    public void setSleep(String sleep) {
        this.sleep = sleep;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }
}
