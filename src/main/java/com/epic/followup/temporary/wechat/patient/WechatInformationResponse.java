package com.epic.followup.temporary.wechat.patient;

import com.epic.followup.temporary.DealMessageResponse;

public class WechatInformationResponse extends DealMessageResponse {
    private WechatInformationRequest information;

    public WechatInformationRequest getInformation() {
        return information;
    }

    public void setInformation(WechatInformationRequest information) {
        this.information = information;
    }
}
