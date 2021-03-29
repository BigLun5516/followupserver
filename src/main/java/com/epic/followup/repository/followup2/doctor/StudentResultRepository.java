package com.epic.followup.repository.followup2.doctor;

import com.epic.followup.model.followup2.doctor.StudentResultModel;
import com.epic.followup.model.followup2.student.WechatFileModel;
import com.mysql.cj.result.Row;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author : zx
 * @version V1.0
 */

@Repository
public interface StudentResultRepository extends JpaRepository<StudentResultModel, Long> {

    Optional<StudentResultModel> findByUserId(Long userId);

    long countByDepartment(String department);

    /**
     * 获取指定学院所有学生数据
     */
    List<StudentResultModel> findAllByDepartmentAndCollege(String department, String college);

    /**
     * 获取指定学校最近的几条
     */
    @Query(nativeQuery = true, value = "SELECT * FROM aidoctor_studentresult a " +
            "WHERE a.department = ?1 " +
            "ORDER BY a.update_time DESC LIMIT 3")
    List<StudentResultModel> findLast(String department);

    /**
     * 获取指定学校、学院的所有数据
     */
    @Query(nativeQuery = true, value = "SELECT * FROM aidoctor_studentresult a " +
            "WHERE a.department = ?1 AND a.college = ?2 " +
            "ORDER BY a.update_time DESC")
    List<StudentResultModel> findListByDepartmentAndCollege(String department, String college);

    Optional<StudentResultModel> findOneByDepartmentAndStid(String department, String stid);

    /**
     * 获取指定学校、指定年级的抑郁分布
     * @return List<row> level num
     */
    @Query(nativeQuery = true, value = "SELECT a.level, COUNT(*) as num FROM aidoctor_studentresult a " +
            "WHERE a.department = ?1 AND a.year = ?2 " +
            "GROUP BY a.level")
    List findCountByDepartmentAndYear(String department, String year);

    /**
     * 获取指定学校、不同年级的抑郁分布
     * @return List<row> level num
     */
    @Query(nativeQuery = true, value = "SELECT a.year, a.level, COUNT(*) as num FROM aidoctor_studentresult a " +
            "WHERE a.department = ?1 " +
            "GROUP BY a.level, a.year")
    List findCountByDepartmentAndYear(String department);

    /**
     * 获取指定学校、日期，的抑郁分布
     * @return List<row> level num
     */
    @Query(nativeQuery = true, value = "SELECT a.level, COUNT(*) as num FROM aidoctor_studentresult a " +
            "WHERE a.department = ?1 AND a.update_time  BETWEEN ?2 AND ?3 " +
            "GROUP BY a.level")
    List findCountByDepartmentAndTime(String department, Date start, Date end);

    /**
     * 获取指定学校、学院，省份的抑郁分布
     * @return List<row> province level num
     */
    @Query(nativeQuery = true, value = "SELECT a.province, a.level, COUNT(*) as num FROM aidoctor_studentresult a " +
            "WHERE a.department = ?1 " +
            "GROUP BY a.level, a.province")
    List findProvinceCountsByDepartment(String department);

    /**
     * 获取指定学校、学院，省份的抑郁分布
     * @return List<row> province level num
     */
    @Query(nativeQuery = true, value = "SELECT a.province, a.level, COUNT(*) as num FROM aidoctor_studentresult a " +
            "WHERE a.department = ?1 AND a.college = ?2 " +
            "GROUP BY a.level, a.province")
    List findProvinceCountsByDepartmentAAndCollege(String department, String college);

    /**
     * 获取指定学校，学历的抑郁分布
     * @return List<row> province level num
     */
    @Query(nativeQuery = true, value = "SELECT a.stype, a.level, COUNT(*) as num FROM aidoctor_studentresult a " +
            "WHERE a.department = ?1 " +
            "GROUP BY a.level, a.stype")
    List findStypeCountsByDepartment(String department);

    /**
     * 获取指定学校、学院，学历的抑郁分布
     * @return List<row> province level num
     */
    @Query(nativeQuery = true, value = "SELECT a.stype, a.level, COUNT(*) as num FROM aidoctor_studentresult a " +
            "WHERE a.department = ?1 AND a.college = ?2 " +
            "GROUP BY a.level, a.stype")
    List findStypeCountsByDepartmentAAndCollege(String department, String college);

    /**
     * 获取学校，性别的抑郁分布
     * @return List<row> gender level num
     */
    @Query(nativeQuery = true, value = "SELECT a.gender, a.level, COUNT(*) as num FROM aidoctor_studentresult a " +
            "WHERE a.department = ?1 " +
            "GROUP BY a.level, a.gender")
    List findGenderCountsByDepartment(String department);

    /**
     * 获取学校，年龄的抑郁分布
     * @return List<row> age level num
     */
    @Query(nativeQuery = true, value = "SELECT a.age, a.level, COUNT(*) as num FROM aidoctor_studentresult a " +
            "WHERE a.department = ?1 " +
            "GROUP BY a.level, a.age")
    List findAgeCountsByDepartment(String department);

    /**
     * 获取学校、学院，年龄的抑郁分布
     * @return List<row> age level num
     */
    @Query(nativeQuery = true, value = "SELECT a.age, a.level, COUNT(*) as num FROM aidoctor_studentresult a " +
            "WHERE a.department = ?1 AND a.college = ?2 " +
            "GROUP BY a.level, a.age")
    List findAgeCountsByDepartmentAndCollege(String department, String college);

    /**
     * 获取学校，人数的抑郁分布
     * @return List<row> age level num
     */
    @Query(nativeQuery = true, value = "SELECT a.level, COUNT(*) as num FROM aidoctor_studentresult a " +
            "WHERE a.department = ?1 " +
            "GROUP BY a.level")
    List findAllCountsByDepartment(String department);

    /**
     * 获取学校、学院，人数的抑郁分布
     * @return List<row> age level num
     */
    @Query(nativeQuery = true, value = "SELECT a.level, COUNT(*) as num FROM aidoctor_studentresult a " +
            "WHERE a.department = ?1 AND a.college = ?2 " +
            "GROUP BY a.level")
    List findAllCountsByDepartmentAAndCollege(String department, String college);

    @Query(nativeQuery = true, value = "SELECT * FROM `aidoctor_studentresult` a, aidoctor_studentinfo s " +
            "WHERE a.userid = s.userid and (a.department= ?1 or ?1 = '') And (a.level= ?2 or ?2 = '') " +
            "and (s.university_id = ?3 or ?3 = -1)")
    List<StudentResultModel> findbyUniversityAndIllness(String university,String illness, Integer userUniversityId);


}
