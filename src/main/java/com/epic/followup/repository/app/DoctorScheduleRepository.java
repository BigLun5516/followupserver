package com.epic.followup.repository.app;

import com.epic.followup.model.app.DoctorScheduleModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorScheduleRepository extends JpaRepository<DoctorScheduleModel, Long> {

    Optional<DoctorScheduleModel> findBytime(String time);

    Optional<DoctorScheduleModel> findById(Long id);
}
