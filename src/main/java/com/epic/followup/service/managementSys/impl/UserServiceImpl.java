package com.epic.followup.service.managementSys.impl;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.conf.PatientConfig;
import com.epic.followup.model.managementSys.UserModel;
import com.epic.followup.repository.managementSys.UserRepository;
import com.epic.followup.service.managementSys.UserService;
import com.epic.followup.temporary.wechat.patient.diary.upLoadDiaryImgResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.math.BigInteger;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public JSONObject loginByTel(JSONObject loginParams, HttpServletRequest req) {

        String tel = loginParams.getString("tel");
        String password = loginParams.getString("password");

        JSONObject res = new JSONObject();

        HttpSession session = req.getSession();
        Object ou = userRepository.getUserByTel(tel);
        if(ou==null){
            res.put("errorCode", 502);
            res.put("errorMsg", "手机号有误");
        }
        else {
            Object[] user = (Object[]) ou;
            if(!user[2].equals(password)){
                res.put("errorCode", 502);
                res.put("errorMsg", "密码有误");
            } else {
                session.setAttribute("id", user[0]);
                session.setAttribute("tel", tel);
                res.put("sessionId", session.getId());
                res.put("errorCode", 200);
                res.put("errorMsg", "登录成功");
                res.put("imageUrl", user[1]);
                res.put("userName", user[4]);
                res.put("userType", user[7]);
                res.put("limit", user[8]);
            }
        }
        return res;
    }

    /**
     * 查询全部用户
     * @param
     * @return JSONObject
     */
    @Override
    public JSONObject findAllUsers(){
        JSONObject res = new JSONObject();
        List<Map<String, Object>> data = new ArrayList<>();
        List<Object> userlist=userRepository.getAllUser();
        for (Object o : userlist) {
            Map<String, Object> item = new HashMap<>();
            Object[] obj = (Object[]) o;
            item.put("id", obj[0]);
            item.put("imageUrl", obj[1]);
            item.put("password", obj[2]);
            item.put("tel", obj[3]);
            item.put("userName", obj[4]);
            item.put("universityId", obj[5]);
            item.put("userType", obj[6]);
            item.put("university", obj[7]);
            item.put("role", obj[8]);
            data.add(item);
        }
        res.put("errorCode", 200);
        res.put("errorMsg", "查询成功");
        res.put("data",data);
        return res;
    }

    /**
     * 删除用户
     * @param id
     * @return JSONObject
     */
    @Override
    public JSONObject deleteUser(Long id){
        JSONObject res = new JSONObject();

        try {
            userRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            res.put("errorCode", 500);
            res.put("errorMsg", "删除失败");
            return res;
        }

        res.put("errorCode", 200);
        res.put("errorMsg", "删除成功");
        return res;

    }


    /**
     * 新增用户
     * @param params
     * @return JSONObject
     */
    @Override
    public JSONObject insertUser(JSONObject params){

        JSONObject res = new JSONObject();

        UserModel userModel = new UserModel();
        userModel.setImageUrl("http://follwup.cmas2020.cn/img/diaryImg/1608689964159-tmp_05359ec4032c588ac2c397854ff6ce60e4ceab585b864509.jpg");
        userModel.setPassword(params.getString("password"));
        userModel.setTel(params.getString("tel"));
        userModel.setUserName(params.getString("name"));
        userModel.setUniversityId(params.getInteger("uid"));
        userModel.setUserType(params.getLong("rid"));

        userRepository.save(userModel);
        res.put("errorCode", 200);
        res.put("errorMsg", "插入成功");
        return res;
    }

    /**
     * 编辑用户
     * @param params
     * @return JSONObject
     */
    @Override
    public JSONObject editUser(JSONObject params){

        JSONObject res = new JSONObject();

        Long id=params.getLong("id");
        Optional<UserModel> u = userRepository.findById(id);
        if (!u.isPresent()){
            res.put("errorCode", 500);
            res.put("errorMsg", "编辑失败");
            return res;
        }
        UserModel userModel=u.get();
        userModel.setPassword(params.getString("password"));
        userModel.setTel(params.getString("tel"));
        userModel.setUserName(params.getString("name"));
        userModel.setUniversityId(params.getInteger("uid"));
        userModel.setUserType(params.getLong("rid"));

        userRepository.save(userModel);
        res.put("errorCode", 200);
        res.put("errorMsg", "编辑成功");
        return res;
    }

    @Override
    //个人资料
    public JSONObject personalInfo(HttpServletRequest req) {

        HttpSession session = req.getSession();
        JSONObject res = new JSONObject();
        String tel= (String) session.getAttribute("tel");
        Object ou = userRepository.getUserInfoByTel(tel);
        Object[] user = (Object[]) ou;
        Map<String, Object> item = new HashMap<>();
        item.put("img",user[1]);
        item.put("name",user[4]);
        item.put("userType",user[8]);
        item.put("phone",user[3]);
        item.put("password",user[2]);
        item.put("university",user[7]);
        res.put("errorCode", 200);
        res.put("errorMsg", "查询成功");
        res.put("data", item);
        return res;
    }

    @Override
    //上传个人头像
    public JSONObject uploadDiaryImg(MultipartFile file){
        JSONObject res=new JSONObject();
        if(file.getOriginalFilename().equals("")){
            res.put("errorCode", 502);
            res.put("errorMsg", "文件名不能为空");
            res.put("imgUrl","");
        }
        try {
            String path = PatientConfig.store_img;//"static/diaryImg";
            //获取文件名
            String fileName = file.getOriginalFilename();
            //创建一个UUID用时间戳表示
            String UUID = new Date().getTime() + "-";
            //组合成新文件名避免有重复的文件名
            String newFileName = UUID + fileName;
            File destFile = new File(new File(path).getAbsolutePath()+ "/" + newFileName);
            //判断该文件下的上级文件夹是否存在 不存在创建
            if(!destFile.getParentFile().exists()) {
                destFile.getParentFile().mkdirs();
            }
            //上传文件
            file.transferTo(destFile);//这一步结束就上传成功了。
            res.put("errorCode", 200);
            res.put("errorMsg", "上传成功");
            res.put("imgUrl", PatientConfig.img_query+newFileName);
        } catch (Exception e) {
            e.printStackTrace();
            res.put("errorCode", 500);
            res.put("errorMsg", "文件上传失败");
            res.put("imgUrl","");
        }
        return res;
    }

    //个人信息编辑
    public JSONObject personalInfoEdit(HttpServletRequest req, JSONObject params){
        HttpSession session = req.getSession();
        JSONObject res=new JSONObject();
        long i= ((BigInteger)session.getAttribute("id")).longValue();
        Long id=new Long(i);
        Optional<UserModel> u = userRepository.findById(id);
        UserModel user=u.get();
        user.setUserName(params.getString("userName"));
        user.setImageUrl(params.getString("imgUrl"));
        user.setPassword(params.getString("password"));
        userRepository.save(user);
        res.put("errorCode", 200);
        res.put("errorMsg", "编辑成功");
        return res;
    }
}
