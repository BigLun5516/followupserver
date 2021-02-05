package com.epic.followup.service.followup2.student.impl;

import com.epic.followup.model.followup2.student.WechatFileModel;
import com.epic.followup.repository.followup2.student.WechatFile2Repository;
import com.epic.followup.service.followup2.student.WechatFile2Service;
import com.epic.followup.util.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author : zx
 * @version V1.0
 */

@Service
public class WechatFile2ServiceImpl implements WechatFile2Service {

    @Autowired
    WechatFile2Repository wechatFile2Repository;

    @Override
    public void insert(WechatFileModel w) {
        wechatFile2Repository.save(w);
    }

    @Override
    public void updateSucc(Long openid) {
        Date today = new Date();
        wechatFile2Repository.updateSucc(openid, DateTimeUtils.getStartOfDay(today),
                DateTimeUtils.getEndOfDay(today));
    }

    @Override
    public void deleteOutData(Long openId) throws Exception {
        // 删除文件
        List<WechatFileModel> l = this.getAppointDayUnsuccData(openId, 0);
        for(WechatFileModel i : l){
            File file = new File(i.getPath());
            if (!file.exists()){
                throw new FileNotFoundException(i.getPath());
            }

            if (file.isFile()){
                if (!file.delete()){
                    throw new Exception(i.getPath()+"删除失败");
                }
            }
        }
        Date today = new Date();
        wechatFile2Repository.deleteByDateAndOpenID(openId, DateTimeUtils.getStartOfDay(today),
                DateTimeUtils.getEndOfDay(today));
    }

    @Override
    public List<WechatFileModel> getAppointDayUnsuccData(Long openId, int day) {
        Date dNow = new Date();
        Date dBefore = new Date();

        Calendar calendar = Calendar.getInstance();

        //把当前时间赋给日历
        calendar.setTime(dNow);
        //设置为前一天
        calendar.add(Calendar.DAY_OF_MONTH, -day);
        // 得到前一天的时间
        dBefore = calendar.getTime();

        return wechatFile2Repository.findUnsuccAnswersByDateAndOpenID(openId, DateTimeUtils.getStartOfDay(dBefore),
                DateTimeUtils.getEndOfDay(dBefore));
    }
}
