package com.epic.followup.repository.school;

import com.epic.followup.model.school.SchoolUserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SchoolUserRepository extends JpaRepository<SchoolUserModel, Long> {

    //登录要用，用户名不重复
    SchoolUserModel findByUsername(String username);

    //根据电话号码查找相应user表中相应的id（用于修改密码）
    @Query(nativeQuery = true, value = "SELECT s.id FROM (SELECT u.id,u.username,u.password, CASE WHEN u.type=1 " +
            "THEN (SELECT c.phone FROM school_counselor c WHERE c.id=u.contact_id) WHEN u.type=2 " +
            "THEN (SELECT p.phone FROM school_psychologist p WHERE p.id=u.contact_id) END phone  from school_user u) s " +
            "WHERE s.phone= ?1")
    Long findByTel(String phone);

    //根据教师编号查找相应user表中相应的密码（用于登录）
    @Query(nativeQuery = true, value = "SELECT s.id,s.password,s.type,s.contact_id " +
            "FROM (SELECT u.id,u.password,u.type,u.contact_id, CASE WHEN u.type=0 " +
            "THEN (SELECT c.username FROM school_user c WHERE c.contact_id=0) " +
            "WHEN u.type=1 THEN (SELECT c.tch_number FROM school_counselor c WHERE c.id=u.contact_id) " +
            "WHEN u.type=2 THEN (SELECT p.tch_number FROM school_psychologist p WHERE p.id=u.contact_id) " +
            "END tch_number  from school_user u) s WHERE s.tch_number= ?1")
    List findBytNumber(String num);

    Optional<SchoolUserModel> findById(Long id);

    //获取所有用户的（姓名，教师编号，密码）用于管理员展示所有用户
    @Query(nativeQuery = true, value = "SELECT s.id,s.name,s.tch_number,s.password " +
            "FROM (SELECT u.id,u.password, CASE WHEN u.type=1 " +
            "THEN (SELECT c.tch_number FROM school_counselor c WHERE c.id=u.contact_id) " +
            "WHEN u.type=2 THEN (SELECT p.tch_number FROM school_psychologist p WHERE p.id=u.contact_id) " +
            "END tch_number ,CASE WHEN u.type=1 THEN (SELECT c.name FROM school_counselor c WHERE c.id=u.contact_id) " +
            "WHEN u.type=2 THEN (SELECT p.name FROM school_psychologist p WHERE p.id=u.contact_id) END name  " +
            "from school_user u) s WHERE s.id!=1")//不要管理员的信息（只展示用户信息)
    List findAllUser();

    //获取某个用户的（姓名，教师编号，密码）用于管理员展示某个用户
    @Query(nativeQuery = true, value = "SELECT s.id,s.name,s.tch_number,s.password " +
            "FROM (SELECT u.id,u.password, CASE WHEN u.type=1 " +
            "THEN (SELECT c.tch_number FROM school_counselor c WHERE c.id=u.contact_id) " +
            "WHEN u.type=2 THEN (SELECT p.tch_number FROM school_psychologist p WHERE p.id=u.contact_id) " +
            "END tch_number ,CASE WHEN u.type=1 THEN (SELECT c.name FROM school_counselor c WHERE c.id=u.contact_id) " +
            "WHEN u.type=2 THEN (SELECT p.name FROM school_psychologist p WHERE p.id=u.contact_id) END name  " +
            "from school_user u) s WHERE s.id= ?1")//不要管理员的信息（只展示用户信息)
    List findUser(Long id);


}
