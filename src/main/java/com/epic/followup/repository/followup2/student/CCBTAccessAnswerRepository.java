package com.epic.followup.repository.followup2.student;

import com.epic.followup.model.followup2.student.CCBTAccessAnswerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;

import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

public interface CCBTAccessAnswerRepository extends JpaRepository<CCBTAccessAnswerModel, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM aidoctor_ccbtaccess_answer" +
            " WHERE userid = ?1 AND  DATE(createtime) = ?2 ")
    List<CCBTAccessAnswerModel> getAnswerByUserIdAndDate(long userId, String date);

    @Query(nativeQuery = true, value = "SELECT `depress_answer`, depress_score,createtime FROM `aidoctor_ccbtaccess_answer`  " +
            "where depress_answer is not null and  userid=?1  ORDER BY createtime desc limit ?2")
    List<Object> getLastestHistory1(Long userid,int num);

    @Query(nativeQuery = true, value = "SELECT `anxious_answer`, anxious_score,createtime FROM `aidoctor_ccbtaccess_answer`  " +
            "where anxious_answer is not null and  userid=?1  ORDER BY createtime desc limit ?2")
    List<Object> getLastestHistory2(Long userid,int num);

    @Query(nativeQuery = true, value = "SELECT `sleep_answer`, sleep_score,createtime FROM `aidoctor_ccbtaccess_answer`  " +
            "where sleep_answer is not null and  userid=?1  ORDER BY createtime desc limit ?2")
    List<Object> getLastestHistory3(Long userid,int num);

    @Query(nativeQuery = true, value = "SELECT `cognition_answer`,createtime FROM `aidoctor_ccbtaccess_answer`  " +
            "where cognition_answer is not null and  userid=?1  ORDER BY createtime desc limit ?2")
    List<Object> getLastestHistory4(Long userid,int num);

    @Query(nativeQuery = true, value = "SELECT `relationship_answer`,createtime FROM `aidoctor_ccbtaccess_answer`  " +
            "where relationship_answer is not null and  userid=?1  ORDER BY createtime desc limit ?2")
    List<Object> getLastestHistory5(Long userid,int num);




}
