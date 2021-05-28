package com.epic.followup.repository.app;

import com.epic.followup.model.app.MiniScaleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MiniScaleRepository extends JpaRepository<MiniScaleModel, Long> {

    /**
     * 根据学校id 查询mini_scale表中的评测数
     * @param universityId
     * @return
     */
    @Query(value = "SELECT count(*) \n" +
            "FROM mini_scale m left join aidoctor_studentinfo s on m.userid = s.userid\n" +
            "where s.university_id = ?1\n" +
            "    and (m.mini_time between ?2 and ?3)"
            , nativeQuery = true)
    Integer countEvaluationFromMiniscaleByUniversityId(Integer universityId, String startDate, String endDate);

}
