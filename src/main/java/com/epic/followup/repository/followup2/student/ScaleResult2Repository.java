package com.epic.followup.repository.followup2.student;

import com.epic.followup.model.followup2.student.NCovResultModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author : zx
 * @version V1.0
 */
public interface ScaleResult2Repository extends JpaRepository<NCovResultModel, Long> {

    /*
     * 查询最近一次成功的测评
     */
    @Query(nativeQuery = true, value = "SELECT * FROM aidoctor_ncov_result a " +
            "WHERE userid = ?1 AND scale_id = ?2 " +
            "ORDER BY answer_time DESC LIMIT 1")
    List<NCovResultModel> getLastScaleByOpenIdAndScaleId(Long userId, int scaleId);


    /**
     * 查询最近100天测评成功的历史时间
     * @param userId
     * @return
     */
    @Query(nativeQuery = true, value = "SELECT DATE_FORMAT(answer_time,'%Y-%m-%d') tt FROM aidoctor_ncov_result " +
            "WHERE userid = ?1  AND scale_id <= 4   " +
            "GROUP BY tt ORDER BY tt DESC LIMIT 100")
    List<String> getLastScaleByUserId(Long userId);



    @Query(nativeQuery = true, value = "SELECT * FROM aidoctor_ncov_result a " +
            "WHERE userid = ?1 AND scale_id = ?2 AND DATE(answer_time) = ?3")
    List<NCovResultModel> getResultByDate(Long userId, int scaleId, String date);
}
