package com.epic.followup.repository.managementSys;

import com.epic.followup.model.managementSys.CollegeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CollegeRepository extends JpaRepository<CollegeModel, Integer> {

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
}
