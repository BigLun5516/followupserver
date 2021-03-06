package com.epic.followup.service.followup2.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.epic.followup.conf.FollowupStaticConfig;
import com.epic.followup.conf.WeChatConfig;
import com.epic.followup.model.followup2.BaseUserModel;
import com.epic.followup.model.followup2.WechatAppUserModel;
import com.epic.followup.model.followup2.student.StudentInfo;
import com.epic.followup.repository.followup2.BaseUserRepository;
import com.epic.followup.repository.followup2.WechatAppUserRepository;
import com.epic.followup.repository.followup2.student.StudentInfoRepository;
import com.epic.followup.repository.managementSys.UniversityRepository;
import com.epic.followup.service.NLPService;
import com.epic.followup.service.followup2.BaseUserService;
import com.epic.followup.temporary.DealMessageResponse;
import com.epic.followup.temporary.followup2.LoginRequest;
import com.epic.followup.temporary.followup2.LoginResponse;
import com.epic.followup.temporary.followup2.RegistRequest;
import com.epic.followup.temporary.followup2.ResetPasswordRequest;
import com.epic.followup.temporary.followup2.session.BaseUserSession;
import com.epic.followup.util.DateTimeUtils;

import org.apache.xmlrpc.XmlRpcException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;

/**
 * @author : zx
 * @version V1.0
 */
@Service
public class BaseUserServiceImpl implements BaseUserService {

    private ExpiringMap<String, String> codeMap;
    private ExpiringMap<String, String> resetCodeMap; // ????????????
    private ExpiringMap<String, BaseUserSession> userMap;
    private BaseUserRepository baseUserRepository;
    private WechatAppUserRepository wechatAppUserRepository;
    private StudentInfoRepository studentInfoRepository;
    private UniversityRepository universityRepository;
    private WeChatConfig weChatConfig;
    private NLPService nlpService;
    private org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public BaseUserServiceImpl(BaseUserRepository baseUserRepository, WeChatConfig weChatConfig,
                               WechatAppUserRepository wechatAppUserRepository,
                               StudentInfoRepository studentInfoRepository,
                               NLPService nlpService,UniversityRepository universityRepository){
        this.codeMap = ExpiringMap.builder()
                .maxSize(FollowupStaticConfig.MAX_USERNUM)
                .expiration(1, TimeUnit.MINUTES) // 1????????????
                .expirationPolicy(ExpirationPolicy.ACCESSED)
                .variableExpiration()
                .build();
        this.resetCodeMap = ExpiringMap.builder()
                .maxSize(FollowupStaticConfig.MAX_USERNUM)
                .expiration(1, TimeUnit.MINUTES) // 1????????????
                .expirationPolicy(ExpirationPolicy.ACCESSED)
                .variableExpiration()
                .build();
        this.baseUserRepository = baseUserRepository;
        this.weChatConfig = weChatConfig;
        this.wechatAppUserRepository = wechatAppUserRepository;
        this.studentInfoRepository = studentInfoRepository;
        this.nlpService = nlpService;
        this.universityRepository=universityRepository;

        this.userMap = ExpiringMap.builder()
                .maxSize(FollowupStaticConfig.MAX_USERNUM)
                .expiration(1, TimeUnit.DAYS)
                .expirationPolicy(ExpirationPolicy.ACCESSED)
                .variableExpiration()
                .build();
    }

    @Override
    public String getCode(String tel) {
        return codeMap.get(tel);
    }

    @Override
    public StudentInfo getStudentInfoByUserID(Long userId) {
        return this.studentInfoRepository.findByUserId(userId).get();
    }

    @Override
    public DealMessageResponse setCode(String tel) {
        DealMessageResponse res = new DealMessageResponse();
        if (codeMap.get(tel) != null){
            res.setErrorMsg("1??????????????????????????????????????????.");
            res.setErrorCode(502);
            return res;
        }

        // ????????????
        String code = DateTimeUtils.generateCode();
        codeMap.put(tel, code);
        if (!this.sendCode(tel, "SMS_183770246", code)){
            res.setErrorMsg("?????????????????????????????????????????????.");
            res.setErrorCode(502);
            return res;
        }

        res.setErrorCode(200);
        res.setErrorMsg("????????????");
        return res;
    }

    @Override
    public DealMessageResponse setPasswordCode(String tel) {
        DealMessageResponse res = new DealMessageResponse();
        if (resetCodeMap.get(tel) != null){
            res.setErrorMsg("1??????????????????????????????????????????.");
            res.setErrorCode(502);
            return res;
        }

        // ????????????
        String code = DateTimeUtils.generateCode();
        resetCodeMap.put(tel, code);
        if (!this.sendCode(tel, "SMS_183770245", code)){
            res.setErrorMsg("?????????????????????????????????????????????.");
            res.setErrorCode(502);
            return res;
        }

        res.setErrorCode(200);
        res.setErrorMsg("????????????");
        return res;
    }

    @Override
    @Transactional
    /*
     * ??????????????????
     */
    public LoginResponse addUser(RegistRequest req) {
        LoginResponse res = new LoginResponse();

        // ???????????????
        if (this.codeMap.get(req.getTel()) != null){
            if (!req.getCode().equals(this.codeMap.get(req.getTel()))){
                res.setErrorCode(503);
                res.setErrorMsg("??????????????????.");
                return res;
            }
        }else {
            res.setErrorCode(503);
            res.setErrorMsg("??????????????????????????????.");
            return res;
        }

        // ?????????????????????
        Optional<StudentInfo>oStudentInfo = studentInfoRepository.findByStidAndDepartment(req.getStid(), req.getDepartment());
        if (!oStudentInfo.isPresent()){
            res.setErrorCode(503);
            res.setErrorMsg("???????????????????????????????????????.");
            return res;
        }

        StudentInfo si = oStudentInfo.get();
        if (si.getUserId() != null){
            if (si.getUserId() != -1){
                res.setErrorCode(503);
                res.setErrorMsg("??????????????????.");
                return res;
            }
        }

        // ????????????
        // !req.getPassword().equals(req.getStid().substring(req.getStid().length()-6))
        if (req.getPassword().equals("")){
            res.setErrorCode(503);
            res.setErrorMsg("??????????????????.");
            return res;
        }

        // ????????????????????????
        if (this.baseUserRepository.findByTel(req.getTel()).isPresent()){
            res.setErrorCode(505);
            res.setErrorMsg("?????????????????????.");
            return res;
        }

        // ??????
        BaseUserModel user = new BaseUserModel();
        user.setCreateTime(new Date());
        user.setDepartment(req.getDepartment());
        user.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes())); // md5 ??????
        user.setStid(req.getStid());
        user.setTel(req.getTel());
        user.setType(3);
        user = baseUserRepository.save(user);
        // ?????????????????????
        si.setUserId(user.getUserId());
        studentInfoRepository.save(si);

        // ??????sessionid
        BaseUserSession s = new BaseUserSession();
        s.setTel(user.getTel());
        s.setUserId(user.getUserId());
        s.setType(user.getType());
        s.setTime(new Date().getTime());
        String md = DigestUtils.md5DigestAsHex((s.getTel()+s.getUserId()+s.getType()+s.getTime()).getBytes());
        userMap.put(md, s);

        // ????????????
        res.setErrorMsg("????????????.");
        res.setErrorCode(200);
        res.setSessionId(md);
        return res;
    }

    @Override
    public DealMessageResponse compareFace(String base64Img, BaseUserSession user) {
        DealMessageResponse res = new DealMessageResponse();
        Optional<StudentInfo> ost = studentInfoRepository.findByUserId(user.getUserId());
        if (!ost.isPresent()){
            res.setErrorMsg("??????????????????.");
            res.setErrorCode(204);
            return res;
        }
        StudentInfo stui = ost.get();
        if (stui.getImgPath() == null){
            res.setErrorMsg("????????????????????????.");
            res.setErrorCode(204);
            return res;
        }
        byte[] data = null;

        // ????????????
        if(stui.getImgPath().equals("na")){
            res.setErrorMsg("????????????");
            res.setErrorCode(200);
            return res;
        }
        // ????????????????????????
        try {
            InputStream in = new FileInputStream(stui.getImgPath());
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            log.error("??????????????????");
            e.printStackTrace();
        }
        // ???????????????Base64??????
        Base64.Encoder encoder = Base64.getEncoder();
        int r = -1;
        try {
            r = nlpService.baiduFaceIden_base64(encoder.encodeToString(Objects.requireNonNull(data)), base64Img);
        }catch (XmlRpcException e){
            log.error(e.getMessage());
            res.setErrorMsg("???????????????.");
            res.setErrorCode(503);
            return res;
        }

        if (r == 2){
            res.setErrorMsg("????????????");
            res.setErrorCode(200);
            return res;
        }else if (r == 0){
            res.setErrorMsg("?????????");
            res.setErrorCode(403);
            return res;
        }else if (r == 1){
            res.setErrorMsg("??????????????????");
            res.setErrorCode(403);
            return res;
        }else {
            res.setErrorMsg("?????????");
            res.setErrorCode(403);
            return res;
        }
    }

    @Override
    public LoginResponse login(LoginRequest req) {
        LoginResponse res = new LoginResponse();
        Optional<BaseUserModel> ob = baseUserRepository.findByTel(req.getTel());
        if (ob.isPresent()){
            if (!req.getPassword().equals(ob.get().getPassword())){
                res.setErrorCode(401);
                res.setErrorMsg("??????????????????????????????.");
                return res;
            }
        }else {
            res.setErrorCode(401);
            res.setErrorMsg("??????????????????????????????.");
            return res;
        }
        BaseUserSession s = new BaseUserSession();
        BaseUserModel user = ob.get();
        s.setTel(user.getTel());
        s.setUserId(user.getUserId());
        s.setType(user.getType());
        s.setTime(new Date().getTime());
        s.setUniversityId(studentInfoRepository.findByUserId(user.getUserId()).get().getUniversityId());
        String md = DigestUtils.md5DigestAsHex((s.getTel()+s.getUserId()+s.getType()+s.getTime()).getBytes());
        userMap.put(md, s);
        // ????????????
        res.setErrorMsg("????????????.");
        res.setErrorCode(200);
        res.setSessionId(md);
        return res;
    }

    @Override
    public BaseUserSession findBySessionId(String sessionId) {
        return userMap.get(sessionId);
    }

    @Override
    public WechatAppUserModel findWechatUserByOpenId(String OpenId) {
        Optional<WechatAppUserModel> ou = wechatAppUserRepository.findByOpenId(OpenId);
        if (ou.isPresent()){
            return ou.get();
        }else {
            return null;
        }
    }

    @Override
    public DealMessageResponse ResetPassword(ResetPasswordRequest req) {
        DealMessageResponse res = new DealMessageResponse();
        Optional<BaseUserModel> ou = baseUserRepository.findByTel(req.getTel());
        if (!ou.isPresent()){
            res.setErrorMsg("???????????????.");
            res.setErrorCode(505);
            return res;
        }
        BaseUserModel u = ou.get();
        if (resetCodeMap.get(u.getTel())!= null && resetCodeMap.get(u.getTel()).equals(req.getCode())){
            res.setErrorMsg("????????????");
            res.setErrorCode(200);
            u.setPassword(req.getPassword());
            baseUserRepository.save(u);
            return res;
        }else {
            res.setErrorMsg("???????????????????????????.");
            res.setErrorCode(505);
            return res;
        }
    }

    /*
     * ??????tempid : SMS_183763062
     * ????????????SMS_183770246
     * ??????????????? SMS_183770245
     */
    public Boolean sendCode(String tel, String templateCode, String code) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", weChatConfig.getAli_access_key_id(), weChatConfig.getAli_access_key_secret());
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", tel);
        request.putQueryParameter("SignName", "????????????");
        request.putQueryParameter("TemplateCode", templateCode);
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            log.info(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
            return false;
        } catch (ClientException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
