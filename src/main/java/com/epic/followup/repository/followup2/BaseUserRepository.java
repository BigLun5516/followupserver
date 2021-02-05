package com.epic.followup.repository.followup2;

import com.epic.followup.model.SecondUserModel;
import com.epic.followup.model.followup2.BaseUserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author : zx
 * @version V1.0
 */
public interface BaseUserRepository extends JpaRepository<BaseUserModel, Long> {

    Optional<BaseUserModel> findByTel(String tel);

    /**
     * 总数
     * @return long
     */
    long count();
}
