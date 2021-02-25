package com.epic.followup.repository.managementSys.pic;

import com.epic.followup.model.managementSys.pic.PicModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PicRepository extends JpaRepository<PicModel, Long> {

    // 根据条件查询
    @Query(value = "select * from management_pic p where p.pic_name like %?1% " +
            "and p.pic_type like ?2 and p.pic_status like ?3 " +
            "and ((p.create_time > ?4 and p.create_time < ?5) or (?4 = \"\" and ?5 = \"\")) ", nativeQuery = true)
    List<PicModel> findPicByQuery(String picName, String picType, String picStatus, String minDate, String maxDate);
}
