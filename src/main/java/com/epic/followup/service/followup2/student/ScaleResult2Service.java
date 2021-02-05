package com.epic.followup.service.followup2.student;


import com.epic.followup.model.followup2.student.NCovResultModel;
import com.epic.followup.temporary.ncov.GetScaleResultResponse;

import java.util.List;

/**
 * @author : zx
 * @version V1.0
 */
public interface ScaleResult2Service {

    void saveResult(NCovResultModel r);

    // 获取最新的量表结果
    GetScaleResultResponse getRecentScales(long userId);

    // 获取量表历史记录保存日期
    List<String> getHistoryDate(Long userId);

    GetScaleResultResponse getResultByDate (long userId, String date);

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
                    return "没有抑郁，继续保持健康的生活方式，祝您身体健康。";
                }else if (score >= 5 && score <= 9){
                    return "建议您及时疏导情绪，调整生活作息，祝您身体健康。   ";
                }else if (score >= 10 && score <= 14){
                    return "建议您规律作息，适量运动，转移注意力，适当心理调整（可请专业人士帮助），祝您身体健康。";
                }else if (score >= 15 && score <= 27){
                    return "请及时寻求专业心理医生的帮助，祝您身体健康。";
                }
                break;
            case 2:
                // 广泛性焦虑量表
                if (score >= 0 && score <= 4){
                    return "正常，没有焦虑，非常好，请继续保持。";
                }else if (score >= 5 && score <= 9){
                    return "建议您适当放松，明天会更好。 ";
                }else if (score >= 10 && score <= 14){
                    return "建议您规律生活，适当心理调整（可请专业人士帮助）。  ";
                }else if (score >= 15 && score <= 21){
                    return "建议您向专业人士咨询，积极调整。 ";
                }
                break;

            case 3:
                // 失眠严重指数
                if (score >= 0 && score <= 7){
                    return "没有临床上显著的失眠症，非常好，请继续保持。 ";
                }else if (score >= 8 && score <= 14){
                    return "建议您规律作息，适量运动。 ";
                }else if (score >= 15 && score <= 21){
                    return "建议您规律作息，减少日间卧床时间，适量运动，必要时请专业人士帮助。 ";
                }else if (score >= 22 && score <= 28){
                    return "建议您向专业人士咨询，积极调整。";
                }
                break;
            case 4:
                // 事件影响量表
                if (score >= 0 && score <= 8){
                    return "无明显应激反应，非常好，请继续保持。 ";
                }else if (score >= 9 && score <= 25){
                    return "建议您适当放松，明天会更好。 ";
                }else if (score >= 26 && score <= 43){
                    return "建议您转移注意力，适当心理调整（可请专业人士帮助）。 ";
                }else if (score >= 44 && score <= 88){
                    return "建议您向专业人士咨询，积极调整。 ";
                }
                break;

            default:
                return "数据异常";
        }
        return "数据异常";
    }

}
