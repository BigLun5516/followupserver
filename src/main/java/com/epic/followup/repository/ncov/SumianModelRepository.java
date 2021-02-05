package com.epic.followup.repository.ncov;

import com.epic.followup.model.sumian.SumianOrderModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author : zx
 * @version V1.0
 */

@Repository
public interface SumianModelRepository extends JpaRepository<SumianOrderModel, Long> {

    Optional<SumianOrderModel> findByUserIdAndIsSucceed(Long UserId, int is);
}
