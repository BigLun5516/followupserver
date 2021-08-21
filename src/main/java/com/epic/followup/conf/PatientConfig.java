package com.epic.followup.conf;

import org.springframework.stereotype.Component;

/**
 * @author : lgg
 * @version V1.0
 */

@Component
public class PatientConfig {

    //个人信息头像上传
    public final static String store_img="/home/followup/static/img/personImg";

    //个人信息头像访问地址
//    public final static String img_query="http://follwup.cmas2020.cn/img/personImg/";
    public final static String img_query="http://follwup-test.cmas2020.cn/img/personImg/";


    //日记存储地址
    public final static String store_diary="/home/followup/static/img/diaryImg";

    //日记访问地址
//    public final static String diary_query="http://follwup.cmas2020.cn/img/diaryImg/";
    public final static String diary_query="http://follwup-test.cmas2020.cn/img/diaryImg/";



    // 性别
    public final static String[] genders = new String[]{"男","女"};

    //职业
    public final static String[] occupations = new String[]{"在校学生",
            "家庭主妇",
            "待业",
            "离退休人员",
            "国家机关、党群组织、企事业单位负责人",
            "专业技术人员",
            "办事人员和有关人员",
            "商业、服务业人员",
            "农林牧渔水利业生产人员",
            "生产运输设备操作人员及有关人员",
            "军人",
            "其他劳动者",
            };

    //用户类型
    public final static String[] userType = new String[]{"普通用户","心理精神障碍困扰用户"};

    //疾病类型
    public final static String[] diseaseType = new String[]{"抑郁症","双相障碍","焦虑症","睡眠障碍" ,"其他"};

    //状态
    public final static String[] psychoStauts = new String[]{"住院病人","门诊病人","未治疗","康复病人"};

    //医院
    public final static String[] hospitals = new String[]{"武汉大学人民医院","其他"};

}
