package com.epic.followup.repository.app;

import com.epic.followup.model.app.DoctorManagerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DoctorManagerRepository extends JpaRepository<DoctorManagerModel, Long> {

    DoctorManagerModel findByUsername(String username);
    DoctorManagerModel findByUsernameAndAndPassword(String username, String password);

    @Query(nativeQuery = true, value = "Select name from app_doctor")
    List<String> findByQuery1();
}
