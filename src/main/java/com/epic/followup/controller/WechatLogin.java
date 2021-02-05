package com.epic.followup.controller;

import static com.epic.followup.util.HTTPsUtils.get;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epic.followup.conf.WeChatConfig;
import com.epic.followup.model.WechatUserModel;
import com.epic.followup.service.WechatUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author : zx
 * @version V1.0
 */

@Controller
@RequestMapping("/wechat/login")
public class WechatLogin {

    @Autowired
    private WeChatConfig weChatConfig;

    @Autowired
    private WechatUserService wechatUserService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public String getLoginStatus(@RequestParam(value = "code", required = true) String code){


        Map<String, String> params = new HashMap<String, String>();
        params.put("secret", weChatConfig.getAppSecert());
        params.put("appid", weChatConfig.getAppID());
        params.put("js_code", code);
        params.put("grant_type", weChatConfig.getGrantType());

        String result = null;
        try {
            result = get(weChatConfig.getUrl(), params, null);
        }catch (Exception e){
            e.printStackTrace();
        }

        JSONObject r = JSON.parseObject(result);
        if (r.getString("openid") != null){

            WechatUserModel u = wechatUserService.findByOpenId(r.getString("openid"));
            if (u!=null){
                u.setSessionKey(r.getString("session_key"));
                u.setOpenId(r.getString("openid"));
            }else {
                u = new WechatUserModel();
                u.setSessionKey(r.getString("session_key"));
                u.setOpenId(r.getString("openid"));
            }

            wechatUserService.updateUser(u);

            r.put("errorCode", 200);
            return JSON.toJSONString(r);
        }else {
            return result;
        }

    }
}
