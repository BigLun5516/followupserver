package com.epic.followup.model.app;


import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "doctor_schedule")
@EntityListeners(AuditingEntityListener.class)
public class DoctorScheduleModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT 'doctor_schedule主键'")
    private Long Id;

    @Column(name = "time", length = 128, nullable = false)
    private String time;

    @Column(name = "morning", length = 128, nullable = true)
    private String morning;

    @Column(name = "afternoon", length = 128, nullable = true)
    private String afternoon;

    @Column(name = "evening", length = 128, nullable = true)
    private String evening;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMorning() {
        return morning;
    }

    public void setMorning(String morning) {
        this.morning = morning;
    }

    public String getAfternoon() {
        return afternoon;
    }

    public void setAfternoon(String afternoon) {
        this.afternoon = afternoon;
    }

    public String getEvening() {
        return evening;
    }

    public void setEvening(String evening) {
        this.evening = evening;
    }
}
