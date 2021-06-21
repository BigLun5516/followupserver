package com.epic.followup.service.managementSys.impl;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.managementSys.UniversityModel;
import com.epic.followup.repository.managementSys.CityRepository;
import com.epic.followup.repository.managementSys.UniversityRepository;
import com.epic.followup.service.managementSys.PublicityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PublicityServiceImpl implements PublicityService {
    @Autowired
    private UniversityRepository universityRepository;
    @Autowired
    private CityRepository cityRepository;

    //高校推广分布接口
    @Override
    public JSONObject publicityUniversity() {

        //基础数据 basicData
        //获取新增相关数据
        List<UniversityModel> today = universityRepository.getToAdd();
        List<UniversityModel> yesterday = universityRepository.getYesAdd();
        List<UniversityModel> week = universityRepository.getWeekAdd();
        List<UniversityModel> month = universityRepository.getMonthAdd();

        Map<String, Object> basicData = new HashMap<>();
        basicData.put("totalNum", (int) universityRepository.count());
        basicData.put("todayAdd", today.size());  //todayAdd
        basicData.put("yesAdd", yesterday.size());//yesAdd
        basicData.put("weekAdd", week.size());//weekAdd
        basicData.put("monthAdd", month.size());//monthAdd

        //高校排名覆盖数前15名 coverRank
        List<Object> cityModelOrderByCover = cityRepository.getCityModelOrderByCover();
        List<Map<String, Object>> keyCityTop15 = new ArrayList<>();
        int i;
        int universityNum = 0;//

        for (Object objX : cityModelOrderByCover) {
            Object[] obj = (Object[]) objX;
            universityNum = universityNum + Integer.valueOf(obj[1].toString());//计算总数
        }

        int count = 0;
        for (Object objY : cityModelOrderByCover) {
            Object[] obj = (Object[]) objY;
            Map<String, Object> item = new HashMap<>();

            item.put("name", obj[0]);
            Integer coverNum = Integer.valueOf(obj[1].toString());
            item.put("coverNum", coverNum);
            Integer time = Integer.valueOf(obj[1].toString()) * 100 / universityNum;
            item.put("percentage", time);
            //item.put("percentage",0);
            keyCityTop15.add(item);
            count = count + 1;
            if (count == 15) {
                break;
            }
        }

        //由覆盖数而得的中国地图 map
        List<Object> getCityGraphData = universityRepository.getCityGraphData();
        List<Map<String, Object>> keyCityGraph = new ArrayList<>();
        int provinceNum = getCityGraphData.size();
        for (int k = 0; k < provinceNum; k++) {
            Map<String, Object> item = new HashMap<>();
            Object[] obj = (Object[]) getCityGraphData.get(k);
            item.put("name", obj[0]);
            Integer valueMap = Integer.valueOf(obj[1].toString());
            item.put("value", valueMap);
            keyCityGraph.add(item);
        }

        //
        JSONObject res = new JSONObject();
        res.put("basicData", basicData);
        res.put("coverRank", keyCityTop15);
        res.put("map", keyCityGraph);
        res.put("errorCode", 200);
        res.put("errorMsg", "查询成功");
        return res;
    }
}
