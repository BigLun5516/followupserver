package com.epic.followup.test;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.app.MiniScalePublicModel;
import com.epic.followup.model.managementSys.RoleModel;
import com.epic.followup.model.managementSys.UserModel;
import com.epic.followup.model.managementSys.task.TaskModel;
import com.epic.followup.repository.app.MiniScalePublicRepository;
import com.epic.followup.repository.managementSys.RoleRepository;
import com.epic.followup.repository.managementSys.UniversityRepository;
import com.epic.followup.repository.managementSys.UserRepository;
import com.epic.followup.repository.managementSys.task.TaskRepository;
import com.epic.followup.repository.managementSys.task.TaskStatusRepository;
import com.epic.followup.service.managementSys.RoleService;
import com.epic.followup.service.managementSys.UniversityService;
import com.epic.followup.service.managementSys.UserService;
import com.epic.followup.service.managementSys.task.TaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class testManagement {

    @Autowired
    private RoleRepository RoleRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private RoleService roleService;

    @Autowired
    private UniversityService universityService;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;
    @Autowired
    private MiniScalePublicRepository miniScalePublicRepository;

    @Autowired
    private UniversityRepository universityRepository;


    @Test
    public void test() throws ParseException {
        System.out.println(universityRepository.findByUniversityName("华中科技大学").getUniversityId());
//        List<Object> data=taskStatusRepository.getTask1(9L);
//        System.out.println(data);
//        List<MiniScalePublicModel> data = miniScalePublicRepository.findAll();
//        for(MiniScalePublicModel u :data){
//            System.out.println(u.getMiniTime());
//        }
//        Object ou = userRepository.getUserInfoByTel("1");
//        Object[] user = (Object[]) ou;
//        JSONObject q=new JSONObject();
////        q.put("title","任务3");
////        q.put("content","这是任务3");
////        q.put("grade","2020");
////        q.put("scale_id","3");
//        q.put("task_id",4);
//        q.put("status",1);
//        q.put("user_id",1);
//        JSONObject res = taskService.findTask1((long) 1);
//        System.out.println(res);
//        for(Object u :user){
//            System.out.println(u);
//        }
        ////        q.put("remark","jjj111");
       // JSONObject res=userService.loginByTel(q);

        //System.out.println(res);

    }


}
