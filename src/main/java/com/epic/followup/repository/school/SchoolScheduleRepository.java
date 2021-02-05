package com.epic.followup.repository.school;

import com.epic.followup.model.school.SchoolScheduleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SchoolScheduleRepository extends JpaRepository<SchoolScheduleModel, Long> {

    Optional<SchoolScheduleModel> findBytime(String time);

    Optional<SchoolScheduleModel> findById(Long id);

    @Query(nativeQuery = true, value = "Select name from school_psychologist")
    List<String> findByQuery1();
}
