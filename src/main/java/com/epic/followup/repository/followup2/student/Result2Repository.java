package com.epic.followup.repository.followup2.student;

import com.epic.followup.model.followup2.student.ResultModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author : zx
 * @version V1.0
 */

@Repository
public interface Result2Repository extends JpaRepository<ResultModel, Long> {

    /**
     * 获取测评总数
     * @return long
     */
    long count();

    /**
     * 获取新增测评数
     * @return 数量
     */
//    @Query(" select count(t) from aidoctor_result t where t.answer_time > today")
//    int getNumByDate(@Param("today")Date today);





}
