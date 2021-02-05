package com.epic.followup.repository.managementSys;

import com.epic.followup.model.managementSys.PsychologicalCenterModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PsychologicalCenterRepository extends JpaRepository<PsychologicalCenterModel, Integer> {

    PsychologicalCenterModel findByCenterId(Integer id);
}
