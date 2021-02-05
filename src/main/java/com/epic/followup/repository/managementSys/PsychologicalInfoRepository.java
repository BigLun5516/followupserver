package com.epic.followup.repository.managementSys;

import com.epic.followup.model.managementSys.PsychologicalInfoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * author: zmm
 */
@Repository
public interface PsychologicalInfoRepository extends JpaRepository<PsychologicalInfoModel, Long> {

    @Query(nativeQuery = true, value = "SELECT SUM(i.consultation_time_length) FROM management_psychological_info i ")
    Integer getAllConsultationLength();

    /**
     * 获取高校心理咨询师时长排序
     */
    @Query(nativeQuery = true, value = "SELECT SUM(i.consultation_time_length), i.consultant_id, i.consultant_name " +
            "FROM management_psychological_info i, management_university u " +
            "WHERE i.center_id = u.center_id AND u.university_name = ?1 GROUP BY i.consultant_id, i.consultant_name ORDER BY SUM(i.consultation_time_length) DESC ")
    List<Object> getConsultationTimeList(String universityName);

    /**
     * 获取高校心理咨询总时长
     */
    @Query(nativeQuery = true, value = "SELECT SUM(i.consultation_time_length) " +
            "FROM management_psychological_info i, management_university u " +
            "WHERE i.center_id = u.center_id AND u.university_name = ?1 ")
    Integer getConsultationTimeUni(String universityName);
}
