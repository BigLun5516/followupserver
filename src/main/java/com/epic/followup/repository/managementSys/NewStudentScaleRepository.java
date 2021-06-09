package com.epic.followup.repository.managementSys;

import com.epic.followup.model.managementSys.NewStudentScaleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NewStudentScaleRepository extends JpaRepository<NewStudentScaleModel, Long> {

    /**
     * 根据学校id 查询new_student_scale表中的评测数
     * @param universityId
     * @return
     */
    @Query(value = "SELECT count(*) \n" +
            "FROM new_student_scale m left join aidoctor_studentinfo s on m.userid = s.userid \n" +
            "where s.university_id = ?1  and (s.stype = ?2 or ?2 = '') and (s.college_id = ?3 or ?3 = '')\n" +
            "    and (m.answer_time between ?4 and ?5)"
            , nativeQuery = true)
    Integer countEvaluationFromNewstudentscaleByUniversityId(Integer universityId, String sType, String collegeId, String startDate, String endDate);

}
