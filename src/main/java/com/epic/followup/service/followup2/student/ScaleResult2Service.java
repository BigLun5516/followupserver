package com.epic.followup.service.followup2.student;


import com.epic.followup.model.followup2.student.NCovResultModel;
import com.epic.followup.temporary.ncov.GetScaleResultResponse;

import java.util.List;
import java.util.Map;

/**
 * @author : zx
 * @version V1.0
 */
public interface ScaleResult2Service {

    void saveResult(NCovResultModel r);

    // 获取最新的量表结果
    GetScaleResultResponse getRecentScales(long userId);

    // 获取量表历史记录保存日期
    List<Map<String, String>> getHistoryDate(Long userId);

    // 删除无效记录
    void deleteResult(long userId, long count);

    GetScaleResultResponse getResultByDate (long userId, String beforeDate, String afterDate);

  /*
     * 计算得分
     */
    public static int getScore(int num, int[]answers){
        int sum = 0;
        switch (num){
            case 1:
                // 抑郁症
                for (int i : answers){
                    sum += i;
                }
                return sum;
            case 2:
                // 焦虑
                for (int i : answers){
                    sum += i;
                }
                return sum;

            case 3:
                // 失眠严重指数
                for (int i : answers){
                    sum += i;
                }
                return sum;
            case 4:
                // 应激 改成自杀了
                for (int i : answers){
                    sum += i;
                }
                return sum;

            default:
                return -1;
        }
    }

    /*
     * 评价
     */
    public static String getLevel(int num, int score){
        switch (num){
            case 1:
                // 抑郁症筛查量表
                if (score >= 0 && score <= 4){
                    return "没有抑郁情绪，请继续加强情绪管理。保持健康的生活方式，祝您身体健康。";
                }else if (score >= 5 && score <= 9){
                    return "存在轻度抑郁情绪，抑郁症状比一般人要多一点点，请采取积极情绪管理策略。建议您及时疏导情绪，调整生活作息，祝您身体健康。   ";
                }else if (score >= 10 && score <= 14){
                    return "存在明显抑郁情绪，抑郁症状要比一般人明显要多，请在采取积极情绪管理策略的同时，向专业人员求助。" +
                            "建议您规律作息，适量运动，转移注意力，使用知己模块适当心理调整。祝您身体健康。";
                }else if (score >= 15 && score <= 27){
                    return "存在重度抑郁情绪，您的抑郁症状处于较为严重的程度，请在采取积极情绪管理策略的同时，尽早向专业人员求助。使用知己模块适当心理调整，祝您身体健康。";
                }
                break;
            case 2:
                // 广泛性焦虑量表
                if (score >= 0 && score <= 4){
                    return "目前没有焦虑情绪，请继续加强情绪管理。";
                }else if (score >= 5 && score <= 9){
                    return "目前存在轻度焦虑情绪，焦虑症状比一般人要多一点点，请采取积极情绪管理策略。建议您适当放松，明天会更好。 ";
                }else if (score >= 10 && score <= 14){
                    return "存在中度焦虑情绪，抑郁症状要比一般人明显要多，针对压力事件或自己的错误您可能比一般人更容易烦恼，您可能更易受到惊吓，以及因为错失机会而脾气暴躁。请在采取积极情绪管理策略的同时，向专业人员求助。";
                }else if (score >= 15 && score <= 21){
                    return "存在重度焦虑情绪，焦虑症状处于较为严重的程度，请在采取积极情绪管理策略的同时，尽早向专业人员求助，明确诊断。";
                }
                break;

            case 3:
                // 失眠严重指数
                if (score >= 0 && score <= 7){
                    return "没有临床上显著的失眠症，非常好，请继续保持。 ";
                }else if (score >= 8 && score <= 14){
                    return "阈下失眠，建议您规律作息，适量运动。 ";
                }else if (score >= 15 && score <= 21){
                    return "中重度失眠，建议您规律作息，减少日间卧床时间，适量运动，必要时请专业人士帮助。 ";
                }else if (score >= 22 && score <= 28){
                    return "重度失眠，建议您向专业人士咨询，积极调整。";
                }
                break;
            case 4:
                // 自杀风险
                if(score <=6){
                    return "没有自杀意念。很好，最近你没有自杀的想法和冲动，心中积攒着继续生活的勇气，并不想在现在结束自己的生命。";
                }else {
                    return "心中存在某些自杀意念，具有一定的自杀危险性。如果有能力，您需要尽量避免让自己陷入这种情绪中。找支持你的、你信任的家人和朋友聊聊天，听听放松的音乐，亦可以进行相关的心理咨询。";
                }

//                // 事件影响量表
//                if (score >= 0 && score <= 8){
//                    return "无明显应激反应，非常好，请继续保持。 ";
//                }else if (score >= 9 && score <= 25){
//                    return "建议您适当放松，明天会更好。 ";
//                }else if (score >= 26 && score <= 43){
//                    return "建议您转移注意力，适当心理调整（可请专业人士帮助）。 ";
//                }else if (score >= 44 && score <= 88){
//                    return "建议您向专业人士咨询，积极调整。 ";
//                }
//                break;

            default:
                return "数据异常";
        }
        return "数据异常";
    }

}
