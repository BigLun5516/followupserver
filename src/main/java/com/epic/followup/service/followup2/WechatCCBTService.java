package com.epic.followup.service.followup2;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.temporary.followup2.session.BaseUserSession;

/**
 * 小程序CCBT部分
 */
public interface WechatCCBTService {

    /**
     * 存储用户提交的答案
     * @param param
     * @param session
     * @return
     */
    JSONObject saveCCBTData(JSONObject param, BaseUserSession session);

    /**
     * 获取用户提交的答案
     * @param param
     * @param session
     * @return
     */
    JSONObject getCCBTData(JSONObject param, BaseUserSession session);

    /**
     * 存储用户目前进行的模块标号
     * @param param
     * @param session
     * @return
     */
    JSONObject saveCCBTProgess(JSONObject param, BaseUserSession session);

    /**
     * 获取用户目前进行的模块标号
     * @param param
     * @param session
     * @return
     */
    JSONObject getCCBTProgess(JSONObject param, BaseUserSession session);
}
