package com.epic.followup.service.followup2.student.impl;

import com.epic.followup.model.followup2.student.ResultModel;
import com.epic.followup.repository.followup2.student.Result2Repository;
import com.epic.followup.service.followup2.student.Result2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : zx
 * @version V1.0
 */

@Service
public class Result2ServiceImpl implements Result2Service {

    @Autowired
    Result2Repository result2Repository;

    @Override
    public void saveResult(ResultModel r) {
        result2Repository.save(r);
    }
}
