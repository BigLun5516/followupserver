package com.epic.followup.repository.school;

import com.epic.followup.model.school.SchoolPsychologistModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SchoolPsychologistRepository extends JpaRepository<SchoolPsychologistModel, Long> {

    Optional<SchoolPsychologistModel> findById(Long id);

}
