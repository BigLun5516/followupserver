package com.epic.followup.service;

import org.springframework.http.HttpEntity;

/**
 * @author : zx
 * @version V1.0
 */
public interface KnowledgeMapService {


    /**
     * 获取知识图谱问答
     */
    HttpEntity<String> knowledgeMapAnswer(String sessionId, String question, String pattern, String location);
}
