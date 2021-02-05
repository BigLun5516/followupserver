package com.epic.followup.repository.app;

import com.epic.followup.model.app.PatientBodyInformationModel;
import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PatientBodyInformationRepository extends JpaRepository<PatientBodyInformationModel, Long> {

    //根据时间和id查找信息
    Optional<PatientBodyInformationModel> findByTimeAndPid(String time,Long pid);

    //根据用户id查找最近七天的身体数据
    @Query(nativeQuery=true, value = "SELECT * FROM patient_binformation "+
            "WHERE pid = ?1 order by datetime DESC limit 7")
    List<PatientBodyInformationModel> findBodyInfoByPid(Long pid);

    /**
     * 根据月份查找当月有记录的日期
     * @param month
     * @return
     */
    @Query(nativeQuery=true, value = "SELECT datetime FROM patient_binformation "+
            "WHERE pid = ?1 and datetime like CONCAT(?2, '%') ")
    List<String> findDayByMonth(Long userId, String month);


}
