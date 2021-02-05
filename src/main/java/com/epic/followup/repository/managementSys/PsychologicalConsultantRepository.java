package com.epic.followup.repository.managementSys;

import com.epic.followup.model.managementSys.PsychologicalConsultantModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PsychologicalConsultantRepository extends JpaRepository<PsychologicalConsultantModel, Integer> {

    // 根据心理咨询中心id查询心理咨询师数量
    @Query(nativeQuery = true, value = "SELECT COUNT(mpc.consultant_id) FROM management_paychological_consultant mpc WHERE mpc.center_id = ?1")
    Integer getConsultantNum(Integer centerId);

    // 根据中心id查询咨询师
    List<PsychologicalConsultantModel> findByCenterId(Integer centerId);
}
