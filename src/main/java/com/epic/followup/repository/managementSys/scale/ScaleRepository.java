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
    @Query(value = "select s.scale_id, scale_name, s.isnz, s.scale_id not in \n" +
            "                                    (\n" +
            "                                        select f.scale_id\n" +
            "                                        from management_scale_forbidden f left join management_university u on f.university_name = u.university_name\n" +
            "                                        where u.university_id = ?5\n" +
            "                                    ) as status, s.school_name\n" +
            "from management_scale s\n" +
            "where s.scale_name like %?1% \n" +
            "   and (s.school_name = ?2 or ?2 = '' or s.school_name = '' or s.school_name is null)\n" +
            "   and (s.isnz = ?3 or ?3 = -1)\n" +
            "   and ((1 = ?4 and s.scale_id not in \n" +
            "                                    (\n" +
            "                                        select f.scale_id\n" +
            "                                        from management_scale_forbidden f left join management_university u on f.university_name = u.university_name\n" +
            "                                        where u.university_id = ?5\n" +
            "                                    ))\n" +
            "                or (0 = ?4 and s.scale_id in \n" +
            "                                    (\n" +
            "                                        select f.scale_id\n" +
            "                                        from management_scale_forbidden f left join management_university u on f.university_name = u.university_name\n" +
            "                                        where u.university_id = ?5\n" +
            "                                    ))\n" +
            "                or -1 = ?4)\n" +
            "   and (?5 = -1 or s.school_name = \n" +
            "                   (\n" +
            "                       select un.university_name\n" +
            "                       from management_university un\n" +
            "                       where un.university_id = ?5\n" +
            "                    )\n" +
            "                or s.school_name = '' or s.school_name is null\n" +
            "        )", nativeQuery = true)
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
