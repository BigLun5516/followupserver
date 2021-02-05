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
}
