package com.epic.followup.repository.managementSys.task;

import com.epic.followup.model.managementSys.task.TaskModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskModel, Long> {

    TaskModel findByTaskId(Long task_id);



}
