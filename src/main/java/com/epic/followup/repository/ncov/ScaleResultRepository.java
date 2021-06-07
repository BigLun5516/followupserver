package com.epic.followup.repository.ncov;

import com.epic.followup.model.ncov.NCovResultModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author : zx
 * @version V1.0
 */
public interface ScaleResultRepository extends JpaRepository<NCovResultModel, Long> {

    /*
     * 查询最近一次成功的测评
     */
    @Query(nativeQuery = true, value = "SELECT * FROM ncov_result a " +
            "WHERE openid = ?1 AND scale_id = ?2 " +
            "ORDER BY answer_time DESC LIMIT 1")
    List<NCovResultModel> getLastScaleByOpenIdAndScaleId(String openid, int scaleId);

    /**
     * 根据学校id 查询aidoctor_ncov_result表中的评测数
     * @param universityId
     * @return
     */
    @Query(value = "SELECT count(*)\n" +
            "FROM aidoctor_ncov_result r left join aidoctor_studentinfo s on r.userid = s.userid\n" +
            "where s.university_id = ?1  and (s.stype = ?2 or ?2 = '') and (s.college_id = ?3 or ?3 = '')\n" +
            "    and (r.answer_time between ?4 and ?5)"
            , nativeQuery = true)
    Integer countEvaluationFromNcovresultByUniversityId(Integer universityId, String sType, String collegeID, String startDate, String endDate);
}
