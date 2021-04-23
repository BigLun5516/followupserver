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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = "2021-02-10T16:00:00.000+0000";
        Date date = null;
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(date.getDate());
    }

    @Test
    public void testByte(){
        String str = "123";
        byte[] bytes = str.getBytes();
        System.out.println(bytes);
    }

    @Test
    public void testJSON(){

        String json = "{\"data\":\"1\"}";
        JSONObject jsonObject = JSONObject.parseObject(json);


        System.out.println(jsonObject);
        Integer data = jsonObject.getInteger("data");
        System.out.println(data);
    }

    @Test
    public void testAddUser(){
        JSONObject q=new JSONObject();
        q.put("password",333);
        q.put("tel",7);
        q.put("name","test");
        q.put("rid",21);
        q.put("uid",1);
        q.put("cid","2,3");
        userService.insertUser(q);
    }

}
