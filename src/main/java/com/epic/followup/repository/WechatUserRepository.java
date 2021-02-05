package com.epic.followup.repository;

import com.epic.followup.model.WechatUserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author : zx
 * @version V1.0
 */

@Repository
public interface WechatUserRepository extends JpaRepository<WechatUserModel, Long> {

    // 按照unionid查找
    Optional<WechatUserModel> findByOpenId(String unionid);

    // 按照id查找
    Optional<WechatUserModel> findByUserId(Long userId);
}
