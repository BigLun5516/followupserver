package com.epic.followup.service.followup2.impl;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.followup2.ccbt.CCBTDataModel;
import com.epic.followup.repository.followup2.ccbt.CCBTDataRepository;
import com.epic.followup.service.followup2.WechatCCBTService;
import com.epic.followup.temporary.followup2.session.BaseUserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class WechatCCBTServiceImpl implements WechatCCBTService {

    @Autowired
    CCBTDataRepository ccbtDataRepository;

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
        return null;
    }
}
