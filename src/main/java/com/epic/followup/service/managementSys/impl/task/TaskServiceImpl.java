package com.epic.followup.service.managementSys.impl.task;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.managementSys.task.TaskModel;
import com.epic.followup.model.managementSys.task.TaskStatusModel;
import com.epic.followup.repository.managementSys.task.TaskRepository;
import com.epic.followup.repository.managementSys.task.TaskStatusRepository;
import com.epic.followup.service.managementSys.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    // 查询该学生已完成的任务
    @Override
    public JSONObject findTask1(Long userId){
        JSONObject res = new JSONObject();
        List<Map<String, Object>> data = new ArrayList<>();
        List<Object> tasklist=taskStatusRepository.getTask1(userId);
        for (Object o : tasklist) {
            Map<String, Object> item = new HashMap<>();
            Object[] obj = (Object[]) o;
            item.put("task_id", obj[0]);
            item.put("title", obj[1]);
            item.put("content", obj[2]);
//            item.put("create_time", obj[3]);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            item.put("create_time", dateFormat.format(obj[3]));
            item.put("scale_name", obj[4]);
            data.add(item);
        }
        res.put("errorCode", 200);
        res.put("errorMsg", "查询成功");
        res.put("data",data);
        return res;
    }

    // 查询该学生未完成的任务
    public JSONObject findTask2(Long userId){
        JSONObject res = new JSONObject();
        List<Map<String, Object>> data = new ArrayList<>();
        List<Object> tasklist=taskStatusRepository.getTask2(userId);
        for (Object o : tasklist) {
            Map<String, Object> item = new HashMap<>();
            Object[] obj = (Object[]) o;
            item.put("task_id", obj[0]);
            item.put("title", obj[1]);
            item.put("content", obj[2]);
//            item.put("create_time", obj[3]);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            item.put("create_time", dateFormat.format(obj[3]));
            item.put("scale_name", obj[4]);
            data.add(item);
        }
        res.put("errorCode", 200);
        res.put("errorMsg", "查询成功");
        res.put("data",data);
        return res;
    }

    // 保存该学生的完成任务
    public JSONObject saveTask(JSONObject params){
        JSONObject res = new JSONObject();
        TaskStatusModel t=new TaskStatusModel();
        t.setTaskId(params.getLong("task_id"));
        t.setStatus(params.getInteger("status"));
        t.setUserId(params.getLong("user_id"));
        t.setUpdateTime(new Date());
        taskStatusRepository.save(t);
        res.put("errorCode", 200);
        res.put("errorMsg", "保存成功");
        return res;
    }

    //创建任务
    public JSONObject addTask(JSONObject params){
        JSONObject res = new JSONObject();
        TaskModel t=new TaskModel();
        t.setContent(params.getString("content"));
        t.setCreateTime(new Date());
        t.setGrade(params.getString("grade"));
        t.setScaleId(params.getLong("scale_id"));
        t.setTaskStatus(0);
        t.setTitle(params.getString("title"));
        taskRepository.save(t);
        res.put("errorCode", 200);
        res.put("errorMsg", "保存成功");
        return res;
    }

    //查询任务
    public JSONObject findAllTask() throws ParseException {
        JSONObject res = new JSONObject();
        List<TaskModel> data = taskRepository.findAll();
        List<Map<String, Object>> taskTable = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(TaskModel temp:data){
            Map<String, Object> item = new HashMap<>();
            item.put("taskId",temp.getTaskId());
            item.put("title",temp.getTitle());
            item.put("content",temp.getContent());
            item.put("grade",temp.getGrade());
            item.put("scaleId",temp.getScaleId());
            item.put("taskStatus",temp.getTaskStatus());
            item.put("createTime",dateFormat.format(temp.getCreateTime()));
            taskTable.add(item);
        }
        res.put("errorCode", 200);
        res.put("errorMsg", "保存成功");
        res.put("data",taskTable);
        return res;
    }

    //编辑任务状态
    public JSONObject editTask(JSONObject params){
        JSONObject res = new JSONObject();
        Integer label=params.getInteger("label");
        TaskModel t = taskRepository.findByTaskId(params.getLong("task_id"));
        if(label==1)
            t.setTaskStatus(1);
        else
            t.setTaskStatus(2);
        taskRepository.save(t);
        res.put("errorCode", 200);
        res.put("errorMsg", "保存成功");
        return res;
    }


}
