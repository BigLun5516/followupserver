package com.epic.followup.temporary.ncov;

import com.epic.followup.temporary.DealMessageResponse;

/**
 * @author : zx
 * @version V1.0
 */
public class SubmitScaleResponse extends DealMessageResponse {
    private String level;
    private int score;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
