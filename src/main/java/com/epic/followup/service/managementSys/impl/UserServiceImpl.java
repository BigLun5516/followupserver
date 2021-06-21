package com.epic.followup.service.managementSys.impl;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.conf.PatientConfig;
import com.epic.followup.model.managementSys.CollegeModel;
import com.epic.followup.model.managementSys.DataPermissionModel;
import com.epic.followup.model.managementSys.UniversityModel;
import com.epic.followup.model.managementSys.UserModel;
import com.epic.followup.repository.managementSys.CollegeRepository;
import com.epic.followup.repository.managementSys.DataPermissionRepository;
import com.epic.followup.repository.managementSys.UniversityRepository;
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
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private CollegeRepository collegeRepository;

    @Autowired
    private DataPermissionRepository dataPermissionRepository;

    @Override
    public JSONObject loginByTel(JSONObject loginParams, HttpServletRequest req) {

        String tel = loginParams.getString("tel");
        String password = loginParams.getString("password");

        JSONObject res = new JSONObject();

        HttpSession session = req.getSession();

        List<Object> userlist = userRepository.getUserByTel(tel);
        if(userlist.isEmpty()){
            res.put("errorCode", 502);
            res.put("errorMsg", "手机号有误");
        }
        else {
            Object[] user = (Object[]) userlist.get(0);
            if(!user[2].equals(password)){
                res.put("errorCode", 502);
                res.put("errorMsg", "密码有误");
            } else {
                //获取学院id集合
                List<Integer> collegeIds=new ArrayList<>();
                for (Object o : userlist) {
                    Object[] obj = (Object[]) o;
                    collegeIds.add((Integer) obj[9]);
                }
                session.setAttribute("id",  user[0]);
                session.setAttribute("tel", tel);
                session.setAttribute("universityId", user[8]);
                session.setAttribute("collegeId", collegeIds);
//                List<Integer> list=(List<Integer>)session.getAttribute("collegeId");
//                for(Integer i:list){
//                    System.out.print(i+",");
//                }
                res.put("sessionId", session.getId());
                res.put("errorCode", 200);
                res.put("errorMsg", "登录成功");
                res.put("imageUrl", user[1]);
                res.put("userName", user[4]);
                res.put("userType", user[6]);
                res.put("limit", user[7]);
                res.put("visible", user[10]);
            }
        }
        return res;
    }

    public static String getType(Object o){ //获取变量类型方法
        return o.getClass().toString(); //使用int类型的getClass()方法
    }

    /**
     * 根据当前登录用户的学校信息查询能看到的全部用户
     * @param
     * @return JSONObject
     */
    @Override
    public JSONObject findAllUsers(HttpSession session){
        Integer universityId= (Integer) session.getAttribute("universityId");
        JSONObject res = new JSONObject();
        List<Map<String, Object>> data = new ArrayList<>();
        List<Object> userlist=userRepository.getAllUser(universityId);
        for (Object o : userlist) {
            Map<String, Object> item = new HashMap<>();
            Object[] obj = (Object[]) o;
            item.put("id", obj[0]);
            item.put("imageUrl", obj[1]);
            item.put("password", obj[2]);
            item.put("tel", obj[3]);
            item.put("userName", obj[4]);
//            item.put("universityId", obj[5]);
//            item.put("userType", obj[6]);
            if(obj[5]==null){
                item.put("university", "全部");
            }else{
                item.put("university", obj[5]);
            }
            item.put("role", obj[6]);
//            item.put("collegeId", obj[9]);
            if(obj[7]==null){
                item.put("college", "全部");
            }else{
                item.put("college", obj[7]);
            }
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
            userRepository.deleteById(id);//用户删除以后，用户权限表中也要删除
            userRepository.deletePermission(id);
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
        userModel.setImageUrl("http://119.3.234.22:8013/img/diaryImg/1608689964159-tmp_05359ec4032c588ac2c397854ff6ce60e4ceab585b864509.jpg");
        userModel.setPassword(params.getString("password"));
        userModel.setTel(params.getString("tel"));
        userModel.setUserName(params.getString("name"));
        userModel.setUserType(params.getLong("rid"));
        userModel.setVisible(1);//代表能看到学生评测数据
        UserModel temp=userRepository.save(userModel);
        String cids=params.getString("cid");
        String[] colleges=cids.split(",");
        for(String cid:colleges){
            DataPermissionModel d=new DataPermissionModel();
            d.setUniversityId(params.getInteger("uid"));
            d.setCollegeId(Integer.parseInt(cid));
            d.setUserId(temp.getUserId());
            dataPermissionRepository.save(d);
        }
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
        long i= ((BigInteger)session.getAttribute("id")).longValue();
        Long id=new Long(i);
//        System.out.println("学校id:"+session.getAttribute("universityId"));
//        System.out.println("学院id:"+session.getAttribute("collegeId"));
        Object ou = userRepository.getUserInfoById(id);
        Object[] user = (Object[]) ou;
        Map<String, Object> item = new HashMap<>();
        item.put("img",user[1]);
        item.put("name",user[4]);
        item.put("userType",user[6]);
        item.put("phone",user[3]);
        item.put("password",user[2]);
        item.put("university",user[5]);
        item.put("college",user[7]);
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
    @Override
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

    //Mini结果展示（包括不重复的展示和个人的展示）
    @Override
    public JSONObject getMiniResult(Integer userUniversityId, List<Integer> userCollegeIdList, String stid){
        //stid为空代表查询展示（不重复），不为空代表查看个人mini结果
        if(stid==null){
            stid="";
        }
        JSONObject res=new JSONObject();
        List<Object> miniList=userRepository.getMiniResult(userUniversityId, userCollegeIdList,stid);
        List<Map<String, Object>> data = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Set<String> label= new HashSet<>();
        for (Object o : miniList) {
            Map<String, Object> item = new HashMap<>();
            Object[] obj = (Object[]) o;
            item.put("stname", obj[0]);
            item.put("stid", obj[1]);
            item.put("age", obj[2]);
            item.put("department", obj[3]);
            item.put("college", obj[4]);
            item.put("stype", obj[5]);
            item.put("year", obj[6]);
            item.put("mini_time", dateFormat.format(obj[7]));
            item.put("mini_result", obj[8]);
            if(!"".equals(stid)){
                data.add(item);
                continue;
            }
            if(!label.contains(obj[1])) {
                data.add(item);
                label.add((String) obj[1]);
            }
        }
        res.put("data",data);
        res.put("errorCode", 200);
        res.put("errorMsg", "查询成功");
        return res;
    }

    //获取当前登录后台用户的学校的所有注册账号
    @Override
    public JSONObject getRegisterUser(Integer universityId){
        JSONObject res=new JSONObject();
        List<Object> userList=userRepository.getRegisterUser(universityId);
        List<Map<String, Object>> data = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Object o : userList) {
            Map<String, Object> item = new HashMap<>();
            Object[] obj = (Object[]) o;
            item.put("department", obj[0]);
            item.put("college", obj[1]);
            item.put("stid", obj[2]);
            item.put("tel", obj[3]);
            item.put("province", obj[4]);
            item.put("stname", obj[5]);
            item.put("stype", obj[6]);
            item.put("year", obj[7]);
            item.put("create_time", dateFormat.format(obj[8]));
            data.add(item);
        }
        res.put("data",data);
        res.put("errorCode", 200);
        res.put("errorMsg", "查询成功");
        return res;
    }
}
