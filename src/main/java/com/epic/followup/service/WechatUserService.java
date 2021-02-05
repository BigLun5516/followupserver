package com.epic.followup.service;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.WechatUserModel;
import com.epic.followup.temporary.UnionIdRequest;
import com.epic.followup.util.AesUtil;

/**
 * @author : zx
 * @version V1.0
 */
public interface WechatUserService {

    WechatUserModel findByOpenId(String openid);

    boolean checkUser(String openId, String sessionId);

    <S extends WechatUserModel> boolean updateUser(S u);

    /*
     * 获取unionid
     */
    static String getUnionId(UnionIdRequest u){
        try{
            JSONObject j = JSONObject.parseObject(AesUtil.decrypt(u.getEncryptedData(), u.getSession_key(), u.getIv(), "UTF-8"));
            return j.getString("openId");
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /*
     * 设置速眠跳转url 缓存
     */
    String getFrom(String uuid);

    String setFrom(String url);
}
