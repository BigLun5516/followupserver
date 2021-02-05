package com.epic.followup.service.ncov;

import com.epic.followup.model.ncov.NCovResultModel;
import com.epic.followup.temporary.ncov.GetScaleResultResponse;

/**
 * @author : zx
 * @version V1.0
 */
public interface ScaleResultService {

    void saveResult(NCovResultModel r);

    // 获取最新的量表结果
    GetScaleResultResponse getRecentScales(String openId);

    //获取学生端量表最新结果
    GetScaleResultResponse getStuRecentScales(String openId);

}
