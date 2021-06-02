package com.epic.followup.repository.followup2.ccbt;

import com.epic.followup.model.followup2.ccbt.CCBTDataModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CCBTDataRepository extends JpaRepository<CCBTDataModel, Long> {

    CCBTDataModel findCCBTDataModelByCommitTypeAndUserId(String commitType, Long userId);
}
