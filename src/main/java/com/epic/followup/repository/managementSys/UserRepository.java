package com.epic.followup.repository.managementSys;

import com.epic.followup.model.managementSys.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {

    Optional<UserModel> findByTel(String Tel);

}
