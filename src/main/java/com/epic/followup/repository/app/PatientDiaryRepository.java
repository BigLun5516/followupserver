package com.epic.followup.repository.app;

import com.epic.followup.model.app.PatientDiaryModel;
import com.epic.followup.temporary.wechat.patient.diary.MoodList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PatientDiaryRepository extends JpaRepository<PatientDiaryModel, Long> {

    //根据id查找日记
    Optional<PatientDiaryModel> findById(Long id);

    //根据tel查找日记
    List<PatientDiaryModel> findAllByTel(String tel);

    //根据tel查找心情个数
    @Query(nativeQuery=true, value = "SELECT d.mood, d.time FROM patient_diary d "+
            "WHERE d.tel = ?1 order by d.time DESC limit 7")
    List findBySqlTel(String tel);


}
