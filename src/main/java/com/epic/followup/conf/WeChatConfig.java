package com.epic.followup.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author : zx
 * @version V1.0
 */


@Component
@PropertySource( value = "classpath:wechat.properties")
@ConfigurationProperties(prefix="followup")
public class WeChatConfig {

    private String appSecert;
    private String appID;
    private String url;
    private String grantType;
    private int delay;
    private int answerNums;
    private int maxScore;

    private String sumianUrl;
    private String sumianUserRegistUurl;
    private String sumianTokenUrl;
    private String sumianAppKey;
    private String sumianAppSecret;
    private String sumianH5Url;

    private String officialSecert;
    private String officialID;

    private String ali_access_key_id;
    private String ali_access_key_secret;
    private String ali_region_id;

    private String guoguang;

    public String getGuoguang() {
        return guoguang;
    }

    public void setGuoguang(String guoguang) {
        this.guoguang = guoguang;
    }

    public String getAli_access_key_id() {
        return ali_access_key_id;
    }

    public void setAli_access_key_id(String ali_access_key_id) {
        this.ali_access_key_id = ali_access_key_id;
    }

    public String getAli_access_key_secret() {
        return ali_access_key_secret;
    }

    public void setAli_access_key_secret(String ali_access_key_secret) {
        this.ali_access_key_secret = ali_access_key_secret;
    }

    public String getAli_region_id() {
        return ali_region_id;
    }

    public void setAli_region_id(String ali_region_id) {
        this.ali_region_id = ali_region_id;
    }

    public String getOfficialID() {
        return officialID;
    }

    public String getOfficialSecert() {
        return officialSecert;
    }

    public void setOfficialID(String officialID) {
        this.officialID = officialID;
    }

    public void setOfficialSecert(String officialSecert) {
        this.officialSecert = officialSecert;
    }

    public String getSumianH5Url() {
        return sumianH5Url;
    }

    public void setSumianH5Url(String sumianH5Url) {
        this.sumianH5Url = sumianH5Url;
    }

    public String getSumianTokenUrl() {
        return sumianTokenUrl;
    }

    public void setSumianTokenUrl(String sumianTokenUrl) {
        this.sumianTokenUrl = sumianTokenUrl;
    }

    public String getSumianAppKey() {
        return sumianAppKey;
    }

    public String getSumianUrl() {
        return sumianUrl;
    }

    public String getSumianUserRegistUurl() {
        return sumianUserRegistUurl;
    }

    public String getSumianAppSecret() {
        return sumianAppSecret;
    }

    public void setSumianUrl(String sumianUrl) {
        this.sumianUrl = sumianUrl;
    }

    public void setSumianAppKey(String sumianAppKey) {
        this.sumianAppKey = sumianAppKey;
    }

    public void setSumianUserRegistUurl(String sumianUserRegistUurl) {
        this.sumianUserRegistUurl = sumianUserRegistUurl;
    }

    public void setSumianAppSecret(String sumianAppSecret) {
        this.sumianAppSecret = sumianAppSecret;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getAppSecert() {
        return appSecert;
    }

    public void setAppSecert(String appSecert) {
        this.appSecert = appSecert;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getAnswerNums() {
        return answerNums;
    }

    public void setAnswerNums(int answerNums) {
        this.answerNums = answerNums;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }
}
