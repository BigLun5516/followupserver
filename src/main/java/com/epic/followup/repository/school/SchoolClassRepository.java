package com.epic.followup.repository.school;

import com.epic.followup.model.school.SchoolClassModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolClassRepository extends JpaRepository<SchoolClassModel, Long> {

    //获取某个班级的详细信息
    SchoolClassModel findByCollegeAndName(String college,String name);
}
