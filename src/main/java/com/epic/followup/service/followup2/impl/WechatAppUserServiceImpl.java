package com.epic.followup.service.followup2.impl;

import com.epic.followup.model.WechatUserModel;
import com.epic.followup.model.followup2.WechatAppUserModel;
import com.epic.followup.repository.WechatUserRepository;
import com.epic.followup.repository.followup2.WechatAppUserRepository;
import com.epic.followup.service.WechatUserService;
import com.epic.followup.service.followup2.WechatAppUserService;
import com.epic.followup.util.UUIDUtil;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author : zx
 * @version V1.0
 */

@Service
public class WechatAppUserServiceImpl implements WechatAppUserService {


    private WechatAppUserRepository wechatAppUserRepository;

    @Autowired
    public WechatAppUserServiceImpl(WechatAppUserRepository wechatAppUserRepository){
        this.wechatAppUserRepository = wechatAppUserRepository;
    }


    @Override
    public WechatAppUserModel findByOpenId(String openid){
        Optional<WechatAppUserModel> ou = wechatAppUserRepository.findByOpenId(openid);
        if (ou.isPresent()){
            return ou.get();
        }
        else {
            return null;
        }
    }

    @Override
    public <S extends WechatAppUserModel> boolean updateUser(S u){
        return (wechatAppUserRepository.save(u).getUserId() != null);
    }
}
