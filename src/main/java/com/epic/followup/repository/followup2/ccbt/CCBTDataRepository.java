package com.epic.followup.repository.followup2.ccbt;

import com.epic.followup.model.followup2.ccbt.CCBTDataModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CCBTDataRepository extends JpaRepository<CCBTDataModel, Long> {

    @Query(value = "SELECT * \n" +
            "FROM ccbt_data\n" +
            "where commit_type = ?1 and userid = ?2\n" +
            "order by date desc\n" +
            "limit ?3, ?4"
            , nativeQuery = true)
    List<CCBTDataModel> findCCBTDataModelByCommitTypeAndUserId(String commitType, Long userId, Integer offset, Integer len);
}
