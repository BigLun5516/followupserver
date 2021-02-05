package com.epic.followup.service.officialwechat;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.WechatUserModel;
import com.epic.followup.model.sumian.SumianOrderModel;
import com.epic.followup.temporary.sumian.SumianNoifyRequest;
import com.epic.followup.temporary.sumian.TokenResponse;
import org.springframework.http.HttpRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : zx
 * @version V1.0
 */
public interface SumianService {

    /*
     * 必经之路，校验后进行注册和购买服务
     */
    TokenResponse getToken(WechatUserModel u);
    Boolean regist(WechatUserModel u);

    SumianOrderModel createOrder(WechatUserModel u);

    /*
     * 对速眠平台消息进行头部校验
     */
    Boolean isLegal (HttpServletRequest req);

    /*
     * 转发速眠平台消息
     */
    boolean transmitNotify(SumianNoifyRequest s);
}
