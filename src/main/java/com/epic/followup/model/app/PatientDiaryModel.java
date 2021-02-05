package com.epic.followup.model.app;


import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "patient_diary")
@EntityListeners(AuditingEntityListener.class)
public class PatientDiaryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT 'patient_diary主键'")
    private Long Id;

    // 用户心情
    @Column(name = "mood", length = 128, nullable = true)
    private String mood;

    // 时间戳
    @Column(name = "time",  length = 13,nullable = true)
    private String time;

    // 日及简介
    @Column(name = "brief", length = 255, nullable = true)
    private String brief;

    // 日记内容
    @Column(name = "html",  nullable = true)
    private String html;

    // 图像URL
    @Column(name = "img", length = 256, nullable = true)
    private String img;

    // 电话号码
    @Column(name = "tel", length = 11, nullable = true)
    private String tel;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
