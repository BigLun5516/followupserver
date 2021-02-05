package com.epic.followup.repository.school;

import com.epic.followup.model.school.SchoolClassModel;
import com.epic.followup.model.school.SchoolEmergencyModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolEmergencyRepository extends JpaRepository<SchoolEmergencyModel, Long> {


}
