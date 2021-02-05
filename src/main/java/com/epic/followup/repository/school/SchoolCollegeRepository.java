package com.epic.followup.repository.school;

import com.epic.followup.model.school.SchoolCollegeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SchoolCollegeRepository extends JpaRepository<SchoolCollegeModel, Long> {

    //获取学院详细信息
    SchoolCollegeModel findByName(String name);

    //获取学院的班级列表
    @Query(nativeQuery = true, value ="SELECT name from school_class s where s.college = ?1")
    List<String> findByCollegeName(String cName);
}
