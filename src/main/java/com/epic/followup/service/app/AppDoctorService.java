package com.epic.followup.service.app;

import com.epic.followup.model.app.AppDoctorModel;
import com.epic.followup.temporary.DealMessageResponse;
import com.epic.followup.temporary.app.patient.AppLoginbyCodeRequest;
import com.epic.followup.temporary.app.patient.AppLoginbyCodeResponse;
import com.epic.followup.temporary.app.doctor.*;
import org.springframework.stereotype.Service;

@Service
public interface AppDoctorService {

    // 职工号、密码登录
    LoginResponse loginByEmployeeNum(LoginByEmployeeNumRequest req);
    //通过验证码登录
    AppLoginbyCodeResponse loginbyCode(AppLoginbyCodeRequest req);
    // 绑定手机号发送验证码
    DealMessageResponse sendBindCode(String tel);
    //重置密码时发送验证码
    DealMessageResponse sendResetPasswordCode(String tel);
    // 登录时发送验证码
    DealMessageResponse sendLoginCode(String tel);
    // 绑定手机号
    DealMessageResponse bindTelWithCode(BindTelRequest req);
    // 修改医生信息
    DealMessageResponse editDoctorInfo(EditDoctorRequest req);
    // 重置密码
    DealMessageResponse ResetPassword(ResetPasswdRequest req);
    // 医生信息展示
    DoctorInfoResponse showDoctorInfo(String employeeNum);

    // 根据职工号查询医生信息
    AppDoctorModel getByEmployeeNum(String employeeNum);
    //添加医生
    <S extends AppDoctorModel> boolean addDoctor(S d);
    //根据Id查询医生信息
    AppDoctorModel getById(Long id);
    //根据Id删除
    void deleteById(Long id);
}
