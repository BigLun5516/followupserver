package com.epic.followup.repository.followup2.student;

import com.epic.followup.model.followup2.student.WechatFileModel;
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
public interface WechatFile2Repository extends JpaRepository<WechatFileModel, Long> {
    /*
     * 查询指定日期未提交数据
     */
    @Query(nativeQuery = true, value = "SELECT * FROM aidoctor_wechat_file a " +
            "WHERE userid = ?1 AND succ = 0 AND answer_time BETWEEN ?2 AND ?3 " +
            "ORDER BY answer_time DESC LIMIT 1")
    List<WechatFileModel> findLastAnswerByOpenid(Long userid, Date start, Date end);


    /*
     * 删除指定日期未提交数据
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM aidoctor_wechat_file " +
            "WHERE userid = ?1 AND succ = 0 AND answer_time BETWEEN ?2 AND ?3")
    void deleteByDateAndOpenID(Long userid, Date start, Date end);

    /*
     * 获取指定日期未提交数据
     */
    @Query(nativeQuery = true, value = "SELECT * FROM aidoctor_wechat_file " +
            "WHERE userid = ?1 AND succ = 0 AND answer_time BETWEEN ?2 AND ?3")
    List<WechatFileModel> findUnsuccAnswersByDateAndOpenID(Long userid, Date start, Date end);


    /*
     * 提交指定日期数据
     */
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE aidoctor_wechat_file a SET a.succ = 1 " +
            " WHERE userid = ?1 AND succ = 0 AND answer_time BETWEEN ?2 AND ?3")
    int updateSucc(Long userid, Date start, Date end);


    /*
     * 获取指定日期已提交数据
     */
    @Query(nativeQuery = true, value = "SELECT * FROM aidoctor_wechat_file a " +
            "WHERE userid = ?1 AND succ = 1 AND answer_time BETWEEN ?2 AND ?3 " +
            "ORDER BY answer_time DESC")
    List<WechatFileModel> getAnswerByOpenIdAndDate(Long userid, Date start, Date end);

    /*
     * 查询最近一次成功的测评
     */
    @Query(nativeQuery = true, value = "SELECT * FROM aidoctor_wechat_file a " +
            "WHERE userid = ?1 AND succ = 1 " +
            "ORDER BY answer_time DESC LIMIT ?2")
    List<WechatFileModel> getLastSuccByOpenId(Long userid, int i);

}
