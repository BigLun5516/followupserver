package com.epic.followup.service.managementSys.impl.pic;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epic.followup.conf.FollowupStaticConfig;
import com.epic.followup.conf.WeChatConfig;
import com.epic.followup.model.managementSys.pic.PicModel;
import com.epic.followup.repository.managementSys.pic.PicRepository;
import com.epic.followup.service.managementSys.pic.PicService;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class PicServiceImpl implements PicService {

    @Autowired
    private PicRepository picRepository;

    @Autowired WeChatConfig weChatConfig;

    @Autowired
    private ApplicationContext applicationContext;

    private ExpiringMap<String, JSONObject> resultMap; //登录验证码
    public PicServiceImpl() {
        this.resultMap = ExpiringMap.builder()
                .maxSize(FollowupStaticConfig.MAX_USERNUM)
                .expiration(1, TimeUnit.HOURS)
                .expirationPolicy(ExpirationPolicy.ACCESSED)
                .variableExpiration()
                .build();
    }

    @Override
    public JSONObject findPic(JSONObject params) {

        JSONObject res = new JSONObject();

        String picName = params.getString("picName");
        String picType = params.getString("picType");
        String picStatus = params.getString("picStatus");
        JSONArray filterDates = params.getJSONArray("filterDates");
        String minDate, maxDate;

        Integer pageNum = params.getInteger("pageNum");
        Integer pageSize = params.getInteger("pageSize");

        if(picName == "" || picName == null ){
            picName = "%%";
        } else {
            picName = "%" + picName + "%";
        }
        if(picType == "" || picName == null ){
            picType = "%%";
        }
        if(picStatus == "" || picName == null ){
            picStatus = "%%";
        }
        if(filterDates == null ){
            minDate = "1900-1-1";
            maxDate = "3000-1-1";
        } else {
            minDate = (String) filterDates.get(0);
            maxDate = (String) filterDates.get(1);
        }

        List<PicModel> picByQuery = picRepository.findPicByQuery(
                picName, picType, picStatus, minDate, maxDate, PageRequest.of(pageNum - 1, pageSize)
        );

        // 返回参数：totalNum
        res.put("totalNum", picRepository.countPicByQuery(
                picName, picType, picStatus, minDate, maxDate
        ));

        // 返回参数：data
        List<Map<String, Object>> data = new ArrayList<>();
        for (PicModel picModel : picByQuery) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", picModel.getPicId());
            item.put("picName", picModel.getPicName());
            item.put("picType", picModel.getPicType());
            item.put("picStatus", picModel.getPicStatus());
            item.put("createTime", picModel.getCreateTime());
            String picDetailsStr = null;
            if (picModel.getPicDetails() != null){
                picDetailsStr = new String(picModel.getPicDetails());
            }
            item.put("picDetails", picDetailsStr);
            data.add(item);
        }
        res.put("data", data);

        res.put("errorCode", 200);
        res.put("errorMsg", "查询成功");
        return res;
    }

    @Override
    public JSONObject insertPic(JSONObject params) {

        // 获取请求参数
        String picName = params.getString("picName");
        String picType = params.getString("picType");
        String picStatus = params.getString("picStatus");
        String picDetailsStr = params.getString("picDetails");
        String createTime = params.getString("createTime");

        JSONObject res = new JSONObject();

        PicModel picModel = new PicModel();
        picModel.setPicName(picName);
        picModel.setPicType(picType);
        picModel.setPicStatus(picStatus);
        if (picDetailsStr != null && picDetailsStr != ""){
            picModel.setPicDetails(picDetailsStr.getBytes());
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (createTime != "") {
            try {
                picModel.setCreateTime(dateFormat.parse(createTime));
            } catch (ParseException e) {
                e.printStackTrace();
                res.put("errorCode", 500);
                res.put("errorMsg", "日期格式错误");
                return res;
            }
        }

        picRepository.save(picModel);

        res.put("errorCode", 200);
        res.put("errorMsg", "插入成功");
        return res;

    }

    @Override
    public JSONObject deletePic(JSONObject params) {

        // 获取参数
        Long id = params.getLong("id");

        JSONObject res = new JSONObject();

        try {
            picRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            res.put("errorCode", 502);
            res.put("errorMsg", "id错误，没有这个图文");
            return res;
        }

        res.put("errorCode", 200);
        res.put("errorMsg", "删除成功");
        return res;
    }

    @Override
    public JSONObject editPic(JSONObject params) {

        // 获取请求参数
        Long id = params.getLong("id");
        String picName = params.getString("picName");
        String picType = params.getString("picType");
        String picStatus = params.getString("picStatus");
        String picDetailsStr = params.getString("picDetails");

        JSONObject res = new JSONObject();

        Optional<PicModel> optional = picRepository.findById(id);
        if (!optional.isPresent()){
            res.put("errorCode", 502);
            res.put("errorMsg", "没有这个图文");
            return res;
        }
        PicModel picModel = optional.get();
        picModel.setPicName(picName);
        picModel.setPicType(picType);
        picModel.setPicStatus(picStatus);
        if (picDetailsStr != null && picDetailsStr != ""){
            picModel.setPicDetails(picDetailsStr.getBytes());
        } else {
            picModel.setPicDetails(null);
        }

        picRepository.save(picModel);

        res.put("errorCode", 200);
        res.put("errorMsg", "编辑成功");
        return res;
    }

    // 获取token接口
    @Override
    public JSONObject getToken(JSONObject params) throws Exception {
        String official_account = params.getString("official_account");
        //优化一下，防止每次都去查token
        if(this.resultMap.get(official_account) != null){
            return this.resultMap.get(official_account);
        }
        String type = params.getString("type");
        int offset = params.getInteger("offset");
        int count = params.getInteger("count");
        String appidMethod = "get" + official_account + "_appID";
        String appSecertMethod = "get" + official_account + "_appSecert";
        // 反射获取相应的方法(必须要用applicationContext，否则反射失败）
        Class clazz = Class.forName("com.epic.followup.conf.WeChatConfig");
        Object bean = applicationContext.getBean(clazz);
        System.out.println("反射类");
        System.out.println(clazz);
        //直接java反射得到方法
        Method idMethod= clazz.getMethod(appidMethod);
        Method secretMethod= clazz.getMethod(appSecertMethod);
        System.out.println("反射方法");
        System.out.println(idMethod);
        System.out.println(secretMethod);
        Object appid = idMethod.invoke(bean);
        Object appSecert = secretMethod.invoke(bean);
        System.out.println("appid： " + appid);
        System.out.println("appSecert：" + appSecert);
        //先请求获取token
        RestTemplate restTemplate = new RestTemplate();
        // 中文乱码，主要是 StringHttpMessageConverter的默认编码为ISO导致的
        List<HttpMessageConverter<?>> list = restTemplate.getMessageConverters();
        for (HttpMessageConverter converter : list) {
            if (converter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) converter).setDefaultCharset(Charset.forName("UTF-8"));
                break;
            }
        }
        System.out.println("第一次请求");
        ResponseEntity<JSONObject> forEntity = restTemplate.getForEntity("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid + "&secret=" + appSecert, JSONObject.class);
        JSONObject body = forEntity.getBody();
        System.out.println("access_token如下:");
        System.out.println(body.getString("access_token"));
        String token = body.getString("access_token");
        //再请求获取item
        JSONObject req = new JSONObject();
        req.put("type",type);
        req.put("offset",offset);
        req.put("count",count);
        System.out.println("第二次请求");
        String res = restTemplate.postForObject("https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=" + token,req,String.class);
        System.out.println(res);
        JSONObject resObject =JSONObject.parseObject(res);
        List<JSONObject> listRes = new ArrayList<>();
        for(int i = 0; i < resObject.getJSONArray("item").size(); i++) {
            for (int j = 0; j < ((JSONObject)resObject.getJSONArray("item").get(i)).getJSONObject("content").getJSONArray("news_item").size(); j++) {
                JSONObject jsTemp = (JSONObject)(((JSONObject)resObject.getJSONArray("item").get(i)).getJSONObject("content").getJSONArray("news_item").get(j));
                JSONObject temp = new JSONObject();
                temp.put("thumb_url",jsTemp.getString("thumb_url"));
                temp.put("title",jsTemp.getString("title"));
                temp.put("url",jsTemp.getString("url"));
                temp.put("digest",jsTemp.getString("digest"));
                listRes.add(temp);
            }
        }
        JSONObject finalRes = new JSONObject();
        finalRes.put("resultList",listRes);
        this.resultMap.put(official_account,finalRes);
        return finalRes;
    }
}
