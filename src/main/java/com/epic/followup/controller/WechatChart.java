package com.epic.followup.controller;

import com.alibaba.fastjson.JSON;
import com.epic.followup.conf.WeChatConfig;
import com.epic.followup.model.AnswerModel;
import com.epic.followup.model.WechatUserModel;
import com.epic.followup.service.AnswerService;
import com.epic.followup.service.WechatUserService;
import com.epic.followup.temporary.*;
import com.epic.followup.util.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : zx
 * @version V1.0
 */

@Controller
@RequestMapping("/wechat/chart")
public class WechatChart {

    @Autowired
    private WeChatConfig weChatConfig;
    @Autowired
    private WechatUserService wechatUserService;
    @Autowired
    private AnswerService answerService;


    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public String getData(@RequestBody NormalUserRequest userInfo){

        ChartResponse dm = new ChartResponse();

        // 校验用户身份合法性
        if (!wechatUserService.checkUser(userInfo.getOpenid(), userInfo.getSession_key())){
            dm.setErrorCode(500);
            dm.setErrorMsg("/wechat/chart: 用户登录状态异常");
            return JSON.toJSONString(dm);
        }

        // 计算最近得分
        List<AnswerModel> lastSuccs = answerService.getLastSucces(userInfo.getOpenid(), weChatConfig.getAnswerNums());
        if (lastSuccs.size() != weChatConfig.getAnswerNums()){
            dm.setErrorCode(501);
            dm.setErrorMsg("/wechat/chart: 无最近测评数据");
            return JSON.toJSONString(dm);
        }else {
            dm.setScore(answerService.countScore(lastSuccs));
        }

        // 建议
        dm.setSuggestion("吃好喝好");

        // 计算level
        dm.setLevel(answerService.getDPLevel(lastSuccs));

        // 计算每日得分
        ChartData[] cd = new ChartData[7];
        for (int i = 0; i < 7; i++){
            List<AnswerModel> l = answerService.getAppointDayData(userInfo.getOpenid(), i);
            cd[i] = new ChartData();
            // 星期几
            cd[i].setDay(DateTimeUtils.getDayOfWeek(i));
            // 缓存数据
            List<Integer> dataList = new ArrayList<Integer>();
            for (int z = 0; z < l.size(); z+=weChatConfig.getAnswerNums()){

                // 无数据 或 数据不足
                if (l.size()+1 - z < weChatConfig.getAnswerNums()){
                    break;
                }else {
                    cd[i].setDay(DateTimeUtils.getDayOfWeek(i));
                    cd[i].setTime(l.get(z).getAnswerTime());
                    dataList.add(answerService.countScore(l.subList(z, z+weChatConfig.getAnswerNums())));
                    cd[i].setLevel(answerService.getDPLevel(l.subList(z, z+weChatConfig.getAnswerNums())));
                }
            }
            // 取均值
            if (!dataList.isEmpty()){
                int sum = 0;
                int tmp[] = new int[dataList.size()];
                for (int y = 0; y < dataList.size(); y ++){
                    tmp[y] = dataList.get(y);
                    sum += tmp[y];
                }
                cd[i].setData(tmp);
                cd[i].setTitle(sum/dataList.size());

            }
        }
        dm.setErrorCode(200);
        dm.setErrorMsg("succ");
        dm.setData(cd);
        return JSON.toJSONString(dm);
    }
}
