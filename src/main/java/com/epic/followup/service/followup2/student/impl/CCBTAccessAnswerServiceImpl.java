package com.epic.followup.service.followup2.student.impl;

import com.epic.followup.model.followup2.student.CCBTAccessAnswerModel;
import com.epic.followup.repository.followup2.student.CCBTAccessAnswerRepository;
import com.epic.followup.service.followup2.student.CCBTAccessAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CCBTAccessAnswerServiceImpl implements CCBTAccessAnswerService {

    @Autowired
    public CCBTAccessAnswerRepository answerRepository;

    @Override
    public CCBTAccessAnswerModel getAnswerByUserIdAndDate(Long userId, String date) {

        List<CCBTAccessAnswerModel> list = answerRepository.getAnswerByUserIdAndDate(userId, date);
        if(list == null || list.isEmpty()){
            return null;
        }
        return list.get(0);
    }

    @Override
    public void saveAccessAnswer(CCBTAccessAnswerModel model) {
        answerRepository.save(model);
    }

    @Override
    public CCBTAccessAnswerModel getLastestHistory(Long userId) {


        List<CCBTAccessAnswerModel> list = answerRepository.getLastestHistory(userId);
        if(list == null || list.isEmpty()){
            return null;
        }
        return list.get(0);

    }
}
