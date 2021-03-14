package com.epic.followup.repository.managementSys.task;

import com.epic.followup.model.managementSys.task.TaskStatusModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskStatusRepository extends JpaRepository<TaskStatusModel, Long> {
    //查找学生已完成的
    @Query(nativeQuery = true, value = "SELECT a.task_id,b.title,b.content,b.create_time,c.scale_name " +
            "FROM `management_task_status` a LEFT JOIN management_task b ON a.task_id=b.task_id LEFT JOIN " +
            "management_scale c ON  b.scale_id=c.scale_id WHERE a.user_id=?1 AND a.`status`=1 ;")
    List<Object> getTask1(Long userId);

    //查找学生未完成的
    @Query(nativeQuery = true, value = "SELECT a.task_id,b.title,b.content,b.create_time,c.scale_name " +
            "FROM `management_task_status` a LEFT JOIN management_task b ON a.task_id=b.task_id LEFT JOIN " +
            "management_scale c ON  b.scale_id=c.scale_id WHERE a.user_id=?1 AND a.`status`=0 ;")
    List<Object> getTask2(Long userId);
}
