package com.epic.followup.service.ncov.impl;

import com.epic.followup.conf.NCovConfig;
import com.epic.followup.model.ncov.NCovResultModel;
import com.epic.followup.repository.ncov.ScaleResultRepository;
import com.epic.followup.service.ncov.ScaleResultService;
import com.epic.followup.service.ncov.ScaleService;
import com.epic.followup.temporary.ncov.GetScaleResultResponse;
import com.epic.followup.temporary.ncov.ScaleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : zx
 * @version V1.0
 */

@Service
public class ScaleResultServiceImpl implements ScaleResultService {
    @Autowired
    ScaleResultRepository scaleResultRepository;
    @Autowired
    private NCovConfig nCovConfig;

    @Override
    public void saveResult(NCovResultModel r) {
        scaleResultRepository.save(r);
    }

    @Override
    public GetScaleResultResponse getRecentScales(String openId) {
        GetScaleResultResponse gsr = new GetScaleResultResponse();
        ScaleResult[] srs = new ScaleResult[nCovConfig.SCALENUM];

        for (int i = 1; i <= nCovConfig.SCALENUM; i++){
            List<NCovResultModel> l = scaleResultRepository.getLastScaleByOpenIdAndScaleId(openId, i);
            if (l.size() != 0){
                ScaleResult s = transResult(l.get(0));
                s.setTitle(nCovConfig.SCALENAMES[s.getScaleId()]);
                srs[i-1] = s;
            }else {
                ScaleResult s = new ScaleResult();
                s.setTitle(nCovConfig.SCALENAMES[i]);
                s.setScaleId(i);
                srs[i-1] = s;
            }
        }

        gsr.setScaleResults(srs);
        return gsr;
    }

    @Override
    public GetScaleResultResponse getStuRecentScales(String openId) {
        GetScaleResultResponse gsr = new GetScaleResultResponse();
        ScaleResult[] srs = new ScaleResult[nCovConfig.SCALENUM];

        for (int i = 0; i < nCovConfig.SCALENUM; i++){
            List<NCovResultModel> l = scaleResultRepository.getLastScaleByOpenIdAndScaleId(openId, i);
            if (l.size() != 0){
                ScaleResult s = transResult(l.get(0));
                s.setTitle(nCovConfig.STUSCALENAMES[s.getScaleId()]);
                srs[i] = s;
            }else {
                ScaleResult s = new ScaleResult();
                s.setTitle(nCovConfig.STUSCALENAMES[i]);
                s.setScaleId(i);
                srs[i] = s;
            }
        }

        gsr.setScaleResults(srs);
        return gsr;
    }

    private static ScaleResult transResult(NCovResultModel n){
        ScaleResult s = new ScaleResult();
        if (n == null){
            return s;
        }
        s.setLevel(n.getLevel());
        s.setScaleId(n.getScaleId());
        s.setScore(n.getScore());
        return s;
    }
}
