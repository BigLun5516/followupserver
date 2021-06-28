package com.epic.followup.service.followup2.student.impl;

import com.epic.followup.model.followup2.student.CCBTAccessAnswerModel;
import com.epic.followup.repository.followup2.student.CCBTAccessAnswerRepository;
import com.epic.followup.service.followup2.student.CCBTAccessAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CCBTAccessAnswerServiceImpl implements CCBTAccessAnswerService {

    @Autowired
    public CCBTAccessAnswerRepository answerRepository;

    @Override
    public CCBTAccessAnswerModel getAnswerByUserIdAndDate(Long userId, String date) {

        List<CCBTAccessAnswerModel> list = answerRepository.getAnswerByUserIdAndDate(userId, date);
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public void saveAccessAnswer(CCBTAccessAnswerModel model) {
        answerRepository.save(model);
    }

    @Override
    public List<Map<String, Object>> getLastestHistory(Long userId) {
        List<Object> list1 = answerRepository.getLastestHistory1(userId);
        List<Object> list2 = answerRepository.getLastestHistory2(userId);
        List<Object> list3 = answerRepository.getLastestHistory3(userId);
        List<Object> list4 = answerRepository.getLastestHistory4(userId);
        List<Object> list5 = answerRepository.getLastestHistory5(userId);
        List<Map<String, Object>> data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Map<String, Object> item = new HashMap<>();
            if(i>=list1.size()){
                item.put("depress_answer",null);
                item.put("depress_score",0);
                item.put("createtime1",null);
            }else{
                Object[] obj = (Object[]) list1.get(i);
                item.put("depress_answer",obj[0]);
                item.put("depress_score",obj[1]);
                item.put("createtime1",obj[2]);
            }
            if(i>=list2.size()){
                item.put("anxious_answer",null);
                item.put("anxious_score",0);
                item.put("createtime2",null);
            }else{
                Object[] obj = (Object[]) list2.get(i);
                item.put("anxious_answer",obj[0]);
                item.put("anxious_score",obj[1]);
                item.put("createtime2",obj[2]);
            }
            if(i>=list3.size()){
                item.put("sleep_answer",null);
                item.put("sleep_score",0);
                item.put("createtime3",null);
            }else{
                Object[] obj = (Object[]) list3.get(i);
                item.put("sleep_answer",obj[0]);
                item.put("sleep_score",obj[1]);
                item.put("createtime3",obj[2]);
            }
            if(i>=list4.size()){
                item.put("cognition_answer",null);
                item.put("createtime4",null);
            }else{
                Object[] obj = (Object[]) list4.get(i);
                item.put("cognition_answer",obj[0]);
                item.put("createtime4",obj[1]);
            }
            if(i>=list5.size()){
                item.put("relationship_answer",null);
                item.put("createtime5",null);
            }else{
                Object[] obj = (Object[]) list5.get(i);
                item.put("relationship_answer",obj[0]);
                item.put("createtime5",obj[1]);
            }
            data.add(item);
        }
        return data;

    }
}
