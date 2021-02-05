package com.epic.followup.temporary;

import java.util.Date;

/**
 * @author : zx
 * @version V1.0
 */
public class ChartData{
    // 星期一
    private String day;
    // 每次测评数据
    private int[] data;
    // 综合得分 平均数
    private int title;
    // 时间
    private Date time;
    // 抑郁程度
    private String level;

    public int getTitle() {
        return title;
    }
    public void setData(int[] data) {
        this.data = data;
    }
    public int[] getData() {
        return data;
    }
    public void setDay(String day) {
        this.day = day;
    }
    public String getDay() {
        return day;
    }
    public void setTitle(int title) {
        this.title = title;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
