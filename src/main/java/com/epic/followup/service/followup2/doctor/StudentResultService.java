package com.epic.followup.service.followup2.doctor;

import com.epic.followup.model.followup2.doctor.StudentResultModel;
import com.epic.followup.model.followup2.student.StudentInfo;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * @author : zx
 * @version V1.0
 */


public interface StudentResultService {

    /**
     * 存储
     * @param symptoms 症状 0001001
     * @param score 抑郁程度 得分
     * @param level 程度
     */
    void addResult(Long userId, StudentInfo stInfo, String symptoms, int score, String level);

    /**
     * 获取指定学校，省份的抑郁分布
     * @return List<row> province level num
     */
    List<Map> findProvinceCountsByDepartment(String department);
//    List<Map> findProvinceCountsByDepartmentAndCollege(String department, String college);

    /**
     * 获取学历分布
     */
    List<Map> findStypeCountsByDepartment(String department);
//    List<Map> findStypeCountsByDepartmentAndCollege(String department, String College);

    /**
     * 获取年龄分布
     */
    List<Map> findAgeCountsByDepartment(String department);

    /**
     * 获取性别分布
     */
    List<Map> findGenderCountsByDepartment(String department);
//    List<Map> findGenderCountsByDepartmentAndCollege(String department, String College);

    /**
     * 获取人数分布
     */
    List<Map> findAllCountsByDepartment(String department);

//    List<Map> findAllCountsByDepartmentAndCollege(String department, String college);

    /**
     * 获取最新3条数据
     */
    List<StudentResultModel> findLast(String department);

    /**
     * 获取指定userId的学生数据
     */
    StudentResultModel findByUserId(Long userId);

    /**
     * 获取指定学号
     *
     * @param department 学校
     * @param stid 学号
     * @return s
     */
    StudentResultModel findByStid(String department, String stid);

}
