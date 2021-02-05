package com.epic.followup.repository.app;

import com.epic.followup.model.app.MiniScaleModel;
import com.epic.followup.model.app.MiniScalePublicModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MiniScalePublicRepository extends JpaRepository<MiniScalePublicModel, Long> {

}
