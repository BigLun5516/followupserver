package com.epic.followup.test;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.managementSys.RoleModel;
import com.epic.followup.model.managementSys.UserModel;
import com.epic.followup.repository.managementSys.RoleRepository;
import com.epic.followup.repository.managementSys.UserRepository;
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
    private UserRepository userRepository;


    @Autowired
    private RoleService roleService;

    @Autowired
    private UniversityService universityService;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;



    @Test
    public void test() throws ParseException {
//        Object ou = userRepository.getUserInfoByTel("1");
//        Object[] user = (Object[]) ou;
        JSONObject q=new JSONObject();
//        q.put("title","任务3");
//        q.put("content","这是任务3");
//        q.put("grade","2020");
//        q.put("scale_id","3");
        q.put("task_id",4);
        q.put("status",1);
        q.put("user_id",1);
        JSONObject res = taskService.findTask1((long) 1);
        System.out.println(res);
//        for(Object u :user){
//            System.out.println(u);
//        }
        ////        q.put("remark","jjj111");
       // JSONObject res=userService.loginByTel(q);

        //System.out.println(res);

    }


}
