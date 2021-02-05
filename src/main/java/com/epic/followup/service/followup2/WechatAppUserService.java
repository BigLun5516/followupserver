package com.epic.followup.service.followup2;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.WechatUserModel;
import com.epic.followup.model.followup2.WechatAppUserModel;
import com.epic.followup.temporary.UnionIdRequest;
import com.epic.followup.util.AesUtil;

/**
 * @author : zx
 * @version V1.0
 */
public interface WechatAppUserService {

    WechatAppUserModel findByOpenId(String openid);

    <S extends WechatAppUserModel> boolean updateUser(S u);
}
