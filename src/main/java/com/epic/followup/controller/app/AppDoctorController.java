package com.epic.followup.controller.app;

import com.epic.followup.service.app.AppDoctorService;
import com.epic.followup.temporary.DealMessageResponse;
import com.epic.followup.temporary.app.patient.AppLoginbyCodeRequest;
import com.epic.followup.temporary.app.patient.AppLoginbyCodeResponse;
import com.epic.followup.temporary.app.doctor.*;
import com.epic.followup.temporary.followup2.CodeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
@RequestMapping("/app/doctor")
public class AppDoctorController {

    @Autowired
    private AppDoctorService appDoctorService;

    @RequestMapping(value = "/login/loginWithEmployeeNum", method = RequestMethod.POST)
    @ResponseBody
    public LoginResponse loginWithEmployeeNum (@RequestBody LoginByEmployeeNumRequest doctorInfo){

        return appDoctorService.loginByEmployeeNum(doctorInfo);
    }

    @RequestMapping(value = "/login/loginWithIdentifyCode", method = RequestMethod.POST)
    @ResponseBody
    public AppLoginbyCodeResponse loginWithIdentifyCode(@RequestBody AppLoginbyCodeRequest userInfo){

        return appDoctorService.loginbyCode(userInfo);
    }

    @RequestMapping(value = "/getIdentifyCode", method = RequestMethod.POST)
    @ResponseBody
    public DealMessageResponse getIdentifyCode(@RequestBody CodeRequest req){
        if (req.getType() == 0){ // 登录
            return appDoctorService.sendLoginCode(req.getTel());
        }
        // 绑定手机号获取验证码
        else if (req.getType() == 3){ //绑定手机号
            return appDoctorService.sendBindCode(req.getTel());
        }
        else if (req.getType() == 2){ //重置密码
            return appDoctorService.sendResetPasswordCode(req.getTel());
        }
        else {
            return null;
        }
    }

    @RequestMapping(value = "/bindTel", method = RequestMethod.POST)
    @ResponseBody
    public DealMessageResponse bindTelWithCode(@RequestBody BindTelRequest req){
        return appDoctorService.bindTelWithCode(req);
    }

    @RequestMapping(value = "/editInfo",method = RequestMethod.POST)
    @ResponseBody
    public DealMessageResponse editDoctorInfo(@RequestBody EditDoctorRequest req){
        return appDoctorService.editDoctorInfo(req);
    }

    @RequestMapping(value = "/login/modifyPasswd", method = RequestMethod.POST)
    @ResponseBody
    public DealMessageResponse resetPassword(@RequestBody ResetPasswdRequest req){
        return appDoctorService.ResetPassword(req);
    }

    @RequestMapping(value = "/showDoctorInfo", method = RequestMethod.POST)
    @ResponseBody
    public DoctorInfoResponse showDoctorInfo(@RequestParam String employeeNum){
        return appDoctorService.showDoctorInfo(employeeNum);
    }
}
