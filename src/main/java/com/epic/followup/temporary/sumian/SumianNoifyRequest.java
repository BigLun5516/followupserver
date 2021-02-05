package com.epic.followup.temporary.sumian;

/**
 * @author : zx
 * @version V1.0
 */
public class SumianNoifyRequest {

    private String userId;
    private String srcId;
    private String sendAt;
    private String type;
    private String url;
    private String first;
    private String keyword1;
    private String keyword2;
    private String keyword3;
    private String remark;

    public String getSrcId() {
        return srcId;
    }

    public String getUserId() {
        return userId;
    }

    public String getSendAt() {
        return sendAt;
    }

    public String getUrl() {
        return url;
    }

    public String getFirst() {
        return first;
    }

    public String getKeyword1() {
        return keyword1;
    }

    public String getKeyword2() {
        return keyword2;
    }

    public String getKeyword3() {
        return keyword3;
    }

    public String getType() {
        return type;
    }

    public String getRemark() {
        return remark;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setSrcId(String srcId) {
        this.srcId = srcId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSendAt(String sendAt) {
        this.sendAt = sendAt;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public void setKeyword1(String keyword1) {
        this.keyword1 = keyword1;
    }

    public void setKeyword2(String keyword2) {
        this.keyword2 = keyword2;
    }

    public void setKeyword3(String keyword3) {
        this.keyword3 = keyword3;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
