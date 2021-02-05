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
    public String knowledgeMapAnswer(String sessionId, String question) {
        String tmp = update(sessionId);
        ChatRobotRequest req = new ChatRobotRequest();
        req.msg = question;
        req.sessionId = tmp;
        return this.restTemplate.postForObject(weChatConfig.getGuoguang()+QuestionAnswerUrl, req, String.class);
    }

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
