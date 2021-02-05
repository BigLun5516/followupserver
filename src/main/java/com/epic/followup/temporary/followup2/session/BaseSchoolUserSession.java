package com.epic.followup.temporary.followup2.session;

/**
 * @author : lgg
 * @version V1.0
 */
public class BaseSchoolUserSession {

    private Long userId;//代表其在某个具体表中的id，比如在辅导员表中的具体的id

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
