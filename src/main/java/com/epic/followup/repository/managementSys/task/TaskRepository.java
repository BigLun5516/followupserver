package com.epic.followup.repository.managementSys.task;

import com.epic.followup.model.managementSys.task.TaskModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskModel, Long> {

    TaskModel findByTaskId(Long task_id);

    @Query(nativeQuery = true, value = "SELECT a.task_id,a.title,a.content,a.grade,a.scale_id,a.create_time,b.university_name,c.scale_name ,co.college_name  " +
            "FROM `management_task` a LEFT JOIN management_university b on a.university_id=b.university_id " +
            "LEFT JOIN management_scale c ON a.scale_id=c.scale_id LEFT JOIN management_college co ON co.college_id=a.college_id " +
            "WHERE (a.university_id= ?1 or ?1=-1) AND (a.college_id in ?2 or -1 in ?2 or a.college_id=-1)" )
    List<Object> findListByUid(Integer uid,List<Integer> colleges);

}
