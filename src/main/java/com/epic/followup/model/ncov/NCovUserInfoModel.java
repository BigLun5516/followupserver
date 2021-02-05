package com.epic.followup.model.ncov;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * @author : zx
 * @version V1.0
 */


@Entity
@Table(name = "ncov_user",
        indexes = {@Index(columnList = "openid")})  // 为字段openid加上索引
@EntityListeners(AuditingEntityListener.class)
public class NCovUserInfoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT 'ncov_user主键'")
    private Long Id;

    // 微信平台用户身份id 与 wechat_user表关联
    @Column(name = "openid", length = 32, nullable = false)
    private String openId;

    /*
     * 基本信息
     * 1.性别：①男②女
     * 2.出生日期（公历）：年月日
     * 3.民族：①汉族②少数民族
     * 4.婚姻状况：①未婚②已婚③丧偶④离异
     * 5.文化程度：①高中/中专/职校②大专③本科④研究生⑤博士及以上
     * 6.工作单位：省市医院
     * 7.执业资质：①医生②护士③医学生④其他
     * 8.目前实际工作所在科室：①发热门诊②普通隔离病房③重症病房④其他
     * 9.职称：①无②初级③中级④副高或正高
     * 10.家中是否有超过65岁的老人？①是②否
     * 11.家中的老人是否患有高血压、糖尿病等基础性疾病？①是②否
     * 12.子女是否未成年（未满18周岁）①是②否（含无子女）
     */
    @Column(name = "info", nullable = true)
    private String info;

    /*
     * 最近一周居住与工作状况
     *
     * 1.近一周主要居住城市：
     * ①武汉市②湖北省内其他地市③湖北省外其他城市
     * 2.近一周主要居住地点：①城市②农村
     * 3.近一周居住状态：
     * ①独居②与家人居住③与朋友同事居住④与其他人居住
     * 4.截至目前为止，您是否直接从事发热患者或确诊新型冠状病毒肺炎患者的诊疗或护理工作？①是②否
     * 5.截至目前为止，是否有以下人员确诊为新型冠状病毒肺炎？（可多选）
     * ①科室接诊的患者②您本人③家人④同事⑤朋友⑥您居住的小区、街道有确诊病例
     * 6.近一周，您本人和与您共同居住的人（包括家人或合住的人）中，是否有人未确诊，但有发热、咳嗽等症状？①是②否
     * 7.您认为，您是否已经接受足够的新型冠状病毒医院感染防护培训？①是②否
     * 8.工作中，您是否按照最新的新型冠状病毒医院感染预防与控制指南或相关技术标准的要求，规范的做好个人防护？①是②否
     * 9.您认为当前的新型冠状病毒医院感染防护标准是否能保护您在工作中避免感染新型冠状病毒？①是②否
     * 10.近一周您主要通过哪种方式获取新型冠状病毒肺炎疫情信息：
     * ①与他人聊天（包括面对面、电话、语音、视频、文字聊天等）
     * ②社交媒体（包括朋友圈、公众号、微博、抖音等）
     * ③电视④上网⑤其他
     * 11.近一周，您平均每天通过各种方式接收疫情信息的时间大概为小时。
     * 12.近一周，通常您晚上睡前是否关注疫情的相关信息？①是②否
     */
    @Column(name = "recent", nullable = true)
    private String recent;

    /*
     * 心理健康服务需求
     *
     *  1.您听说过“突发公共事件的心理危机干预”吗？
     *  ①听说过并且有较深了解②听说过但不是很了解③没听说过
     *  2.您在此次武汉肺炎疫情发生后，曾经得到过哪些心理上帮助？（可多选）
     *  ①收到关于心理方面的宣传材料②听到媒体对心理方面的宣传
     *  ③团体心理辅导④个体心理辅导⑤心理治疗⑥没获得过任何帮助
     *  3.此次武汉肺炎疫情中，您最希望获得哪方面的心理帮助？
     *  ①收到关于心理方面的宣传材料②听到媒体对心理方面的宣传
     *  ③集体接受心理辅导④个体心理咨询⑤其他
     *  4.此次武汉肺炎疫情中，您最希望由谁提供心理帮助？
     *  ①专业心理咨询人员②家人及亲戚③朋友同事④其他
     *  5. 目前您了解了哪些有关心理方面的知识和技能？（可多选）
     *  ①常见的心理反应②自己如何缓解心理反应③如何帮助别人缓解心理反应④如何寻求专业心理咨询人员的帮助
     *  ⑤其他⑥不了解
     *  6. 您希望进一步了解哪项有关心理方面的知识和技能？（可多选）
     *  ①常见的心理反应②自己如何缓解心理反应③如何帮助别人缓解心理反应④如何寻求专业心理咨询人员的帮助⑤其他
     *  7.您希望通过什么方式来获得有关心理方面的知识和技能？（可多选）
     *  ①聊天②社交媒体（如朋友圈、公众号、微博、抖音等）③网络④咨询热线⑤面对面心理咨询⑥宣传材料⑦手机短信⑧电视节目⑨其他
     *  8.如果发给您关于心理方面的宣传材料，您希望得到哪些种类的材料？（可多选）
     *  ①网络视频②网络图文③传单、折页④宣传画⑤书刊⑥其他
     *  9.在做好防护的情况下，如果有关机构开展针对此次疫情的心理健康教育活动如讲座、咨询等，您愿意参加吗？
     *  ①愿意②不愿意
     */
    @Column(name = "need", nullable = true)
    private String need;

    @Column(name = "doctor_ncov", nullable = true)
    private String doctorNcov;

    // 用户备注
    @Column(name = "text", nullable = true)
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String[] getDoctorNcov(){
        return this.doctorNcov.split(",");
    }

    public void setDoctorNcov(String s) {
        this.doctorNcov = s;
    }

    public void setDoctorNcov(String[] need) {
        if (need == null || need.length == 0){
            this.doctorNcov = "";
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(need[0].replace(',','，'));

        for (int i = 1; i < need.length; i++){
            sb.append(",");
            sb.append(need[i].replace(',','，'));
        }
        this.doctorNcov = sb.toString();
    }

    public Long getId() {
        return Id;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public String[] getInfo() {
        return this.info.split(",");
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setInfo(String[] info) {
        if (info == null || info.length == 0){
            this.info = "";
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(info[0].replace(',','，'));
        for (int i = 1; i < info.length; i++){
            sb.append(",");
            sb.append(info[i].replace(',','，'));
        }
        this.info = sb.toString();
    }


    public void setRecent(String recent) {
        this.recent = recent;
    }

    public void setRecent(String[] recent) {
        if (recent == null || recent.length == 0){
            this.recent = "";
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(recent[0].replace(',','，'));
        for (int i = 1; i < recent.length; i++){
            sb.append(",");
            sb.append(recent[i].replace(',','，'));
        }
        this.recent = sb.toString();
    }

    public String[] getNeed(){
        return this.need.split(",");
    }

    public void setNeed(String need) {
        this.need = need;
    }

    public void setNeed(String[] need) {
        if (need == null || need.length == 0){
            this.need = "";
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(need[0].replace(',','，'));

        for (int i = 1; i < need.length; i++){
            sb.append(",");
            sb.append(need[i].replace(',','，'));
        }
        this.need = sb.toString();
    }

    public String[] getRecent(){
        return this.need.split(",");
    }
}
