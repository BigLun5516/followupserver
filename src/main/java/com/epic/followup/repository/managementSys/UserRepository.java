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

    @Query(nativeQuery = true, value = "SELECT * from (SELECT a.id,a.image_url,a.`password`,a.tel,a.user_name,a.user_type,c.`name` as role_name,c.limit1,d.university_id,d.college_id,a.visible " +
            "from management_user a LEFT JOIN management_role c on a.user_type=c.id LEFT JOIN management_data_permission d ON a.id= d.user_id) m WHERE m.tel=?1")
    List<Object> getUserByTel(String Tel);

    @Query(nativeQuery = true, value = " SELECT a.id,a.image_url,a.`password`,a.tel,a.user_name,any_value(b.university_name) " +
            "university,c.`name` as role_name ,GROUP_CONCAT(d.college_name) college from management_user a LEFT JOIN " +
            "management_data_permission p ON  a.id=p.user_id  LEFT JOIN management_university b " +
            "ON p.university_id=b.university_id LEFT JOIN management_role c on a.user_type=c.id " +
            "LEFT JOIN management_college d on p.college_id=d.college_id WHERE p.university_id=?1 or ?1=-1 GROUP BY a.id")
    List<Object> getAllUser(Integer universityId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = " UPDATE management_user a SET a.user_type = -1 WHERE a.user_type = ?1")
    void upDateUserType(Long id);

    List<UserModel> findByUserType(Long userType);


    @Query(nativeQuery = true, value = "SELECT a.id,a.image_url,a.`password`,a.tel,a.user_name,any_value(b.university_name) university,c.`name` as role_name " +
            ",GROUP_CONCAT(d.college_name) college from management_user a LEFT JOIN management_data_permission p " +
            "ON  a.id=p.user_id  LEFT JOIN management_university b ON p.university_id=b.university_id " +
            "LEFT JOIN management_role c on a.user_type=c.id LEFT JOIN management_college d on p.college_id=d.college_id " +
            "WHERE a.id=?1 GROUP BY a.id")
    Object getUserInfoById(Long id);

    @Query(nativeQuery = true, value = "SELECT b.stname,b.stid,b.age,b.department,b.college,b.stype,b.year," +
            "a.mini_time,a.mini_result FROM `mini_scale` a LEFT JOIN aidoctor_studentinfo b ON a.userid=" +
            "b.userid " +
            "where (b.university_id = ?1 or ?1 = -1) and (b.college_id in ?2 or -1 in ?2) and (b.stid = ?3 or ?3 = '') ORDER BY a.mini_time DESC")
    List<Object> getMiniResult(Integer userUniversityId, List<Integer>  userCollegeIdList, String stid);


    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = " DELETE FROM management_data_permission a WHERE a.user_id = ?1")
    void deletePermission(Long id);

    @Query(nativeQuery = true, value = "select b.department,s.college,b.stid,b.tel,s.province,s.stname,s.stype,s.`year`,b.create_time " +
            "FROM aidoctor_baseuser b LEFT JOIN aidoctor_studentinfo s ON b.userid=s.userid where s.university_id=?1 ORDER BY b.create_time ")
    List<Object> getRegisterUser(Integer userUniversityId);


}
