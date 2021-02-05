package com.epic.followup.repository.followup2.student;

import com.epic.followup.model.followup2.student.StudentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
}
