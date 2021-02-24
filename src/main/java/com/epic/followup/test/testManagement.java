package com.epic.followup.test;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.managementSys.RoleModel;
import com.epic.followup.repository.managementSys.RoleRepository;
import com.epic.followup.repository.managementSys.UserRepository;
import com.epic.followup.service.managementSys.RoleService;
import com.epic.followup.service.managementSys.UniversityService;
import com.epic.followup.service.managementSys.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class testManagement {

    @Autowired
    private RoleRepository RoleRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UniversityService universityService;

    @Autowired
    private UserService userService;


    @Test
    public void test() {
        JSONObject q=new JSONObject();
        q.put("tel","111111");
        q.put("password","1234");
////        q.put("remark","jjj111");
       // JSONObject res=userService.loginByTel(q);

        //System.out.println(res);

    }


}
