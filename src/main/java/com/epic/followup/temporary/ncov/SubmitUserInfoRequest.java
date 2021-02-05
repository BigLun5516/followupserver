package com.epic.followup.temporary.ncov;

import com.epic.followup.temporary.NormalUserRequest;

/**
 * @author : zx
 * @version V1.0
 */
public class SubmitUserInfoRequest extends NormalUserRequest {
    private String[] message;
    // 用户备注
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String[] getMessage() {
        return message;
    }

    public void setMessage(String[] message) {
        this.message = message;
    }
}
