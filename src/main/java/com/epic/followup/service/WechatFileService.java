package com.epic.followup.service;

import com.epic.followup.model.WechatFileModel;

import java.util.List;

/**
 * @author : zx
 * @version V1.0
 */
public interface WechatFileService {

    void insert(WechatFileModel w);

    void deleteOutData(String openId) throws Exception;
    void updateSucc(String openid);

    // 获取未提交数据
    List<WechatFileModel> getAppointDayUnsuccData(String openId, int day);
}
