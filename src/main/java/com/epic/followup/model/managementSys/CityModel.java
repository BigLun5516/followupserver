package com.epic.followup.model.managementSys;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;


/**
 * 城市表：记录每个城市多个所高校
 */
@Entity
@Data
@ToString
@Table(name = "management_city")
public class CityModel {

    // 城市id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "city_id")
    private Integer cityId;

    // 城市名字
    @Column(name = "city_name", length = 64, nullable = false)
    private String cityName;

    // 城市高校数量
    @Column(name = "university_num", nullable = false)
    private Integer universityNum;
}
