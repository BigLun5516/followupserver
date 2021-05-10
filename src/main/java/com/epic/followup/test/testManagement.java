package com.epic.followup.test;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.managementSys.RoleModel;
import com.epic.followup.repository.managementSys.RoleRepository;
import com.epic.followup.repository.managementSys.UserRepository;
import com.epic.followup.repository.managementSys.task.TaskRepository;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Autowired
    private TaskRepository taskRepository;


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

    @Test
    public void testfindListByUid(){
        List<Integer> l= new ArrayList<>();
        l.add(-1);
//        l.add(2);
        System.out.print(taskRepository.findListByUid(1,l).size());
    }

    @Test
    public void testRegix(){
        String htmlStr="<p><img src=\"http://follwup.cmas2020.cn/img/diaryImg/1618543088906-tmp_87cc43307e2e4bfa20eb7e55555caeca55d03bdbc02f36e4.jpg\"></p><hr><h2><br></h2><p><br></p><hr><ul data-checked=\"false\"><li><br></li></ul>";
        String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签
        System.out.println(htmlStr);
    }


}
