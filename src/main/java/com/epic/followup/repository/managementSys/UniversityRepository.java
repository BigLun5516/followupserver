package com.epic.followup.repository.managementSys;

import com.epic.followup.model.managementSys.UniversityModel;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UniversityRepository extends JpaRepository<UniversityModel, Integer>, JpaSpecificationExecutor<UniversityModel> {

    @Query(nativeQuery = true, value = "SELECT * FROM management_university mu " +
            "ORDER BY university_star DESC ")
    List<UniversityModel> getUniversityModelOrderByStar();

    @Query(nativeQuery = true, value = "SELECT * FROM  management_university mu " +
            "ORDER BY mu.active_num DESC ")
    List<UniversityModel> getUniversityModelOrderByActiveNum();

    // 根据高校名字查询
    UniversityModel findByUniversityName(String name);

    //高校推广
    //今日新增
    @Query(nativeQuery = true, value = " SELECT * FROM management_university "+
            " WHERE DATEDIFF(join_date,NOW()) = 0 " )
    List<UniversityModel> getToAdd();

    //昨日新增
    @Query(nativeQuery = true, value = " SELECT * FROM management_university "+
            " WHERE DATEDIFF(join_date,NOW()) = -1 " )
    List<UniversityModel> getYesAdd();

    //本周新增
    @Query(nativeQuery = true, value = " SELECT * FROM management_university "+
            " where DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= date(join_date) " )
    List<UniversityModel> getWeekAdd();

    //本月新增
    @Query(nativeQuery = true, value = "SELECT * FROM management_university "+
            " where DATE_SUB(CURDATE(), INTERVAL 30 DAY) <= date(join_date) " )
    List<UniversityModel> getMonthAdd();

    //推广覆盖分布图，中国地图数据源
    @Query(nativeQuery = true, value = "select province,count(*) from management_university " +
            "group by province ")
    List<Object> getCityGraphData();

    // 根据心理咨询中心查高校
    List<UniversityModel> findByCenterId(Integer centerid);

    /**
     * 量表
     */
    // 获取所有的学校名称
    @Query(value = "SELECT DISTINCT university_name from management_university", nativeQuery = true)
    List<String> getAllUniversityName();


}