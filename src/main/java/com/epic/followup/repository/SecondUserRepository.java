package com.epic.followup.repository;

import com.epic.followup.model.SecondUserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author : zx
 * @version V1.0
 */
public interface SecondUserRepository extends JpaRepository<SecondUserModel, Long> {

    SecondUserModel findByUserNameAndPassword(String username, String password);

    Optional<SecondUserModel> findByUserName(String username);
}
