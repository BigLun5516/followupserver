package com.epic.followup.repository.managementSys.task;

import com.epic.followup.model.managementSys.task.TaskFinishedModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskStatusRepository extends JpaRepository<TaskFinishedModel, Long> {
    //查找学生已完成的
    @Query(nativeQuery = true, value = "SELECT a.task_id,b.title,b.content,b.create_time,c.scale_name FROM " +
            "`management_task_finished` a LEFT JOIN management_task b ON a.task_id=b.task_id LEFT JOIN management_scale " +
            "c ON  b.scale_id=c.scale_id WHERE a.user_id=?1")
    List<Object> getTask1(Long userId);

    //查找学生未完成的(根据所有的任务再排除完成表中的任务）
    @Query(nativeQuery = true, value = "SELECT c.task_id,c.title,c.content,c.create_time,d.scale_name " +
            "FROM management_task c LEFT JOIN management_scale d ON c.scale_id=d.scale_id " +
            "WHERE c.task_id in (SELECT a.task_id FROM management_task a " +
            "WHERE a.university_id=?1 AND a.grade=?2 And (a.college_id=-1 or a.college_id =?4)  " +
            "AND a.task_id NOT in(SELECT b.task_id FROM management_task_finished b WHERE b.user_id=?3 )) ")
    List<Object> getTask2(Integer university_id,String grade,Long userId,Integer college_id);
}
