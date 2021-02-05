package com.epic.followup.service.followup2.student;

import com.epic.followup.model.followup2.student.WechatFileModel;

import java.util.List;

/**
 * @author : zx
 * @version V1.0
 */
public interface WechatFile2Service {

    void insert(WechatFileModel w);

    void deleteOutData(Long userId) throws Exception;
    void updateSucc(Long userId);

    // 获取未提交数据
    List<WechatFileModel> getAppointDayUnsuccData(Long userId, int day);
}
