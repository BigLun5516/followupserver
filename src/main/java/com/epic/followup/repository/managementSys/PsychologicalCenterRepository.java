package com.epic.followup.repository.managementSys;

import com.epic.followup.model.managementSys.PsychologicalCenterModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 心理咨询中心
 */
public interface PsychologicalCenterRepository extends JpaRepository<PsychologicalCenterModel, Integer> {

    PsychologicalCenterModel findByCenterId(Integer id);

    /**
     * 根据查询条件查询
     */
    @Query(value = "SELECT c.center_id, u.university_name, c.center_name, c.center_manager, c.tel, c.total_num, c.accept_num, c.status, c.create_time\n" +
            "from management_university u, management_psychological_center c\n" +
            "where u.center_id = c.center_id and (u.university_name = ?1 or ?1 = \"\") " +
            "and (c.center_name like ?2 or ?2 = \"\") and (c.status = ?3 or ?3 = -1) " +
            "and (c.create_time >= ?4 or ?4 = \"\") and (c.create_time <= ?5 or ?5 = \"\")"
            , countQuery = "SELECT count(*) " +
            "from management_university u, management_psychological_center c\n" +
            "where u.center_id = c.center_id and (u.university_name = ?1 or ?1 = \"\") " +
            "and (c.center_name like ?2 or ?2 = \"\") and (c.status = ?3 or ?3 = -1) " +
            "and (c.create_time >= ?4 or ?4 = \"\") and (c.create_time <= ?5 or ?5 = \"\")"
            , nativeQuery = true)
    List<Object> findPsychologicalCenterModel(String universityName, String centerName, Integer centerStatus, String startTime, String endTime, Pageable pageable);
}
