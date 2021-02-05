package com.epic.followup.temporary;

import java.util.Date;

/**
 * @author : zx
 * @version V1.0
 */
public class ChartResponse extends DealMessageResponse {

    // 最近一次得分
    private int score;
    // 建议
    private String suggestion;
    // 周一到周日数据
    private ChartData[] data;
    // 抑郁程度
    private String level;

    public void setData(ChartData[] data) {
        this.data = data;
    }
    public ChartData[] getData() {
        return data;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public int getScore() {
        return score;
    }
    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }
    public String getSuggestion() {
        return suggestion;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
