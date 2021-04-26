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
    @Query(value = "select s.scale_id, scale_name, s.isnz, f.id is null, s.school_name\n" +
            "from management_scale s left join management_scale_forbidden f on s.scale_id = f.scale_id \n" +
            "left join management_university u on s.school_name = u.university_name\n" +
            "where s.scale_name like %?1% " +
            "   and (s.school_name = ?2 or ?2 = '' or s.school_name = '' or s.school_name is null)\n" +
            "   and (s.isnz = ?3 or ?3 = -1) " +
            "   and ((0 = ?4 and f.id is not null ) or (1 = ?4 and  f.id is null) or ?4 = -1)\n" +
            "   and (u.university_id = ?5 or ?5 = -1 or u.university_id is null) \n", nativeQuery = true)
    List<Object> findScaleByQuery(String scaleName, String schoolName, int isnz, int status, Integer userUniversityId);

    // 根据条Id查询
    Optional<ScaleModel> findByScaleId(Long scaleId);

    // 保存量表
    <S extends ScaleModel> S save(S scale);

    //根据学校id获取对应的量表
    @Query(value = "SELECT a.scale_id,a.classify,a.scale_name from management_scale a LEFT JOIN management_university b ON " +
            "a.school_name=b.university_name where a.isnz=1 or b.university_id=?1", nativeQuery = true)
    List<Object> findByUid(Integer userUniversityId);


}
