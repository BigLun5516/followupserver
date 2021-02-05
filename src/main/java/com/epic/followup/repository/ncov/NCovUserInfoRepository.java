package com.epic.followup.repository.ncov;

import com.epic.followup.model.ncov.NCovUserInfoModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author : zx
 * @version V1.0
 */
public interface NCovUserInfoRepository extends JpaRepository<NCovUserInfoModel, Long> {

    Optional<NCovUserInfoModel> findByOpenId(String openId);
}
