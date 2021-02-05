package com.epic.followup.service.followup2.doctor;

import com.epic.followup.model.followup2.doctor.StudentResultModel;
import com.epic.followup.service.followup2.doctor.StudentResultService;

import java.util.List;
import java.util.Map;

/**
 * 获取指定学院的数据
 *
 * @author : zx
 * @version V1.0
 */
public interface CollegeStudentResultService{

    /**
     * 获取指定学校，省份的抑郁分布
     * @return List<row> province level num
     */
    List<Map> findProvinceCountsByDepartment(String department, String college);
//    List<Map> findProvinceCountsByDepartmentAndCollege(String department, String college);

    /**
     * 获取学历分布
     */
    List<Map> findStypeCountsByDepartment(String department, String college);
//    List<Map> findStypeCountsByDepartmentAndCollege(String department, String College);

    /**
     * 获取年龄分布
     */
    List<Map> findAgeCountsByDepartment(String department, String college);


    /**
     * 获取人数分布
     */
    List<Map> findAllCountsByDepartment(String department, String college);

    /**
     * 获取全院数据
     */
    List<StudentResultModel> findListByDepartmentAndCollege(String department, String college);


}
