package com.epic.followup.repository.managementSys.task;

import com.epic.followup.model.managementSys.task.TaskModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskModel, Long> {

    TaskModel findByTaskId(Long task_id);

    @Query(nativeQuery = true, value = "SELECT a.task_id,a.title,a.content,a.grade,a.scale_id,a.create_time,b.university_name FROM " +
            "`management_task` a LEFT JOIN management_university b on a.university_id=b.university_id WHERE (a.university_id= ?1 or ?1=-1)" )
    List<Object> findListByUid(Integer uid);

}
