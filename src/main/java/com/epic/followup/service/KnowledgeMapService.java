package com.epic.followup.service;

/**
 * @author : zx
 * @version V1.0
 */
public interface KnowledgeMapService {


    /**
     * 获取知识图谱问答
     */
    String knowledgeMapAnswer(String sessionId, String question);
}
