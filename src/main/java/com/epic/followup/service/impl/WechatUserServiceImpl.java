package com.epic.followup.service.impl;

import com.epic.followup.model.WechatUserModel;
import com.epic.followup.repository.WechatUserRepository;
import com.epic.followup.service.WechatUserService;
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
public class WechatUserServiceImpl implements WechatUserService {

    @Autowired
    private WechatUserRepository wechatUserRepository;

    private ExpiringMap<String,String> sumainUrlMap;

    public WechatUserServiceImpl(){
        this.sumainUrlMap = ExpiringMap.builder()
                .maxSize(1000)
                .expiration(10, TimeUnit.SECONDS)
                .expirationPolicy(ExpirationPolicy.ACCESSED)
                .variableExpiration()
                .build();
    }

    @Override
    public String getFrom(String uuid) {
        return sumainUrlMap.get(uuid);
    }

    @Override
    public String setFrom(String url) {
        String uuid = UUIDUtil.getUUID2();
        sumainUrlMap.put(uuid, url);
        return uuid;
    }

    @Override
    public WechatUserModel findByOpenId(String openid){
        Optional<WechatUserModel> ou = wechatUserRepository.findByOpenId(openid);
        if (ou.isPresent()){
            return ou.get();
        }
        else {
            return null;
        }
    }

    @Override
    public boolean checkUser(String openId, String sessionId){
        Optional<WechatUserModel> ou = wechatUserRepository.findByOpenId(openId);
        if (ou.isPresent()){
            return ou.get().getSessionKey().equals(sessionId);
        }
        return false;
    }

    @Override
    public <S extends WechatUserModel> boolean updateUser(S u){
        return (wechatUserRepository.save(u).getUserId() != null);
    }
}
