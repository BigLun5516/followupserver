package com.epic.followup.repository.followup2;

import com.epic.followup.model.SecondUserModel;
import com.epic.followup.model.followup2.BaseUserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * @author : zx
 * @version V1.0
 */
public interface BaseUserRepository extends JpaRepository<BaseUserModel, Long> {

    Optional<BaseUserModel> findByTel(String tel);

    Optional<BaseUserModel> findByUserId(Long userid);

    /**
     * 总数
     * @return long
     */
    long count();

    /**
     * 获取某个学校的注册用户数
     */
    @Query(value = "SELECT count(*)\n" +
            "FROM aidoctor_baseuser u left join aidoctor_studentinfo s on u.userid = s.userid\n" +
            "where s.university_id = ?1"
            , nativeQuery = true)
    Integer countUserByUniversityId(Integer universityId);








}
