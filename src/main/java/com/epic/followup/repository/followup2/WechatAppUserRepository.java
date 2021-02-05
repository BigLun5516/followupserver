package com.epic.followup.repository.followup2;

import com.epic.followup.model.WechatUserModel;
import com.epic.followup.model.followup2.WechatAppUserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author : zx
 * @version V1.0
 */

@Repository
public interface WechatAppUserRepository extends JpaRepository<WechatAppUserModel, Long> {

    // 按照unionid查找
    Optional<WechatAppUserModel> findByOpenId(String unionid);

    // 按照id查找
    Optional<WechatAppUserModel> findByUserId(Long userId);
}
