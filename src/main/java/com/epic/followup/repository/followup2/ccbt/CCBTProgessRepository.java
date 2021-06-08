package com.epic.followup.repository.followup2.ccbt;

import com.epic.followup.model.followup2.ccbt.CCBTProgressModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CCBTProgessRepository extends JpaRepository<CCBTProgressModel, Long> {

    CCBTProgressModel findByUserid(Long userid);
}
