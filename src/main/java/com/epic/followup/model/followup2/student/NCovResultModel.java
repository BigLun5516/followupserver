package com.epic.followup.model.followup2.student;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : zx
 * @version V1.0
 */

@Entity
@Table(name = "aidoctor_ncov_result",
        indexes = {@Index(columnList = "userid, answer_time")})  // 为字段加上索引
@EntityListeners(AuditingEntityListener.class)
public class NCovResultModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT 'result主键'")
    private Long Id;

    // 量表序号
    @Column(name = "scale_id", nullable = false)
    private int scaleId;

    // 评测结果
    @Column(name = "level", length = 500, nullable = true)
    private String Level;

    // 得分
    @Column(name = "score", nullable = false)
    private int score;

    // 回答时间 起始时间
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "answer_time", nullable = false)
    private Date answerTime;

    // 用户id
    @Column(name = "userid")
    private Long userId;

    // 答案序号数组
    @Column(name = "answer_array", nullable = false)
    private String answerArray;

    // 用户备注
    @Column(name = "text", nullable = true)
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setAnswerArray(String answerArray) {
        this.answerArray = answerArray;
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

    public Integer[] getAnswerArray() {
        // String str = "1,3,6,9,4,2,1,6";
        String[] strArr = this.answerArray.split(",");
        List<Integer> longList = Arrays.stream(strArr)
                // 调用map将集合中的每个字符串装换为Long类型
                .map(Integer::parseInt)
                // 收约成一个Long类型集合
                .collect(Collectors.toList());
        return longList.toArray(new Integer[]{});
    }

    public void setAnswerArray(int[] answerArray) {

        if (answerArray == null || answerArray.length == 0 ){
            this.answerArray = "";
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(Integer.toString(answerArray[0]));
        for (int i = 1; i < answerArray.length; i++){
            sb.append(",");
            sb.append(Integer.toString(answerArray[i]));
        }
        this.answerArray = sb.toString();
    }

    public void setScaleId(int scaleId) {
        this.scaleId = scaleId;
    }

    public int getScaleId() {
        return scaleId;
    }
}
