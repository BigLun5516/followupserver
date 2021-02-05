package com.epic.followup.temporary.wechat.patient.diary;

import com.epic.followup.temporary.DealMessageResponse;

public class upLoadDiaryImgResponse extends DealMessageResponse {
    private String imgUrl;


    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

}
