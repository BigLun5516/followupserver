package com.epic.followup.controller.sumian;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epic.followup.conf.WeChatConfig;
import com.epic.followup.model.WechatUserModel;
import com.epic.followup.service.WechatUserService;
import com.epic.followup.service.officialwechat.SumianService;
import com.epic.followup.temporary.NormalUserRequest;
import com.epic.followup.temporary.sumian.SumianNoifyRequest;
import com.epic.followup.temporary.sumian.TokenResponse;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author : zx
 * @version V1.0
 */

@Controller
@RequestMapping("/sumian")
public class SumianController {

    private SumianService sumianService;
    private WechatUserService wechatUserService;
    private WeChatConfig weChatConfig;
    private org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public SumianController(SumianService sumianService, WechatUserService wechatUserService, WeChatConfig weChatConfig){
        this.sumianService = sumianService;
        this.wechatUserService = wechatUserService;
        this.weChatConfig = weChatConfig;
    }

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
            request.setCharacterEncoding("utf-8");
            response.setCharacterEncoding("utf-8");
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

    @RequestMapping(value = "/getToken", method = RequestMethod.POST)
    @ResponseBody
    public String getToken(@RequestBody NormalUserRequest userInfo){
        TokenResponse dm = new TokenResponse();

        // 校验用户身份合法性
        if (!wechatUserService.checkUser(userInfo.getOpenid(), userInfo.getSession_key())){
            dm.setErrorCode(500);
            dm.setErrorMsg("/sumian/gettoken: 用户登录状态异常");
            return JSON.toJSONString(dm);
        }

        WechatUserModel u = wechatUserService.findByOpenId(userInfo.getOpenid());

        dm = sumianService.getToken(u);
        dm.setErrorCode(200);
        dm.setErrorMsg("succ");
        return JSON.toJSONString(dm);
    }

    @RequestMapping(value={"/transmit"}, produces="application/json;charset=UTF-8")
    @ResponseBody
    public String tarnsmit(HttpServletRequest req, @RequestBody SumianNoifyRequest userInfo){
        log.info("进入 transmit");
        JSONObject j = new JSONObject();
        if (sumianService.isLegal(req)){
            if(sumianService.transmitNotify(userInfo)){

                log.info("{'result':'success'}");
                j.put("result", "success");
                return j.toJSONString();
            }else {
                log.error("{'result':'转发失败'}");
                j.put("result", "转发失败");
                return j.toJSONString();
            }
        }else {
            log.error("{'result':'未通过校验'}");
            j.put("result", "未通过校验");
            return j.toJSONString();
        }
//        sumianService.transmitNotify(userInfo);
//        return null;
    }
}
