package com.epic.followup.service.followup2.student;

import com.epic.followup.model.followup2.student.CCBTAccessAnswerModel;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author zhaoyiting
 */
public interface CCBTAccessAnswerService {

    /**
     * 根据用户id 和 date查询结果记录
     * @param userId
     * @param date
     * @return
     */
    CCBTAccessAnswerModel getAnswerByUserIdAndDate(Long userId, String date);

    void saveAccessAnswer(CCBTAccessAnswerModel model);

    CCBTAccessAnswerModel getLastestHistory(Long userId);


}
