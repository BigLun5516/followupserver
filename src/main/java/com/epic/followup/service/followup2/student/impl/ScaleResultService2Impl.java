package com.epic.followup.service.followup2.student.impl;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.conf.NCovConfig;
import com.epic.followup.model.followup2.student.NCovResultModel;
import com.epic.followup.repository.followup2.student.ScaleResult2Repository;
import com.epic.followup.service.followup2.student.ScaleResult2Service;
import com.epic.followup.temporary.ncov.GetScaleResultResponse;
import com.epic.followup.temporary.ncov.ScaleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author : zx
 * @version V1.0
 */

@Service
public class ScaleResultService2Impl implements ScaleResult2Service {
    @Autowired
    ScaleResult2Repository scaleResult2Repository;
    @Autowired
    private NCovConfig nCovConfig;

    @Override
    public void saveResult(NCovResultModel r) {
        scaleResult2Repository.save(r);
    }

    @Override
    public GetScaleResultResponse getRecentScales(long userId) {
        GetScaleResultResponse gsr = new GetScaleResultResponse();
        ScaleResult[] srs = new ScaleResult[nCovConfig.SCALENUM];

        for (int i = 1; i <= nCovConfig.SCALENUM; i++){
            List<NCovResultModel> l = scaleResult2Repository.getLastScaleByOpenIdAndScaleId(userId, i);
            if (l.size() != 0){
                ScaleResult s = transResult(l.get(0));
                s.setTitle(nCovConfig.SCALENAMES2[s.getScaleId()]);
                srs[i-1] = s;
            }else {
                ScaleResult s = new ScaleResult();
                s.setTitle(nCovConfig.SCALENAMES2[i]);
                s.setScaleId(i);
                srs[i-1] = s;
            }
        }

        gsr.setScaleResults(srs);
        return gsr;
    }

    @Override
    public List<Map<String, String>> getHistoryDate(Long userId) {
        List list = scaleResult2Repository.getLastScaleByUserId(userId);
        List<Map<String, String>> time = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            Map<String, String> map = new HashMap<>();
            Object[] o = (Object[])list.get(i);
            map.put("date",(String) o[0]);
            map.put("level", (String) o[1]);
            time.add(map);
        }
        return time;

    }

    @Override
    public void deleteResult(long userId, long count) {
        scaleResult2Repository.deleteResult(userId, count);
    }

    @Override
    public GetScaleResultResponse getResultByDate(long userId, String beforeDate, String afterDate) {
        GetScaleResultResponse gsr = new GetScaleResultResponse();
        ScaleResult[] srs = new ScaleResult[nCovConfig.SCALENUM];

        for (int i = 1; i <= nCovConfig.SCALENUM; i++){
            List<NCovResultModel> l = scaleResult2Repository.getResultByDate(userId, i, beforeDate, afterDate);
            if (l.size() != 0){
                ScaleResult s = transResult(l.get(0));
                s.setTitle(nCovConfig.SCALENAMES2[s.getScaleId()]);
                srs[i-1] = s;
            }else {
                ScaleResult s = new ScaleResult();
                s.setTitle(nCovConfig.SCALENAMES2[i]);
                s.setScaleId(i);
                srs[i-1] = s;
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
