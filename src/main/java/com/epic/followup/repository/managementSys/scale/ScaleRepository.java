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
    @Query(value = "select s.scale_id, s.scale_name, s.school_name, s.classify, s.isnz, s.status, s.num, s.organization from management_scale s where s.scale_name like %?1% " +
            "and s.school_name like ?2 and s.classify like ?3 " +
            "and (s.isnz = ?4 or ?4 = -1) and (s.status = ?5 or ?5 = -1) ", nativeQuery = true)
    List<Object> findScaleByQuery(String scaleName, String schoolName, String classify, int isnz, int status);

    // 根据条Id查询
    Optional<ScaleModel> findByScaleId(Long scaleId);

    // 保存量表
    <S extends ScaleModel> S save(S scale);

}
