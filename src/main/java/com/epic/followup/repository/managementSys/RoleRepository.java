package com.epic.followup.repository.managementSys;

import com.epic.followup.model.managementSys.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleRepository extends JpaRepository<RoleModel, Long> {

    @Query(nativeQuery = true, value = "SELECT a.id,a.`name`,a.remark,a.limit1,a.limit2,b.university_name,b.university_id as uid " +
            "FROM `management_role` a LEFT JOIN management_university b ON a.university_id=b.university_id WHERE b.university_id=?1 or ?1=-1;")
    List<Object> findAllByUid(Integer universityId);

}
