package com.epic.followup.repository;

import com.epic.followup.conf.WeChatConfig;
import com.epic.followup.model.AnswerModel;
import org.springframework.beans.factory.annotation.Autowired;
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
public interface AnswerRepository extends JpaRepository<AnswerModel, Long> {


    /*
     * 查询指定日期未提交数据
     */
    @Query(nativeQuery = true, value = "SELECT * FROM answer a " +
            "WHERE openid = ?1 AND succ = 0 AND answer_time BETWEEN ?2 AND ?3 " +
            "ORDER BY answer_time DESC LIMIT 1")
    List<AnswerModel> findLastAnswerByOpenid(String openid, Date start, Date end);


    /*
     * 删除指定日期未提交数据
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM answer " +
            "WHERE openid = ?1 AND succ = 0 AND answer_time BETWEEN ?2 AND ?3")
    void deleteByDateAndOpenID(String openid, Date start, Date end);


    /*
     * 提交指定日期数据
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE answer a SET a.succ = 1 " +
            " WHERE openid = ?1 AND succ = 0 AND answer_time BETWEEN ?2 AND ?3")
    int updateSucc(String openid, Date start, Date end);


    /*
     * 获取指定日期已提交数据
     */
    @Query(nativeQuery = true, value = "SELECT * FROM answer a " +
            "WHERE openid = ?1 AND succ = 1 AND answer_time BETWEEN ?2 AND ?3 " +
            "ORDER BY answer_time DESC")
    List<AnswerModel> getAnswerByOpenIdAndDate(String openid, Date start, Date end);

    /*
     * 查询最近一次成功的测评
     */
    @Query(nativeQuery = true, value = "SELECT * FROM answer a " +
            "WHERE openid = ?1 AND succ = 1 " +
            "ORDER BY answer_time DESC LIMIT ?2")
    List<AnswerModel> getLastSuccByOpenId(String openid, int i);

    /*
     * 获取指定日期未提交数据
     */
    @Query(nativeQuery = true, value = "SELECT * FROM answer " +
            "WHERE openid = ?1 AND succ = 0 AND answer_time BETWEEN ?2 AND ?3")
    List<AnswerModel> findUnsuccAnswersByDateAndOpenID(String openid, Date start, Date end);
}
