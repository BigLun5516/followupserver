package com.epic.followup.service.impl;


import com.epic.followup.conf.FollowupStaticConfig;
import com.epic.followup.conf.WeChatConfig;
import com.epic.followup.service.KnowledgeMapService;

import com.epic.followup.temporary.followup2.ChatRobotRequest;
import com.epic.followup.util.UUIDUtil;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author : zx
 * @version V1.0
 */

@Service
public class KnowledgeMapServiceImpl implements KnowledgeMapService {


    private org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());

    private RestTemplate restTemplate;
    private WeChatConfig weChatConfig;

    private static final String QuestionAnswerUrl = "";
    private ExpiringMap<String, String> sessionIdMap;


    @Autowired
    public KnowledgeMapServiceImpl(RestTemplate restTemplate, WeChatConfig weChatConfig){
        this.restTemplate = restTemplate;
        this.weChatConfig = weChatConfig;
        this.sessionIdMap = ExpiringMap.builder()
                .maxSize(FollowupStaticConfig.MAX_USERNUM)
                .expiration(10, TimeUnit.MINUTES) // 1分钟有效
                .expirationPolicy(ExpirationPolicy.ACCESSED)
                .variableExpiration()
                .build();
    }

    @Override
    public HttpEntity<String> knowledgeMapAnswer(String session, String question,String pattern ,String location) {
        String tmp = update(session);
        ChatRobotRequest req = new ChatRobotRequest();
        req.msg = question;
        req.pattern = pattern;
        req.location = location;

        HttpHeaders headers = new HttpHeaders();
        if(session!=null && !"".equals(session)){
            headers.set("cookie", "session="+session);
        }
        HttpEntity<ChatRobotRequest> r = new HttpEntity<ChatRobotRequest>(req, headers);
//        return this.restTemplate.postForObject(weChatConfig.getGuoguang()+QuestionAnswerUrl, r, String.class);
        HttpEntity<String> response =  this.restTemplate.exchange(weChatConfig.getGuoguang()+QuestionAnswerUrl, HttpMethod.POST, r, String.class);
        return response;
    }


    // @TODO
    private String update(String sessionId){
        if (this.sessionIdMap.containsKey(sessionId)){
            String tmp = sessionIdMap.get(sessionId);
            this.sessionIdMap.remove(sessionId);
            this.sessionIdMap.put(sessionId, tmp);
            return tmp;
        }else {
            String tmp = UUIDUtil.getUUID();
            this.sessionIdMap.put(sessionId, tmp);
            return tmp;
        }
    }
}
