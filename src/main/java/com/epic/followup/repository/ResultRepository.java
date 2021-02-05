package com.epic.followup.repository;

import com.epic.followup.model.ResultModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author : zx
 * @version V1.0
 */

@Repository
public interface ResultRepository extends JpaRepository<ResultModel, Long> {

}
