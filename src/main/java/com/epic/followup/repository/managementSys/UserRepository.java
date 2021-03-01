package com.epic.followup.repository.managementSys;

import com.epic.followup.model.managementSys.UniversityModel;
import com.epic.followup.model.managementSys.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {

    @Query(nativeQuery = true, value = "  SELECT * from (SELECT a.id,a.image_url,a.`password`,a.tel," +
            "a.user_name,a.university_id,a.user_type,c.`name` as role_name,c.limit1   " +
            "from management_user a LEFT JOIN management_role c on a.user_type=c.id ) m WHERE m.tel=?1")
    Object getUserByTel(String Tel);

    @Query(nativeQuery = true, value = " SELECT a.id,a.image_url,a.`password`,a.tel,a.user_name," +
            "a.university_id,a.user_type,b.university_name,c.`name` as role_name from management_user a " +
            "LEFT JOIN management_university b ON a.university_id=b.university_id LEFT JOIN management_role c on a.user_type=c.id")
    List<Object> getAllUser();

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = " UPDATE management_user a SET a.user_type = -1 WHERE a.user_type = ?1")
    void upDateUserType(Long id);

}
