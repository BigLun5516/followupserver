package com.epic.followup.service.followup2;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.temporary.DealMessageResponse;
import com.epic.followup.temporary.app.MiniSubmitRequest;
import com.epic.followup.temporary.followup2.session.BaseUserSession;
import com.epic.followup.temporary.wechat.patient.diary.*;
import org.springframework.web.multipart.MultipartFile;

public interface WechatPatientService {

    //------日记部分-------

    //上传图片
    upLoadDiaryImgResponse uploadDiaryImg(MultipartFile file);

    //保存日记
    savePatientDiaryResponse saveDiary(BaseUserSession bps, savePatientDiaryRequest saveDiaryRequest);

    //根据Id返回日记信息
    getPatientDiaryResponse getPatientDiaryById(diaryIdRequest diaryId);

    //删除日记
    DealMessageResponse delPatientDiaryById(diaryIdRequest diaryId);

    //返回所有的日记信息
    getAllPatientDiaryResponse getAllPatientDiary(BaseUserSession bps);

    //返回所有的心情
    getAllMoodsResponse getAllPatientMoods(BaseUserSession bps);

    //---------个人身体信息部分--------

    //增 改 个人身体信息
    DealMessageResponse saveorupdateInformation(BaseUserSession bps, JSONObject information);
    //查 个人身体信息
    JSONObject retrieveInformation(BaseUserSession bps, JSONObject information);

    // 查询最近七天身体信息
    JSONObject getSevenDaysBodyInfo(BaseUserSession bps);

    //保存mini量表结果
    DealMessageResponse saveMiniResult(Long userId, MiniSubmitRequest miniSubmitRequest);

    // 根据月份查找当月有身体记录的日期
    JSONObject getDaysByMonth(BaseUserSession bps, String month);

    // 查询量表历史记录时间（不包含mini量表）
    JSONObject getScaleHistory(Long userId);

}
