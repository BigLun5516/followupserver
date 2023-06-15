package com.epic.followup.controller.followup2;

import java.io.InputStream;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.followup2.WechatAppUserModel;
import com.epic.followup.model.managementSys.UniversityModel;
import com.epic.followup.repository.managementSys.UniversityRepository;
import com.epic.followup.service.followup2.BaseUserService;
import com.epic.followup.service.followup2.WechatAppUserService;
import com.epic.followup.service.managementSys.UniversityService;
import com.epic.followup.temporary.DealMessageResponse;
import com.epic.followup.temporary.followup2.CodeRequest;
import com.epic.followup.temporary.followup2.LoginRequest;
import com.epic.followup.temporary.followup2.LoginResponse;
import com.epic.followup.temporary.followup2.RegistRequest;
import com.epic.followup.temporary.followup2.ResetPasswordRequest;
import com.epic.followup.temporary.followup2.session.BaseUserSession;

import com.google.gson.JsonObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author : zx
 * @version V1.0
 */

@Controller
@RequestMapping("/followup2")
public class BaseUserController {

    private BaseUserService baseUserService;
    private WechatAppUserService wechatAppUserService;
    private org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    public BaseUserController(BaseUserService baseUserService,
                              WechatAppUserService wechatAppUserService){
        this.baseUserService = baseUserService;
        this.wechatAppUserService = wechatAppUserService;
    }

    @RequestMapping(value = "/stlogin", method = RequestMethod.POST)
    @ResponseBody
    public LoginResponse stLogin(@RequestBody LoginRequest userInfo){
        LoginResponse res = baseUserService.login(userInfo);
        // 更新微信用户表
        if (userInfo.getOpenID() != null){
            WechatAppUserModel u = wechatAppUserService.findByOpenId(userInfo.getOpenID());
            u.setSessionId(res.getSessionId());
            wechatAppUserService.updateUser(u);
        }

        return res;
    }

    @RequestMapping(value = "/stRegist", method = RequestMethod.POST)
    @ResponseBody
    public LoginResponse stRegist(@RequestBody RegistRequest userInfo){
        return baseUserService.addUser(userInfo);
    }

    @RequestMapping(value = "/getCode", method = RequestMethod.POST)
    @ResponseBody
    public DealMessageResponse getCode(@RequestBody CodeRequest userInfo){
        if (userInfo.getType() == 0){
            return baseUserService.setCode(userInfo.getTel());
        }else if (userInfo.getType() == 1){
            return baseUserService.setPasswordCode(userInfo.getTel());
        }else {
            return null;
        }
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    @ResponseBody
    public DealMessageResponse resetPassword(@RequestBody ResetPasswordRequest userInfo){
        return baseUserService.ResetPassword(userInfo);
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    @ResponseBody
    public String getTest(HttpServletRequest request){
        BaseUserSession bus = baseUserService.findBySessionId(request.getHeader("sessionId"));
        return bus.getTel();
    }

    @RequestMapping(value = "/compareFace")
    @ResponseBody
    public DealMessageResponse compareFace(HttpServletRequest request,
                                           @RequestParam(value = "file", required = true) MultipartFile file){
        BaseUserSession bus = baseUserService.findBySessionId(request.getHeader("sessionId"));

        // 转为base64
        try {
            InputStream in = file.getInputStream();
            byte[] data = new byte[in.available()];
            in.read(data);
            in.close();
            Base64.Encoder encoder = Base64.getEncoder();
            log.info(file.getName());
            return baseUserService.compareFace(encoder.encodeToString(Objects.requireNonNull(data)), bus);
        }catch (Exception e){
            log.error("处理错误");
            log.error(e.getMessage());
            DealMessageResponse res = new DealMessageResponse();
            res.setErrorCode(503);
            res.setErrorMsg("服务器错误");
            return res;
        }
    }

    @RequestMapping("/province")
    @ResponseBody
    public JSONObject getProvince(HttpServletRequest request, HttpServletResponse response) {

        JSONObject res = new JSONObject();
        BaseUserSession session = baseUserService.findBySessionId(request.getHeader("sessionId"));

        // 因为有拦截器存在，这个if其实没啥用了
        if (session == null) {
            res.put("errorMsg", "未查找到此用户的session");
            res.put("errorCode", 501);
            return res;
        }

        Optional<UniversityModel> optional = universityRepository.findById(session.getUniversityId());
        if (optional.isPresent()) {

            res.put("province", optional.get().getProvince());
            res.put("errorCode", 200);
            res.put("errorMsg", "查询成功");
        } else {
            res.put("errorMsg", "未查到此用户所在高校");
            res.put("errorCode", 502);
        }
        return res;

    }


}
