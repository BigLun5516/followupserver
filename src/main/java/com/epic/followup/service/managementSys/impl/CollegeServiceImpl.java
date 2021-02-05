package com.epic.followup.service.managementSys.impl;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.managementSys.CollegeModel;
import com.epic.followup.repository.followup2.student.ScaleResult2Repository;
import com.epic.followup.repository.followup2.student.StudentInfoRepository;
import com.epic.followup.repository.managementSys.CollegeRepository;
import com.epic.followup.service.managementSys.CollegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CollegeServiceImpl implements CollegeService {

    @Autowired
    CollegeRepository collegeRepository;

    @Autowired
    StudentInfoRepository studentInfoRepository;

    @Autowired
    ScaleResult2Repository scaleResult2Repository;

    @Override
    public JSONObject getCollegeData(String universityName, String collegeName) {

        JSONObject res = new JSONObject();
        CollegeModel collegeModel = collegeRepository.findCollegeByCollegeNameAndUniversityName(collegeName, universityName);

        if (collegeModel == null){
            res.put("errorCode", "500");
            res.put("errorMsg", "未查询到这个院");
            return res;
        }

        // 学院基本信息
        res.put("universityName", universityName);
        res.put("collegeName", collegeName);
        res.put("director", collegeModel.getCollegeManager());
        res.put("counselor", collegeModel.getCollegeCounselor());
        res.put("reacherNum", collegeModel.getTeacherNum());

        Integer maleNum = studentInfoRepository.countByCollegeIdAndGender(collegeModel.getCollegeId(), "男");
        Integer femaleNum = studentInfoRepository.countByCollegeIdAndGender(collegeModel.getCollegeId(), "女");
        res.put("stuNum", maleNum + femaleNum);


        // 学院关注学生人数

        // 评测结果时间分布图
        Date todayDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String todayDateStr = dateFormat.format(todayDate);
        List<String> scaleList = new ArrayList<String>(){{
            add("suicide"); add("depress"); add("anxiety"); add("insomnia"); add("others");
        }};
        List<Integer> scoreList = new ArrayList<Integer>(){{
            add(4); add(4); add(4); add(7); add(8);
        }};
        List<Map<String, Object>> timeGraph = new ArrayList<>();
        for (int i = 0; i <24; i = i + 4) {
            String startTime = todayDateStr + " " + i + ":0:0";
            String endTime = todayDateStr + " " + (i + 3) + ":59:59";
            Map<String, Object> item = new HashMap<>();
            for (int scaleId = 0; scaleId < 5; scaleId++) {
                Integer count = scaleResult2Repository.countScaleByTimeAndCollegeId(
                        startTime, endTime, scaleId, scoreList.get(scaleId), collegeModel.getCollegeId());
                item.put(scaleList.get(scaleId), count);
            }
            timeGraph.add(item);
        }
        res.put("timeGraph", timeGraph);


        // 学院中男女生人数
        List<Integer> sexRatio = new ArrayList<>();
        sexRatio.add(maleNum);
        sexRatio.add(femaleNum);
        res.put("sexRatio", sexRatio);

        // 学院中心理指标雷达图（按年）
        List<Map<String, Object>> radar = new ArrayList<>();
        for (int year = 2018; year < 2021; year++) {

        }

        // 学院关注学生



        res.put("errorCode", 200);
        res.put("errorMsg", "查询成功");
        return res;
    }
}
