package com.epic.followup.service.impl;

import com.epic.followup.model.ResultModel;
import com.epic.followup.repository.ResultRepository;
import com.epic.followup.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : zx
 * @version V1.0
 */

@Service
public class ResultServiceImpl implements ResultService {

    @Autowired
    ResultRepository resultRepository;

    @Override
    public void saveResult(ResultModel r) {
        resultRepository.save(r);
    }
}
