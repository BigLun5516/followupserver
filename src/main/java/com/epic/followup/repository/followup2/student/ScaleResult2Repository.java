package com.epic.followup.repository.followup2.student;

import com.epic.followup.model.followup2.student.NCovResultModel;
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
public interface ScaleResult2Repository extends JpaRepository<NCovResultModel, Long> {

    /*
     * 查询最近一次成功的测评
     */
    @Query(nativeQuery = true, value = "SELECT * FROM aidoctor_ncov_result a " +
            "WHERE userid = ?1 AND scale_id = ?2 " +
            "ORDER BY answer_time DESC LIMIT 1")
    List<NCovResultModel> getLastScaleByOpenIdAndScaleId(Long userId, int scaleId);


    /**
     * 查询最近100天测评成功的历史时间和level
     * @param userId
     * @return
     */
    @Query(nativeQuery = true, value = "SELECT DATE_FORMAT(answer_time,'%Y-%m-%d %H:%i') time, level FROM aidoctor_ncov_result WHERE userid = ?1" +
            "  AND scale_id = 1  ORDER BY answer_time DESC LIMIT 100")
    List getLastScaleByUserId(Long userId);


/*
    @Query(nativeQuery = true, value = "SELECT * FROM aidoctor_ncov_result a " +
            "WHERE userid = ?1 AND scale_id = ?2 AND DATE(answer_time) = ?3")
    List<NCovResultModel> getResultByDate(Long userId, int scaleId, String date);*/


    @Query(nativeQuery = true, value = "SELECT * FROM aidoctor_ncov_result a " +
            "WHERE userid = ?1 AND scale_id = ?2 AND answer_time between ?3 and ?4")
    List<NCovResultModel> getResultByDate(Long userId, int scaleId, String beforeDate, String afterDate);

    /*
     * 查询不同年份20/19/18对应的不同scale_id下不同得分人数
     * 0：自杀 >4
     * 1：抑郁 >4
     * 2：焦虑 >4
     * 3：失眠 >7
     * 4：应激 事件影响量表 >8
     * 参数1：year 参数2：scale_id 参数3：score
     */
    @Query(nativeQuery = true, value = "SELECT COUNT(a.userid) FROM aidoctor_ncov_result a, (SELECT a.userid, a.scale_id, MAX(a.answer_time) AS answer_time FROM aidoctor_ncov_result a WHERE a.answer_time LIKE ?1% AND a.scale_id = ?2 GROUP BY a.userid, a.scale_id) AS b " +
            "WHERE a.answer_time  = b.answer_time AND a.userid = b.userid AND a.scale_id = b.scale_id AND a.score>?3 ")
    Integer getScaleByYear(int year, int scale, int score);

    // 查询 高校 不同年份20/19/18对应的不同scale_id下不同得分人数
    @Query(nativeQuery = true, value = "SELECT COUNT(a.userid) FROM aidoctor_ncov_result a, (SELECT a.userid, a.scale_id, MAX(a.answer_time) AS answer_time FROM aidoctor_ncov_result a, aidoctor_studentinfo s WHERE a.answer_time LIKE ?1% AND a.scale_id = ?2 AND a.userid = s.userid AND s.department = ?4 GROUP BY a.userid, a.scale_id) AS b " +
            "WHERE a.answer_time  = b.answer_time AND a.userid = b.userid AND a.scale_id = b.scale_id AND a.score>?3 ")
    Integer getScaleByYearAndUni(int year, int scale, int score, String universityName);


    /*
     * 查询当天不同时间段的评测结果
     */
    @Query(nativeQuery = true, value = "SELECT SUM(a.scale_id) FROM aidoctor_ncov_result a " +
            "WHERE a.answer_time BETWEEN ?1 AND ?2 AND a.scale_id =?3 AND a.score> ?4 ")
    Integer getScaleByDayTime(String startTime, String endTime, int scale, int score);

    // 查询 高校 当天不同时间段的评测结果
    @Query(nativeQuery = true, value = "SELECT SUM(a.scale_id) FROM aidoctor_ncov_result a, aidoctor_studentinfo s " +
            "WHERE a.answer_time BETWEEN ?1 AND ?2 AND a.scale_id =?3 AND a.score> ?4 AND a.userid = s.userid AND s.department = ?5 ")
    Integer getScaleByDayTimeAndUni(String startTime, String endTime, int scale, int score, String universityName);


    /*
     * 查询 不同程度 userid
     * 0: 重度 >14 20 中度 9 15 轻度 4 10
     * 1: 重度 >14 28 中度 9 15 轻度 4 10
     * 2: 重度 >14 22 中度 9 15 轻度 4 10
     * 3: 重度 >21 29 中度 14 22 轻度 7 15
     * 4: 重度 >43 89 中度 25 44 轻度 8 26
     */
    @Query(nativeQuery = true, value = "SELECT a.userid FROM aidoctor_ncov_result a, aidoctor_studentinfo s, (SELECT a.userid, a.scale_id, MAX(a.answer_time) AS answer_time FROM aidoctor_ncov_result a WHERE a.scale_id = ?1 GROUP BY a.userid, a.scale_id) AS b " +
            "WHERE a.answer_time  = b.answer_time AND a.userid = b.userid AND a.scale_id = b.scale_id AND a.score>?2 AND a.score<?3 AND a.userid = s.userid AND s.department = ?4 ")
    List<Long>  getDegreeUserId(int scale, int min, int max, String universityName);


    @Query(nativeQuery = true, value = "SELECT s.stname, s.age, a.level FROM aidoctor_ncov_result a, aidoctor_studentinfo s, (SELECT a.userid, a.scale_id, MAX(a.answer_time) AS answer_time FROM aidoctor_ncov_result a WHERE a.scale_id = ?1 GROUP BY a.userid, a.scale_id) AS b " +
            "WHERE a.answer_time  = b.answer_time AND a.userid = b.userid AND a.scale_id = b.scale_id AND a.score>?2 AND a.score<?3 AND a.userid = s.userid AND s.userid IN ?4 ")
    List<Object> getDegreeInfoByUserId(int scale, int min, int max, List<Long> userId);


//    @Query(nativeQuery = true, value = "SELECT s.stname, s.age, a.level FROM aidoctor_ncov_result a, aidoctor_studentinfo s WHERE a.userid = s.userid ")
//    Object[] getDegreeInfoByUserId(Long userId);


    // 统计ncov_result中某个学校的评测人数
    @Query(nativeQuery = true, value = "select count(*) \n" +
            "from aidoctor_studentinfo, aidoctor_ncov_result\n" +
            "where aidoctor_studentinfo.university_id = ? and aidoctor_studentinfo.userid = aidoctor_ncov_result.userid and scale_id = 1")
    Integer countResultByUniversityId(Integer universityId);

    NCovResultModel findByUserId(Long id);
    @Query(nativeQuery = true, value = "SELECT a.score FROM aidoctor_ncov_result a, aidoctor_studentinfo s, (SELECT a.userid, a.scale_id, MAX(a.answer_time) AS answer_time FROM aidoctor_ncov_result a WHERE a.scale_id = ?1 GROUP BY a.userid, a.scale_id) AS b " +
            "WHERE a.answer_time  = b.answer_time AND a.userid = b.userid AND a.scale_id = b.scale_id AND a.userid = ?2 ")
    Integer getScoreByUserId(int scale, Long userId);


    /**
     * 查询指定学院某时间段内的评测结果人数
     */
    @Query(value = "SELECT count(a.userid)\n" +
            "FROM aidoctor_ncov_result a\n" +
            "\t\t, (SELECT a.userid, a.scale_id, MAX(a.answer_time) AS answer_time \n" +
            "\t\t\tFROM aidoctor_ncov_result a \n" +
            "\t\t\tWHERE a.answer_time BETWEEN ?1 and ?2 AND a.scale_id = ?3 \n" +
            "\t\t\tGROUP BY a.userid, a.scale_id) AS b, aidoctor_studentinfo s\n" +
            "WHERE a.answer_time  = b.answer_time AND a.userid = b.userid AND a.scale_id = b.scale_id " +
            "AND a.score> ?4 and a.userid = s.userid and s.college_id = ?5"
            , nativeQuery = true)
    Integer countScaleByTimeAndCollegeId(String startTime, String endTime, int scaleId, int score, Integer collegeId);


    /**
     * 查询指定高校某时间段内的评测结果人数
     */
    @Query(value = "SELECT count(a.userid)\n" +
            "FROM aidoctor_ncov_result a\n" +
            "\t\t, (SELECT a.userid, a.scale_id, MAX(a.answer_time) AS answer_time \n" +
            "\t\t\tFROM aidoctor_ncov_result a \n" +
            "\t\t\tWHERE a.answer_time BETWEEN ?1 and ?2 AND a.scale_id = ?3 \n" +
            "\t\t\tGROUP BY a.userid, a.scale_id) AS b, aidoctor_studentinfo s\n" +
            "WHERE a.answer_time  = b.answer_time AND a.userid = b.userid AND a.scale_id = b.scale_id " +
            "AND a.score> ?4 and a.userid = s.userid and s.university_id = ?5"
            , nativeQuery = true)
    Integer countScaleByTimeAndUniversityId(String startTime, String endTime, int scaleId, int score, Integer universityId);


    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM aidoctor_ncov_result  WHERE  id IN " +
            "(SELECT id FROM (SELECT id FROM aidoctor_ncov_result  WHERE userid = ?1  ORDER BY answer_time DESC LIMIT ?2) t )")
    void deleteResult(Long userId, Long count);

}
