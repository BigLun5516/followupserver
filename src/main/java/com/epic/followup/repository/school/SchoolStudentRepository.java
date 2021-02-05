package com.epic.followup.repository.school;

import com.epic.followup.model.school.SchoolStudentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SchoolStudentRepository extends JpaRepository<SchoolStudentModel, Long> {

    @Query(nativeQuery = true, value ="SELECT * from school_student s where s.college = ?1 AND s.class = ?2")
    List<SchoolStudentModel> findByCollegeAndClass(String college,String name);

    @Query(nativeQuery = true, value ="SELECT * from school_student s ORDER BY s.sdate")
    List<SchoolStudentModel> findAllStuByOrder();

    @Query(nativeQuery = true, value ="SELECT * from school_student s WHERE s.name = ?1 AND s.birth > ?2 AND s.birth < ?3")
    List<SchoolStudentModel> findByQuery1(String name,String min_date,String max_date);

    @Query(nativeQuery = true, value ="SELECT * from school_student s WHERE s.birth > ?1 AND s.birth < ?2")
    List<SchoolStudentModel> findByQuery2(String min_date,String max_date);

    Optional<SchoolStudentModel> findById(Long id);

    //获取班级下对应的学生列表（id和姓名）
    @Query(nativeQuery = true, value ="SELECT s.id,s.name from school_student s WHERE s.college = ?1 AND s.class = ?2")
    List findStuByClassAndCollege(String college,String bclass);
}
