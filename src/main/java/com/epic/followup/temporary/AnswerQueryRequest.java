package com.epic.followup.temporary;

/**
 * @author : zx
 * @version V1.0
 */
public class AnswerQueryRequest extends NormalUserRequest {

    private int checkOrEnd;

    public int getCheckOrEnd() {
        return checkOrEnd;
    }

    public void setCheckOrEnd(int checkOrEnd) {
        this.checkOrEnd = checkOrEnd;
    }
}
