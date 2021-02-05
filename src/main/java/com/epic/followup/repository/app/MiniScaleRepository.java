package com.epic.followup.repository.app;

import com.epic.followup.model.app.MiniScaleModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MiniScaleRepository extends JpaRepository<MiniScaleModel, Long> {

}
