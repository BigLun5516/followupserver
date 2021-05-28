package com.epic.followup.repository.followup2.student;

import com.epic.followup.model.followup2.student.StudentInfo;
import org.springframework.data.domain.Pageable;
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

    // 学生管理中根据条件查询
    @Query(value = "select *\n" +
            "from aidoctor_studentinfo s\n" +
            "where (s.stname like ?1 or ?1 = '') and (s.department = ?2 or ?2 = '') " +
            "and (s.stype = ?3 or ?3 = -1) and (s.create_time >= ?4 or ?4 = '') " +
            "and (s.create_time <= ?5 or ?5 = '') and (s.university_id = ?6 or ?6 = -1) " +
            "and (s.college_id in ?7 or -1 in ?7)"
            , countQuery = "select count(*)\n" +
            "from aidoctor_studentinfo s\n" +
            "where (s.stname like ?1 or ?1 = '') and (s.department = ?2 or ?2 = '') " +
            "and (s.stype = ?3 or ?3 = -1) and (s.create_time >= ?4 or ?4 = '') " +
            "and (s.create_time <= ?5 or ?5 = '') and (s.university_id = ?6 or ?6 = -1) " +
            "and (s.college_id in ?7 or -1 in ?7)"
            , nativeQuery = true)
    List<StudentInfo> findStudentInfo(String studentName, String universityName, Integer studenType
            , String StartTime, String endTime, Integer userUniversityId, List<Integer> collegeIdList, Pageable pageable);

    // 获取满足条件的学生数量
    @Query(value = "select count(*)\n" +
            "from aidoctor_studentinfo s\n" +
            "where (s.stname like ?1 or ?1 = '') and (s.department = ?2 or ?2 = '') " +
            "and (s.stype = ?3 or ?3 = -1) and (s.create_time >= ?4 or ?4 = '') " +
            "and (s.create_time <= ?5 or ?5 = '') and (s.university_id = ?6 or ?6 = -1) " +
            "and (s.college_id in ?7 or -1 in ?7)"
            , nativeQuery = true)
    Integer countStudentInfo(String studentName, String universityName, Integer studenType
            , String StartTime, String endTime, Integer userUniversityId, List<Integer> collegeIdList);

    // 获取指定学校的学生生源地分布
    @Query(value = "SELECT province, count(*) \n" +
            "FROM aidoctor_studentinfo\n" +
            "where university_id = ?1\n" +
            "group by province"
            , nativeQuery = true)
    List<Object> countProvinceByUniversityId(Integer universityId);

    // 获取指定学校的学生性别分布
    @Query(value = "SELECT gender, count(*) \n" +
            "FROM aidoctor_studentinfo\n" +
            "where university_id = ?1\n" +
            "group by gender"
            , nativeQuery = true)
    List<Object> countGenderByUniversityId(Integer universityId);

    // 获取一些学院的学生人数分布
    @Query(value = "SELECT college_id, college, count(*) \n" +
            "FROM aidoctor_studentinfo\n" +
            "where university_id = ?1\n" +
            "group by college_id, college"
            , nativeQuery = true)
    List<Object> countCollegeStuNumByUniversityId(Integer universityId);

    // 获取指定学院的学生生源地分布
    @Query(value = "SELECT province, count(*) \n" +
            "FROM aidoctor_studentinfo\n" +
            "where college_id = ?1\n" +
            "group by province"
            , nativeQuery = true)
    List<Object> countProvinceByCollegeId(Integer collegeId);

    // 获取指定学院的学生性别分布
    @Query(value = "SELECT gender, count(*) \n" +
            "FROM aidoctor_studentinfo\n" +
            "where college_id = ?1\n" +
            "group by gender"
            , nativeQuery = true)
    List<Object> countGenderByCollegeId(Integer collegeId);


}
