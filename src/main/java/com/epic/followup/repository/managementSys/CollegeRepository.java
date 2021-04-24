package com.epic.followup.repository.managementSys;

import com.epic.followup.model.managementSys.CollegeModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CollegeRepository extends JpaRepository<CollegeModel, Integer>, JpaSpecificationExecutor<CollegeModel> {

        // 查询某个学校各个学院的评测数（answer表）
    @Query(value = "select   c.college_name, count(a.id)\n" +
            "from aidoctor_answer as a, aidoctor_studentinfo as s, management_college as c\n" +
            "where a.userid = s.userid and s.college_id = c.college_id and s.university_id = ?\n" +
            "group by c.college_id\n" +
            "order by count(a.id)  desc\n" +
            "limit 10"
            , nativeQuery = true)
    List<Object> countEvaNumByUniversityId_GroupByCollegeId(Integer universityId);


    // 根据学院姓名和学校姓名查询学院
    @Query(value = "select c.*\n" +
            "from management_college c, management_university u\n" +
            "where c.university_id = u.university_id and c.college_name = ?1 and u.university_name = ?2"
            , nativeQuery = true)
    CollegeModel findCollegeByCollegeNameAndUniversityName(String collegeName, String universityName);

    /**
     * 查询院系 多条件查询
     * @param universityName
     * @param collegeName
     * @param collegeStatus
     * @param startTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT c.college_id, u.university_name, c.college_name, c.college_manager, c.college_phone, c.student_num, c.arrive_num, c.college_status, c.create_time " +
            "FROM management_university u, management_college c\n" +
            "where u.university_id = c.university_id and (u.university_name = ?1 or ?1 = \"\") \n" +
            "and (c.college_name like ?2 or ?2 = \"\") and (c.college_status = ?3 or ?3 = -1) \n" +
            "and (c.create_time >= ?4 or ?4 = \"\") and (c.create_time <= ?5 or ?5 = \"\") " +
            "and (c.university_id = ?6 or ?6 = -1) and (c.college_id = ?7 or ?7 = -1) "
            , countQuery = "SELECT count(*)" +
            "FROM management_university u, management_college c\n" +
            "where u.university_id = c.university_id and (u.university_name = ?1 or ?1 = \"\") \n" +
            "and (c.college_name like ?2 or ?2 = \"\") and (c.college_status = ?3 or ?3 = -1) \n" +
            "and (c.create_time >= ?4 or ?4 = \"\") and (c.create_time <= ?5 or ?5 = \"\") " +
            "and (c.university_id = ?6 or ?6 = -1) and (c.college_id = ?7 or ?7 = -1) "
            , nativeQuery = true)
    List<Object> findCollegeModel(String universityName, String collegeName, Integer collegeStatus
            , String startTime, String endTime, Integer userUniversityId, Integer userCollegeId, Pageable pageable);

    // 获取满足条件的院系数量
    @Query(value = "SELECT count(*)" +
            "FROM management_university u, management_college c\n" +
            "where u.university_id = c.university_id and (u.university_name = ?1 or ?1 = \"\") \n" +
            "and (c.college_name like ?2 or ?2 = \"\") and (c.college_status = ?3 or ?3 = -1) \n" +
            "and (c.create_time >= ?4 or ?4 = \"\") and (c.create_time <= ?5 or ?5 = \"\") " +
            "and (c.university_id = ?6 or ?6 = -1) and (c.college_id = ?7 or ?7 = -1) "
            , nativeQuery = true)
    Integer countCollegeModel(String universityName, String collegeName, Integer collegeStatus
            , String startTime, String endTime, Integer userUniversityId, Integer userCollegeId);

    List<CollegeModel> findByUniversityId(Integer uid);

    CollegeModel findByCollegeId(Integer cid);
}
