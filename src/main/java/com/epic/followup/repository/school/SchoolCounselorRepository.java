package com.epic.followup.repository.school;

import com.epic.followup.model.school.SchoolCounselorModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SchoolCounselorRepository extends JpaRepository<SchoolCounselorModel, Long> {

    //获取班级的辅导员
    @Query(nativeQuery = true, value ="SELECT * from school_counselor s where s.college = ?1 And s.class LIKE ?2")
    SchoolCounselorModel findByCollegeAndClass(String collegeName,String className);

    Optional<SchoolCounselorModel> findById(Long id);

    //获取辅导员对应的学院和班级
    @Query(nativeQuery = true, value ="SELECT s.college,s.class from school_counselor s where s.id = ?1")
    List findCollegeAndClassBycId(Long id);

    //获取辅导员紧急事件学生列表
    @Query(nativeQuery = true, value ="SELECT * FROM (SELECT a.id,a.c_id,b.name,b.college,b.class,b.stu_number,a.event " +
            "from school_emergency a LEFT JOIN school_student b ON  a.s_id=b.id) c WHERE c.c_id= ?1")
    List findAllEmergency(Long id);
}
