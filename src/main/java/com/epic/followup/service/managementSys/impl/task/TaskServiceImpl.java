package com.epic.followup.service.managementSys.impl.task;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.followup2.student.StudentInfo;
import com.epic.followup.model.managementSys.task.TaskModel;
import com.epic.followup.model.managementSys.task.TaskFinishedModel;
import com.epic.followup.repository.followup2.student.StudentInfoRepository;
import com.epic.followup.repository.managementSys.task.TaskRepository;
import com.epic.followup.repository.managementSys.task.TaskStatusRepository;
import com.epic.followup.service.managementSys.task.TaskService;
import com.epic.followup.temporary.followup2.session.BaseUserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private StudentInfoRepository studentInfoRepository;

    // 查询该学生已完成的任务
    @Override
    public JSONObject findTask1(BaseUserSession bus){
        JSONObject res = new JSONObject();
        List<Map<String, Object>> data = new ArrayList<>();
        Long userId=bus.getUserId();
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
            item.put("scale_id", obj[5]);
            data.add(item);
        }
        res.put("errorCode", 200);
        res.put("errorMsg", "查询成功");
        res.put("data",data);
        return res;
    }

    // 查询该学生未完成的任务
    @Override
    public JSONObject findTask2(BaseUserSession bus){
        JSONObject res = new JSONObject();
        List<Map<String, Object>> data = new ArrayList<>();
        Optional<StudentInfo> u=studentInfoRepository.findByUserId(bus.getUserId());
        StudentInfo stu=u.get();
        List<Object> tasklist=taskStatusRepository.getTask2(bus.getUniversityId(),stu.getYear(),bus.getUserId(),stu.getCollegeId());
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
            item.put("scale_id", obj[5]);
            data.add(item);
        }
        res.put("errorCode", 200);
        res.put("errorMsg", "查询成功");
        res.put("data",data);
        return res;
    }

    // 保存该学生的完成任务
    @Override
    public JSONObject saveTask(JSONObject params){
        JSONObject res = new JSONObject();
        TaskFinishedModel t=new TaskFinishedModel();
        t.setTaskId(params.getLong("task_id"));
        t.setUserId(params.getLong("user_id"));
        t.setUpdateTime(new Date());
        taskStatusRepository.save(t);
        res.put("errorCode", 200);
        res.put("errorMsg", "保存成功");
        return res;
    }

    //创建任务
    @Override
    public JSONObject addTask(JSONObject params, HttpSession session){
        //获取创建任务的人的学院id
        List<Integer> colleges=(List<Integer>)session.getAttribute("collegeId");
        JSONObject res = new JSONObject();
        for(Integer c:colleges) {
            TaskModel t = new TaskModel();
            t.setContent(params.getString("content"));
            t.setCreateTime(new Date());
            t.setGrade(params.getString("grade"));
            t.setScaleId(params.getLong("scale_id"));
            t.setTitle(params.getString("title"));
            t.setUniversityId(params.getInteger("universityId"));
            t.setCollegeId(c);
            taskRepository.save(t);
        }
        res.put("errorCode", 200);
        res.put("errorMsg", "保存成功");
        return res;
    }

    //查询任务(此处是后台人员查看任务，应该根据登录人员的学校来查看内部的任务）
    @Override
    public JSONObject findAllTask(Integer universityId,HttpSession session) throws ParseException {
        List<Integer> colleges=(List<Integer>)session.getAttribute("collegeId");
        JSONObject res = new JSONObject();
        List<Object> data = taskRepository.findListByUid(universityId,colleges);
        List<Map<String, Object>> taskTable = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(Object o:data){
            Object[] temp = (Object[]) o;
            Map<String, Object> item = new HashMap<>();
            item.put("taskId",temp[0]);
            item.put("title",temp[1]);
            item.put("content",temp[2]);
            item.put("grade",temp[3]);
            item.put("scaleId",temp[4]);
            item.put("createTime",dateFormat.format(temp[5]));
            item.put("university",temp[6]);
            item.put("scale",temp[7]);
            if(temp[8]==null||temp[8]==""){
                item.put("college","全部");
            }else{
                item.put("college",temp[8]);
            }
            taskTable.add(item);
        }
        res.put("errorCode", 200);
        res.put("errorMsg", "保存成功");
        res.put("data",taskTable);
        return res;
    }

//    //编辑任务状态
//    public JSONObject editTask(JSONObject params){
//        JSONObject res = new JSONObject();
//        Integer label=params.getInteger("label");
//        TaskModel t = taskRepository.findByTaskId(params.getLong("task_id"));
//        if(label==1)
//            t.setTaskStatus(1);
//        else
//            t.setTaskStatus(2);
//        taskRepository.save(t);
//        res.put("errorCode", 200);
//        res.put("errorMsg", "保存成功");
//        return res;
//    }


}
