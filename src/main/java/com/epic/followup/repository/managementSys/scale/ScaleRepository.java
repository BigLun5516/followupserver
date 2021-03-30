package com.epic.followup.repository.managementSys.scale;

import com.epic.followup.model.managementSys.scale.ScaleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ScaleRepository extends JpaRepository<ScaleModel, Long> {

    /**
     * 量表列表
     */

    // 根据条件查询
    @Query(value = "select s.scale_id, s.scale_name, s.school_name, s.classify, s.isnz, s.status, s.num, s.organization " +
            "from management_scale s, management_university u " +
            "where s.school_name = u.university_name and s.scale_name like %?1% " +
            "and s.school_name like ?2 and s.classify like ?3 " +
            "and (s.isnz = ?4 or ?4 = -1) and (s.status = ?5 or ?5 = -1) " +
            "and (u.university_id = ?6 or ?6 = -1)", nativeQuery = true)
    List<Object> findScaleByQuery(String scaleName, String schoolName, String classify, int isnz, int status, Integer userUniversityId);

    // 根据条Id查询
    Optional<ScaleModel> findByScaleId(Long scaleId);

    // 保存量表
    <S extends ScaleModel> S save(S scale);

    //根据学校id获取对应的量表
    @Query(value = "SELECT a.scale_id,a.classify,a.scale_name from management_scale a LEFT JOIN management_university b ON " +
            "a.school_name=b.university_name where a.isnz=1 or b.university_id=?1", nativeQuery = true)
    List<Object> findByUid(Integer userUniversityId);


}
