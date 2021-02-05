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

    @Query(nativeQuery = true, value = "SELECT * FROM aidoctor_ccbtaccess_answer" +
            " WHERE userid = ?1 order by createtime desc limit 1")
    List<CCBTAccessAnswerModel> getLastestHistory(Long userid);


}
