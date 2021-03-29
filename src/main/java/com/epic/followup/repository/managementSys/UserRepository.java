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
            "a.user_name,a.university_id,a.user_type,c.`name` as role_name,c.limit1,a.college_id   " +
            "from management_user a LEFT JOIN management_role c on a.user_type=c.id ) m WHERE m.tel=?1")
    Object getUserByTel(String Tel);

    @Query(nativeQuery = true, value = " SELECT a.id,a.image_url,a.`password`,a.tel,a.user_name,a.university_id,a.user_type,b.university_name," +
            "c.`name` as role_name ,a.college_id,d.college_name from management_user a LEFT JOIN management_university b ON a.university_id=b.university_id " +
            "LEFT JOIN management_role c on a.user_type=c.id LEFT JOIN management_college d on a.college_id=d.college_id WHERE a.university_id=?1 or ?1=-1")
    List<Object> getAllUser(Integer universityId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = " UPDATE management_user a SET a.user_type = -1 WHERE a.user_type = ?1")
    void upDateUserType(Long id);


    @Query(nativeQuery = true, value = "SELECT * from (SELECT a.id,a.image_url,a.`password`,a.tel,a.user_name,a.university_id,a.user_type," +
            "b.university_name,m.college_name ,c.`name` as role_name from management_user a LEFT JOIN management_university b " +
            "ON a.university_id=b.university_id LEFT JOIN management_role c on a.user_type=c.id LEFT JOIN management_college m " +
            "ON a.college_id=m.college_id ) s where s.tel=?1")
    Object getUserInfoByTel(String Tel);

    @Query(nativeQuery = true, value = "SELECT b.stname,b.stid,b.age,b.department,b.college,b.stype,b.year," +
            "a.mini_time,a.mini_result FROM `mini_scale` a LEFT JOIN aidoctor_studentinfo b ON a.userid=" +
            "b.userid ;")
    List<Object> getMiniResult();


}
