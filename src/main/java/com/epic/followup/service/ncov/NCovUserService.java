package com.epic.followup.service.ncov;

import com.epic.followup.model.ncov.NCovUserInfoModel;

/**
 * @author : zx
 * @version V1.0
 */
public interface NCovUserService {

    void saveInfo(NCovUserInfoModel r);
    NCovUserInfoModel findByOpenId(String openid);
}
