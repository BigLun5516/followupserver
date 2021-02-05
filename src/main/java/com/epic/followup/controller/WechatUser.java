package com.epic.followup.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epic.followup.conf.WeChatConfig;
import com.epic.followup.model.WechatUserModel;
import com.epic.followup.service.WechatUserService;
import com.epic.followup.service.officialwechat.SumianService;
import com.epic.followup.temporary.DealMessageResponse;
import com.epic.followup.temporary.InfoinorupRequest;
import com.epic.followup.temporary.NormalUserRequest;
import com.epic.followup.temporary.UnionIdRequest;
import com.epic.followup.temporary.ncov.GetScaleResultResponse;
import com.epic.followup.temporary.sumian.TokenResponse;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Date;

/**
 * @author : zx
 * @version V1.0
 */

@Controller
@RequestMapping("/wechat/user")

public class WechatUser {

    @Autowired
    WechatUserService wechatUserService;
    @Autowired
    WeChatConfig weChatConfig;
    @Autowired
    SumianService sumianService;

    private org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/infoinorup", method = RequestMethod.POST)
    @ResponseBody
    public String updateWechatUser(@RequestBody InfoinorupRequest info){
        DealMessageResponse dm = new DealMessageResponse();
        if (wechatUserService.checkUser(info.getOpenid(), info.getSessionkey())){
            WechatUserModel u = wechatUserService.findByOpenId(info.getOpenid());
            try {
                u.setBirthday(new Date(Long.valueOf(info.getBirthday())));
            }catch (Exception e ){
                e.printStackTrace();
            }

            u.setGender(info.getGender());
            u.setTabs(info.getTabs());
            u.setUserName(info.getUsername());
            if(wechatUserService.updateUser(u)){
                dm.setErrorCode(200);
                dm.setErrorMsg("succ");
                return JSON.toJSONString(dm);
            }else {
                dm.setErrorCode(505);
                dm.setErrorMsg("伺服器内部错误");
                return JSON.toJSONString(dm);
            }
        }else {
            dm.setErrorCode(500);
            dm.setErrorMsg("用户登录状态异常");
            return JSON.toJSONString(dm);
        }
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    public String getUserInfo(@RequestParam(value = "openid", required = true) String openId,
                              @RequestParam(value = "session_key", required = true) String sessionKey){
        if (wechatUserService.checkUser(openId, sessionKey)){

            WechatUserModel u = wechatUserService.findByOpenId(openId);

            JSONObject r = new JSONObject();
            r.put("errorCode", 200);
            r.put("errorMessage", "succ");
            r.put("gender", u.getGender());
            r.put("birthday", Long.toString(u.getBirthday().getTime()));
            r.put("tabs", u.getTabs());

            return JSON.toJSONString(r);
        }else {
            DealMessageResponse dm = new DealMessageResponse();
            dm.setErrorCode(500);
            dm.setErrorMsg("用户登录状态异常");
            return JSON.toJSONString(dm);
        }
    }

    /*
     * 获取医护人员类型
     */
    @RequestMapping(value = "/doctor_info", method = RequestMethod.GET)
    public String getDoctorType(@RequestParam("code") String code,@RequestParam("state") int state){


        log.info("/wechat/user/doctor_info: code="+code + "&stat=" + state);
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + weChatConfig.getOfficialID() +
                "&secret=" + weChatConfig.getOfficialSecert() + "&code="+ code + "&grant_type=authorization_code";
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);
        log.info("/wechat/user/doctor_info"+result);
        JSONObject j = JSONObject.parseObject(result);
        WechatUserModel u =new WechatUserModel();
        if (j.getString("openid") != null) {
            u = wechatUserService.findByOpenId(j.getString("openid"));
            if (u == null) {
                u = new WechatUserModel();
                u.setOpenId(j.getString("openid"));
                if (state == 100){
                    // 100表示非用户信息上传只填表
                }else {
                    u.setDoctorType(state);
                }
                // 公众号用accesstoken替代sessionkey
                u.setSessionKey(j.getString("access_token"));
                wechatUserService.updateUser(u);
            }else {
                if (state == 100){
                    // 100表示非用户信息上传只填表
                }else {
                    u.setDoctorType(state);
                }
                u.setSessionKey(j.getString("access_token"));
                wechatUserService.updateUser(u);
                u = wechatUserService.findByOpenId(u.getOpenId());
            }
        }else {
            log.error("/wechat/user/doctor_info"+"openid为空");
        }
        // 跳转到填表
        String jump;
        if (state == 100){
            jump = "redirect:" + "https://cdn.cmas2020.cn/h5/dist/index.html?" +
                    "openid="+u.getOpenId()+
                    "&sessionkey="+u.getSessionKey()+
                    "&doctortype="+u.getDoctorType();
        }else {
            jump = "redirect:" + "https://cdn.cmas2020.cn/h5/Queweb/web/welcome.html";
        }

        log.info(jump);
        return jump;
    }

    /*
     * 获取医护人员类型
     */
    @RequestMapping(value = "/gettoken", method = RequestMethod.GET)
    @ResponseBody
    public String getToken(@RequestParam("code") String code,@RequestParam("state") int state){


        log.info("/wechat/user/doctor_info: code="+code + "&stat=" + state);
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + weChatConfig.getOfficialID() +
                "&secret=" + weChatConfig.getOfficialSecert() + "&code="+ code + "&grant_type=authorization_code";
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);
        log.info("/wechat/user/doctor_info"+result);
        JSONObject j = JSONObject.parseObject(result);
        WechatUserModel u = new WechatUserModel();
        if (j.getString("openid") != null) {
            u = wechatUserService.findByOpenId(j.getString("openid"));
            if (u == null) {
                u = new WechatUserModel();
                if (state == 100){
                    // 100表示非用户信息上传只填表
                }else {
                    u.setDoctorType(state);
                }
                u.setOpenId(j.getString("openid"));
                // 公众号用accesstoken替代sessionkey
                u.setSessionKey(j.getString("access_token"));
                wechatUserService.updateUser(u);
            }else {
                u.setSessionKey(j.getString("access_token"));
                if (state == 100){
                    // 100表示非用户信息上传只填表
                }else {
                    u.setDoctorType(state);
                }
                wechatUserService.updateUser(u);
                u = wechatUserService.findByOpenId(u.getOpenId());
            }
        }else {
            log.error("/wechat/user/doctor_info"+"openid为空");
        }
        j = new JSONObject();
        try {
            j.put("sessionkey", u.getSessionKey() );
            j.put("openid", u.getOpenId());
        }catch (Exception e){
            e.printStackTrace();
        }

        return j.toJSONString();
    }

    /*
     * 获取unionid
     */
    @RequestMapping(value = "/unionid", method = RequestMethod.POST)
    @ResponseBody
    public String getUnionId(@RequestBody UnionIdRequest userInfo){
        DealMessageResponse dm = new DealMessageResponse();

        // 校验用户身份合法性
        if (!wechatUserService.checkUser(userInfo.getOpenid(), userInfo.getSession_key())){
            dm.setErrorCode(500);
            dm.setErrorMsg("/submitrecent: 用户登录状态异常");
            return JSON.toJSONString(dm);
        }

        WechatUserModel u = wechatUserService.findByOpenId(userInfo.getOpenid());
        u.setUnionId(WechatUserService.getUnionId(userInfo));
        if (wechatUserService.updateUser(u)){
            dm.setErrorCode(200);
            dm.setErrorMsg("succ");
        }else {
            dm.setErrorCode(500);
            dm.setErrorMsg("服务器存储失败");
        }
        return JSON.toJSONString(dm);
    }

    /*
     * sumian 跳转 如果stat为空则使用配置url
     */
    @GetMapping("/auth")
    public String auth(@RequestParam("code") String code,@RequestParam("state") String state){
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + weChatConfig.getOfficialID() +
                "&secret=" + weChatConfig.getOfficialSecert() + "&code="+ code + "&grant_type=authorization_code";
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);
//        System.out.println(">>>" + url);
//        System.out.println(">>>" + code);
//        System.out.println(">>>" + result);
        JSONObject j = JSONObject.parseObject(result);
        if (j.getString("openid") != null){
            WechatUserModel u = wechatUserService.findByOpenId(j.getString("openid"));
            if (u == null){
                u = new WechatUserModel();
                u.setOpenId(j.getString("openid"));
                wechatUserService.updateUser(u);
                u = wechatUserService.findByOpenId(u.getOpenId());
            }
            TokenResponse token = sumianService.getToken(u);
            String sumianUrl;
            if (state.equals("123")){
                sumianUrl = weChatConfig.getSumianH5Url() + "&token=" + token.getToken();
            }else {
                if (wechatUserService.getFrom(state) == null){
                    sumianUrl = weChatConfig.getSumianH5Url() + "&token=" + token.getToken();
                }else {
                    try {
                        sumianUrl = URLDecoder.decode(wechatUserService.getFrom(state), "UTF-8") + "?token=" + token.getToken();
                    }catch (Exception e){
                        log.error("sumain url 出错");
                        e.printStackTrace();
                        sumianUrl = weChatConfig.getSumianH5Url() + "&token=" + token.getToken();
                    }

                }
            }
            log.info(sumianUrl);
            return "redirect:" + sumianUrl;
        }else {
            return null;
        }
    }

    /*
     * 缓存速眠url
     */
    @RequestMapping(value = "/putSumianUrl", method = RequestMethod.POST)
    @ResponseBody
    public String updateWechatUser(@RequestBody String info){
        JSONObject j = JSONObject.parseObject(info);
        String url = j.getString("url");

        JSONObject res = new JSONObject();
        res.put("uuid", wechatUserService.setFrom(url));
        return res.toJSONString();
    }


    /*
     * 用于微信认证
     */
    @GetMapping("/MP_verify_dUWiMvgmKRfxn01J.txt")
    public String downloadZipFile(HttpServletRequest request, HttpServletResponse response){
        try{
            String filePathAndName = "./animations/MP_verify_dUWiMvgmKRfxn01J.txt";
            String fileRealName = "MP_verify_dUWiMvgmKRfxn01J.txt";
            // 以流的形式下载文件。
            File f = new File(filePathAndName);
            InputStream fis = new BufferedInputStream(new FileInputStream(filePathAndName));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            response.reset();
            // 设置response的Header
            response.setCharacterEncoding("utf-8");
            request.setCharacterEncoding("utf-8");
            response.addHeader("Content-Length", "" + f.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/x-download");
            String Agent = request.getHeader("User-Agent");
            if (null != Agent) {
                Agent = Agent.toLowerCase();
                if (Agent.indexOf("firefox") != -1) {
                    response.addHeader("Content-Disposition", String.format("attachment;filename*=utf-8'zh_cn'%s", URLEncoder.encode(fileRealName, "utf-8")));
                } else{
                    response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileRealName,"utf-8"));
                }
            }
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
            return null;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
