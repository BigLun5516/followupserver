package com.epic.followup.conf;

import org.springframework.stereotype.Component;

/**
 * @author : zx
 * @version V1.0
 */

@Component
public class NCovConfig {

    // 量表名称
    public final static String[] SCALENAMES = new String[]{"", "抑郁症筛查量表",
            "广泛性焦虑障碍量表", "失眠严重指数", "事件影响量表修订版"};

    // 小程序端
    public final static String[] SCALENAMES2 = new String[]{"", "抑郁症筛查量表",
            "广泛性焦虑障碍量表", "失眠严重指数", "自杀风险量表"};

    //学生量表
    public final static String[] STUSCALENAMES = new String[]{"自杀风险量表", "抑郁症筛查量表",
            "广泛性焦虑障碍量表", "失眠严重指数"};

    public final static int SCALENUM = 4;
}
