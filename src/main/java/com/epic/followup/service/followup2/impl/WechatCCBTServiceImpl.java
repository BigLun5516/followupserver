package com.epic.followup.service.followup2.impl;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.followup2.ccbt.CCBTDataModel;
import com.epic.followup.model.followup2.ccbt.CCBTProgressModel;
import com.epic.followup.repository.followup2.ccbt.CCBTDataRepository;
import com.epic.followup.repository.followup2.ccbt.CCBTProgessRepository;
import com.epic.followup.service.followup2.WechatCCBTService;
import com.epic.followup.temporary.followup2.session.BaseUserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WechatCCBTServiceImpl implements WechatCCBTService {

    @Autowired
    CCBTDataRepository ccbtDataRepository;

    @Autowired
    CCBTProgessRepository ccbtProgessRepository;

    @Override
    public JSONObject saveCCBTData(JSONObject param, BaseUserSession session) {

        // 获取参数
        String commitType = param.getString("commitType");
        String commit = param.getString("commit");
        String dateStr = param.getString("date"); // Date的Long值
        Long userId = session.getUserId();

        JSONObject res = new JSONObject();

        CCBTDataModel ccbtDataModel = new CCBTDataModel();
        ccbtDataModel.setDate(new Date(Long.valueOf(dateStr)));
        ccbtDataModel.setUserId(userId);
        ccbtDataModel.setCommitType(commitType);
        ccbtDataModel.setCommit(commit);

        ccbtDataRepository.save(ccbtDataModel);

        res.put("errorCode", 200);
        res.put("errorMsg", "更新成功");

        return res;
    }

    @Override
    public JSONObject getCCBTData(JSONObject param, BaseUserSession session) {

        // 获取参数
        String commitType = param.getString("commitType");
        Integer offset = param.getInteger("latestBegin"); // 将提交按时间从近到远排序后，从latestBegin开始获取，latestBegin从0开始。
        Integer len = param.getInteger("length"); // 获取的提交数量

        List<CCBTDataModel> ccbtDataModelList = ccbtDataRepository.findCCBTDataModelByCommitTypeAndUserId(commitType, session.getUserId(), offset, len);

        JSONObject data = new JSONObject();
        data.put("commitType", commitType);
        List<Map> commitArray = new ArrayList<>();
        for (CCBTDataModel ccbtDataModel : ccbtDataModelList) {
            HashMap<String, Object> item = new HashMap<>();
            item.put("date", ccbtDataModel.getDate().getTime());
            item.put("commit", ccbtDataModel.getCommit());
            commitArray.add(item);
        }
        data.put("commitArray", commitArray);

        JSONObject res = new JSONObject();
        res.put("data", data);
        res.put("errorCode", 200);
        res.put("errorMsg", "获取成功");

        return res;
    }

    @Override
    public JSONObject saveCCBTProgess(JSONObject param, BaseUserSession session) {

        // 获取参数
        Integer currentIndex = param.getJSONObject("progress").getInteger("currentIndex");
        Integer total = param.getJSONObject("progress").getInteger("total");

        CCBTProgressModel ccbtProgressModel = ccbtProgessRepository.findByUserid(session.getUserId());
        if (ccbtProgressModel == null) {
            ccbtProgressModel = new CCBTProgressModel();
            ccbtProgressModel.setUserid(session.getUserId());
        }
        ccbtProgressModel.setCurrentIndex(currentIndex);
        ccbtProgressModel.setTotal(total);

        ccbtProgessRepository.save(ccbtProgressModel);

        JSONObject res = new JSONObject();
        res.put("errorCode", 200);
        res.put("errorMsg", "更新成功");

        return res;
    }

    @Override
    public JSONObject getCCBTProgess(JSONObject param, BaseUserSession session) {

        JSONObject res = new JSONObject();

        CCBTProgressModel ccbtProgressModel = ccbtProgessRepository.findByUserid(session.getUserId());

        if (ccbtProgressModel == null) {
            res.put("errorCode", 500);
            res.put("errorMsg", "未查询到此用户的进度");
            return res;
        }

        JSONObject data = new JSONObject();
        data.put("currentIndex", ccbtProgressModel.getCurrentIndex());
        data.put("total", ccbtProgressModel.getTotal());
        res.put("data", data);

        res.put("errorCode", 200);
        res.put("errorMsg", "获取成功");

        return res;
    }
}
