package com.epic.followup.repository.followup2.student;

import com.epic.followup.model.followup2.student.AnswerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author : zx
 * @version V1.0
 */
public interface Answer2Repository extends JpaRepository<AnswerModel, Long> {


    /*
     * 查询指定日期未提交数据
     */
    @Query(nativeQuery = true, value = "SELECT * FROM aidoctor_answer a " +
            "WHERE userid = ?1 AND succ = 0 AND answer_time BETWEEN ?2 AND ?3 " +
            "ORDER BY answer_time DESC LIMIT 1")
    List<AnswerModel> findLastAnswerByOpenid(Long userid, Date start, Date end);


    /*
     * 删除指定日期未提交数据
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM aidoctor_answer " +
            "WHERE userid = ?1 AND succ = 0 AND answer_time BETWEEN ?2 AND ?3")
    void deleteByDateAndOpenID(Long userid, Date start, Date end);


    /*
     * 提交指定日期数据
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE aidoctor_answer a SET a.succ = 1 " +
            " WHERE userid = ?1 AND succ = 0 AND answer_time BETWEEN ?2 AND ?3")
    int updateSucc(Long userid, Date start, Date end);


    /*
     * 获取指定日期已提交数据
     */
    @Query(nativeQuery = true, value = "SELECT * FROM aidoctor_answer a " +
            "WHERE userid = ?1 AND succ = 1 AND answer_time BETWEEN ?2 AND ?3 " +
            "ORDER BY answer_time DESC")
    List<AnswerModel> getAnswerByOpenIdAndDate(Long userid, Date start, Date end);

    /*
     * 查询最近一次成功的测评
     */
    @Query(nativeQuery = true, value = "SELECT * FROM aidoctor_answer a " +
            "WHERE userid = ?1 AND succ = 1 " +
            "ORDER BY answer_time DESC LIMIT ?2")
    List<AnswerModel> getLastSuccByOpenId(Long userid, int i);

    /*
     * 获取指定日期未提交数据
     */
    @Query(nativeQuery = true, value = "SELECT * FROM aidoctor_answer " +
            "WHERE userid = ?1 AND succ = 0 AND answer_time BETWEEN ?2 AND ?3")
    List<AnswerModel> findUnsuccAnswersByDateAndOpenID(Long userid, Date start, Date end);

    // 统计某个学校的Answer数
    @Query(value = "select count(*) \n" +
            "from aidoctor_studentinfo, aidoctor_answer\n" +
            "where aidoctor_studentinfo.university_id = ? and aidoctor_studentinfo.userid = aidoctor_answer.userid"
            , nativeQuery = true)
    Integer countAnswerByUniversityId(Integer universityId);

    /**
     * 根据学校id 查询aidoctor_answer表中的评测数
     * @param universityId
     * @return
     */
    @Query(value = "SELECT count(*)\n" +
            "FROM aidoctor_answer a left join aidoctor_studentinfo s on a.userid = s.userid\n" +
            "where s.university_id = ?1 and (s.stype = ?2 or ?2 = '') and (s.college_id = ?3 or ?3 = '')\n" +
            "\t\tand (a.answer_time between ?4 and ?5)"
            , nativeQuery = true)
    Integer countEvaluationFromAnswerByUniversityId(Integer universityId, String sType, String collegeId, String startDate, String endDate);
}
