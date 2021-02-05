package com.epic.followup.repository.followup2.student;

import com.epic.followup.model.followup2.student.StudentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author : zx
 * @version V1.0
 */
@Repository
public interface StudentInfoRepository extends JpaRepository<StudentInfo, Long> {

    Optional<StudentInfo> findByUserId(Long aLong);

    // 通过学号和学校查找学生信息
    Optional<StudentInfo> findByStidAndDepartment(String stid, String department);

    // 高校中各个学院的评测人次排名
    @Query(nativeQuery = true, value = "SELECT a.college, COUNT(a.userid) FROM aidoctor_studentinfo a " +
            "WHERE a.department = ?1 GROUP BY a.college ORDER BY COUNT(a.userid) DESC")
    List<Object> getCollegeUserCount(String name);

    List<StudentInfo> findByDepartment(String department);

    // 根据学校和性别统计人数
    Integer countByUniversityIdAndGender(Integer universityId, String gender);

    // 根据学院id和性别统计学生人数
    Integer countByCollegeIdAndGender(Integer collegeId, String gender);

    // 从List userId中获取学院的List
    @Query(nativeQuery = true, value = "SELECT a.userid FROM aidoctor_studentinfo a WHERE a.college = ?1 AND a.userid IN ?2")
    List<Long> getListUserIdCollege(String department, List<Long> userId);
}
