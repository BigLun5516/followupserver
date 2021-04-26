package com.epic.followup.repository.managementSys.scale;

import com.epic.followup.model.managementSys.scale.ScaleForbiddenModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScaleForbiddenRepository extends JpaRepository<ScaleForbiddenModel, Integer> {

    Optional<ScaleForbiddenModel> getScaleForbiddenModelByScaleIdAndUniversityName(Long scaleId, String universityName);

    @Modifying
    @Query(value = "delete from management_scale_forbidden f where f.scale_id = ?1 and f.university_name = ?2"
            , nativeQuery = true)
    void deleteByScaleIdAndUniversityName(Long scaleId, String universityName);

    @Modifying
    @Query(value = "delete from management_scale_forbidden f where f.scale_id = ?1"
            , nativeQuery = true)
    void deleteByScaleId(Long scaleId);
}
