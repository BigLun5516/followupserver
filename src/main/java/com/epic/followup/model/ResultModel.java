package com.epic.followup.model;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : zx
 * @version V1.0
 */

@Entity
@Table(name = "result",
        indexes = {@Index(columnList = "openid, answer_time")})  // 为字段加上索引
@EntityListeners(AuditingEntityListener.class)
public class ResultModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT 'result主键'")
    private Long Id;

    // 回答序号
    @Column(name = "answer_id", nullable = false)
    private String answerIds;

    // 文件序号
    @Column(name = "file_id", nullable = false)
    private String wechatFileIds;

    // 评测结果
    @Column(name = "level", length = 32, nullable = false)
    private String Level;

    // 得分
    @Column(name = "score", nullable = false)
    private int score;

    // 回答时间
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "answer_time", nullable = false)
    private java.util.Date answerTime;

    // 用户id
    @Column(name = "openid", length = 32, nullable = false)
    private String openId;

    public Long[] getAnswerIds() {
        // String str = "1,3,6,9,4,2,1,6";
        String[] strArr = this.answerIds.split(",");
        List<Long> longList = Arrays.stream(strArr)
                // 调用map将集合中的每个字符串装换为Long类型
                .map(Long::parseLong)
                // 收约成一个Long类型集合
                .collect(Collectors.toList());
        return longList.toArray(new Long[]{});
    }

    public void setAnswerIds(long[] answerIds) {

        if (answerIds == null || answerIds.length == 0){
            this.answerIds = "";
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(Long.toString(answerIds[0]));
        for (int i = 1; i < answerIds.length; i++){
            sb.append(",");
            sb.append(Long.toString(answerIds[i]));
        }
        this.answerIds = sb.toString();
    }

    public Long[] getWechatFileIds() {
        // String str = "1,3,6,9,4,2,1,6";
        String[] strArr = this.wechatFileIds.split(",");
        List<Long> longList = Arrays.stream(strArr)
                // 调用map将集合中的每个字符串装换为Long类型
                .map(Long::parseLong)
                // 收约成一个Long类型集合
                .collect(Collectors.toList());
        return longList.toArray(new Long[]{});
    }

    public void setWechatFileIds(long[] wechatFileIds) {

        if (wechatFileIds == null || wechatFileIds.length == 0 ){
            this.wechatFileIds = "";
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(Long.toString(wechatFileIds[0]));
        for (int i = 1; i < wechatFileIds.length; i++){
            sb.append(",");
            sb.append(Long.toString(wechatFileIds[i]));
        }
        this.wechatFileIds = sb.toString();
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getLevel() {
        return Level;
    }

    public void setAnswerTime(Date answerTime) {
        this.answerTime = answerTime;
    }

    public Date getAnswerTime() {
        return answerTime;
    }

    public void setLevel(String level) {
        Level = level;
    }

    public void setAnswerIds(String answerIds) {
        this.answerIds = answerIds;
    }

    public void setWechatFileIds(String wechatFileIds) {
        this.wechatFileIds = wechatFileIds;
    }
}
