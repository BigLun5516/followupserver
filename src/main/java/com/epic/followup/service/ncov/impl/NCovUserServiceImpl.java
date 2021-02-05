package com.epic.followup.service.ncov.impl;

import com.epic.followup.model.ncov.NCovUserInfoModel;
import com.epic.followup.repository.ncov.NCovUserInfoRepository;
import com.epic.followup.service.ncov.NCovUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author : zx
 * @version V1.0
 */

@Service
public class NCovUserServiceImpl implements NCovUserService {

    @Autowired
    NCovUserInfoRepository nCovUserInfoRepository;

    @Override
    public void saveInfo(NCovUserInfoModel r) {
        nCovUserInfoRepository.save(r);
    }

    @Override
    public NCovUserInfoModel findByOpenId(String openid) {
        Optional<NCovUserInfoModel> ou = nCovUserInfoRepository.findByOpenId(openid);
        if (ou.isPresent()){
            return ou.get();
        }
        else {
            return null;
        }
    }
}
