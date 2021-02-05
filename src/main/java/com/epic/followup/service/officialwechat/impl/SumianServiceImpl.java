package com.epic.followup.service.officialwechat.impl;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.conf.WeChatConfig;
import com.epic.followup.model.WechatUserModel;
import com.epic.followup.model.sumian.SumianOrderModel;
import com.epic.followup.repository.WechatUserRepository;
import com.epic.followup.repository.ncov.SumianModelRepository;
import com.epic.followup.service.officialwechat.SumianService;
import com.epic.followup.temporary.sumian.SumianNoifyRequest;
import com.epic.followup.temporary.sumian.TokenResponse;
import com.epic.followup.temporary.sumian.wechatnoifyrequest.*;
import com.epic.followup.util.AesUtil;
import com.epic.followup.util.SumianHttpsUtils;
import com.epic.followup.util.UUIDUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.epic.followup.util.HTTPsUtils.getRequestBody;

/**
 * @author : zx
 * @version V1.0
 */

@Service
public class SumianServiceImpl implements SumianService {

    private WechatUserRepository wechatUserRepository;
    private WeChatConfig weChatConfig;
    private SumianModelRepository sumianModelRepository;

    private String access_token;
    // 获取token时间
    private Long access_time;
    private static final String uri = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";
    private static final String postNooifyUri = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";
    private static final String JUMPURL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx3213c" +
            "681fb752c01&redirect_uri=https%3A%2F%2Ffollwup.aiwac.net%2Fsumian%2Fauth&response_type=code&" +
            "scope=snsapi_userinfo&state=123#wechat_redirect";

    private org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String[] noifyMeesageIds = new String[]{
        "dZLqtGrGhiS_tX620Xp9HBiOihsiRNQTfrVx0MIdobI", "FSzTilWXyy60mCkfHc84khtGbKVXtHLidbhHbKg3rHc",
            "CgBe0mCvxozouzv-jlXI9Yoxi9CyOAhNFkqEY6cmtRo", "kEF58O-RG9DELuZfVRnoBKJnq8fMpnzfTzX8YDnGFlg",
            "kEF58O-RG9DELuZfVRnoBKJnq8fMpnzfTzX8YDnGFlg", "LhdGaRdbOqjM819FXd8M6uli2mcAQRu0WAUt8kOpAq0",
            "kEF58O-RG9DELuZfVRnoBKJnq8fMpnzfTzX8YDnGFlg", "kEF58O-RG9DELuZfVRnoBKJnq8fMpnzfTzX8YDnGFlg"
    };

    @Autowired
    public SumianServiceImpl(WechatUserRepository wechatUserRepository, WeChatConfig weChatConfig, SumianModelRepository sumianModelRepository){
        this.weChatConfig = weChatConfig;
        this.wechatUserRepository = wechatUserRepository;
        this.sumianModelRepository = sumianModelRepository;
    }

    private void getWechatAccessToken(){
        RestTemplate t = new RestTemplate();
        String result = t.getForObject(uri+"&appid="+weChatConfig.getOfficialID()+"&secret="+weChatConfig.getOfficialSecert(), String.class);
        JSONObject j = JSONObject.parseObject(result);
        this.access_token = j.getString("access_token");
        this.access_time = new Date().getTime();
    }

    private Boolean isTimeOut(){
        return ((new Date().getTime()/1000 - this.access_time/1000) > 7000);
    }

    @Override
    public TokenResponse getToken(WechatUserModel u) {

        if (u.getSumianRegisted() == 0){
            if (this.regist(u)){
                this.createOrder(u);
            }
        }

        // 查找订单
        if (!this.sumianModelRepository.findByUserIdAndIsSucceed(u.getUserId(), 1).isPresent()){
            this.createOrder(u);
        }

        // 回复token和expired_at

        Map<String, String> params = new HashMap<String, String>();
        String time = Long.toString(new Date().getTime()/1000);
        String uuid = UUIDUtil.getUUID();
        params.put("App-Key", weChatConfig.getSumianAppKey());
        params.put("Nonce", uuid);
        params.put("Timestamp", time);
        params.put("Signature", DigestUtils.sha1Hex(weChatConfig.getSumianAppSecret()+uuid+time));

        Map<String, String> bodyParams = new HashMap<String, String>();
        bodyParams.put("user_id", u.getUserId().toString());

        JSONObject j = JSONObject.parseObject(SumianHttpsUtils.proxyStringSumianHttpRequest(
                weChatConfig.getSumianUrl()+weChatConfig.getSumianTokenUrl(), "POST", params,
                getRequestBody(bodyParams)));
        TokenResponse t = new TokenResponse();
        t.setExpired_at(j.getString("expired_at"));
        t.setToken(j.getString("token"));

        return t;

    }

    @Override
    public Boolean regist(WechatUserModel u) {
        Map<String, String> params = new HashMap<String, String>();
        String time = Long.toString(new Date().getTime()/1000);
        String uuid = UUIDUtil.getUUID();
        params.put("App-Key", weChatConfig.getSumianAppKey());
        params.put("Nonce", uuid);
        params.put("Timestamp", time);
        params.put("Signature", DigestUtils.sha1Hex(weChatConfig.getSumianAppSecret()+uuid+time));

        Map<String, String> bodyParams = new HashMap<String, String>();
        bodyParams.put("user_id", u.getUserId().toString());
        if (SumianHttpsUtils.postUserId(weChatConfig.getSumianUrl()+weChatConfig.getSumianUserRegistUurl(), params, bodyParams)){
            u.setSumianRegisted(1);
            wechatUserRepository.save(u);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public SumianOrderModel createOrder(WechatUserModel u) {
        if (u.getSumianRegisted() == 1){
            SumianOrderModel s = new SumianOrderModel();
            s.setOrderTime(new Date());
            s.setUserId(u.getUserId());
            s = sumianModelRepository.save(s);

            Map<String, String> params = new HashMap<String, String>();
            String time = Long.toString(new Date().getTime()/1000);
            String uuid = UUIDUtil.getUUID();
            params.put("App-Key", weChatConfig.getSumianAppKey());
            params.put("Nonce", uuid);
            params.put("Timestamp", time);
            params.put("Signature", DigestUtils.sha1Hex(weChatConfig.getSumianAppSecret()+uuid+time));

            Map<String, String> bodyParams = new HashMap<String, String>();
            bodyParams.put("user_id", u.getUserId().toString());
            bodyParams.put("order_no", s.getId().toString());
            if (SumianHttpsUtils.postUserId(weChatConfig.getSumianUrl()+ "/c/scenarios/orders", params, bodyParams)){
                s.setIsSucceed(1);
            }
            sumianModelRepository.save(s);
            return s;
        }else {
            return null;
        }

    }

    @Override
    public Boolean isLegal(HttpServletRequest req) {
        String Nonce = req.getHeader("Nonce");
        String Timestamp = req.getHeader("Timestamp");
        String Signature = req.getHeader("Signature");

        log.info("Nonce: " + Nonce);
        log.info("Timestamp: " + Timestamp);
        log.info("Sinn: " + Signature);
        log.info("mySign: " + DigestUtils.sha1Hex(weChatConfig.getSumianAppSecret()+Nonce+Timestamp));
        return Signature.equals(DigestUtils.sha1Hex(weChatConfig.getSumianAppSecret()+Nonce+Timestamp));
    }

    @Override
    public boolean transmitNotify(SumianNoifyRequest s) {
        if (this.access_token == null || isTimeOut()){
            getWechatAccessToken();
        }

        // 发给微信的请求体
        JsonRootBean q = new JsonRootBean();

        // 获取openid
        Optional<WechatUserModel> ou = wechatUserRepository.findByUserId(Long.valueOf(s.getUserId()));
        if (ou.isPresent()){
            q.setTouser(ou.get().getOpenId());
        }else {
            return false;
        }

        q.setTemplate_id(noifyMeesageIds[Integer.valueOf(s.getType())]);
        q.setUrl(s.getUrl());

//        Miniprogram m = new Miniprogram();
//        m.setAppid(weChatConfig.getAppID());
//        q.setMiniprogram(m);

        Data d = new Data();
        First f = new First();
        f.setValue(s.getFirst());
        d.setFirst(f);

        Keyword1 keyword1 = new Keyword1();
        keyword1.setValue(s.getKeyword1());
        d.setKeyword1(keyword1);

        Keyword2 keyword2 = new Keyword2();
        keyword2.setValue(s.getKeyword2());
        d.setKeyword2(keyword2);

        Keyword3 keyword3 = new Keyword3();
        keyword3.setValue(s.getKeyword3());
        d.setKeyword3(keyword3);

        Remark remark = new Remark ();
        remark.setValue(s.getRemark());
        d.setRemark(remark);

        q.setData(d);

        RestTemplate t = new RestTemplate();
        String result = t.postForObject(postNooifyUri+this.access_token, q, String.class);
        log.info(result);
        log.info(q.getUrl());
        JSONObject j = JSONObject.parseObject(result);
        if (j.getInteger("errcode") == 0){
            return true;
        }
        return false;
    }
}
