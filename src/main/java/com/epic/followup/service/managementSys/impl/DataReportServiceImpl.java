package com.epic.followup.service.managementSys.impl;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.repository.app.MiniScaleRepository;
import com.epic.followup.repository.followup2.BaseUserRepository;
import com.epic.followup.repository.followup2.doctor.StudentResultRepository;
import com.epic.followup.repository.followup2.student.Answer2Repository;
import com.epic.followup.repository.followup2.student.StudentInfoRepository;
import com.epic.followup.repository.managementSys.NewStudentScaleRepository;
import com.epic.followup.repository.managementSys.UserRepository;
import com.epic.followup.repository.managementSys.scale.ScaleRepository;
import com.epic.followup.repository.ncov.ScaleResultRepository;
import com.epic.followup.service.managementSys.DataReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DataReportServiceImpl implements DataReportService {

    @Autowired
    StudentInfoRepository studentInfoRepository;

    @Autowired
    BaseUserRepository baseUserRepository;

    @Autowired
    Answer2Repository answer2Repository;

    @Autowired
    ScaleResultRepository scaleResultRepository;

    @Autowired
    MiniScaleRepository miniScaleRepository;

    @Autowired
    StudentResultRepository studentResultRepository;

    @Autowired
    NewStudentScaleRepository newStudentScaleRepository;

    @Override
    public JSONObject universitySourceDistribution(JSONObject param) {

        // 获取参数
        Integer userUniversityId = param.getInteger("userUniversityId");
        String sType = param.getString("sType");
        String yiyuLabelInt = param.getString("yiyuLabel");

        // 处理参数
        String yiyuLabel = null;
        switch (yiyuLabelInt) {
            case "" : {
                yiyuLabel = "";
                break;
            }
            case "0" : {
                yiyuLabel = "轻度抑郁";
                break;
            }
            case "1" : {
                yiyuLabel = "中度抑郁";
                break;
            }
            case "2" : {
                yiyuLabel = "重度抑郁";
                break;
            }
        }

        JSONObject res = new JSONObject();

        if (userUniversityId == null || userUniversityId == -1) {
            res.put("errorCode", 500);
            res.put("errorMsg", "当前用户不属于任何学校");
            return res;
        }

        List<Object> provinceList = studentInfoRepository.countProvinceByUniversityId(userUniversityId, sType, yiyuLabel);
        List<Map> stuData = new ArrayList<>();
        for (Object o : provinceList) {
            Object[] oo = (Object[]) o;
            HashMap<String, Object> item = new HashMap();
            item.put("name", oo[0]);
            item.put("num", oo[1]);
            stuData.add(item);
        }
        res.put("stuData", stuData);

        List<Object> genderList = studentInfoRepository.countGenderByUniversityId(userUniversityId, sType, yiyuLabel);
        List<Map> sexRatio = new ArrayList<>();
        for (Object o : genderList) {
            Object[] oo = (Object[]) o;
            HashMap<String, Object> item = new HashMap<>();
            item.put("name", oo[0]);
            item.put("value", oo[1]);
            sexRatio.add(item);
        }
        res.put("sexRatio", sexRatio);

        res.put("errorCode", 200);
        res.put("errorMsg", "查询成功");

        return res;
    }

    @Override
    public JSONObject collegeStuNumDistribution(JSONObject param) {

        // 获取参数
        Integer userUniversityId = param.getInteger("userUniversityId");
        String sType = param.getString("sType");
        String yiyuLabelInt = param.getString("yiyuLabel");

        // 处理参数
        String yiyuLabel = null;
        switch (yiyuLabelInt) {
            case "" : {
                yiyuLabel = "";
                break;
            }
            case "0" : {
                yiyuLabel = "轻度抑郁";
                break;
            }
            case "1" : {
                yiyuLabel = "中度抑郁";
                break;
            }
            case "2" : {
                yiyuLabel = "重度抑郁";
                break;
            }
        }

        JSONObject res = new JSONObject();

        if (userUniversityId == null || userUniversityId == -1) {
            res.put("errorCode", 500);
            res.put("errorMsg", "当前用户不属于任何学校");
            return res;
        }

        List<Object> collegeStuNumList = studentInfoRepository.countCollegeStuNumByUniversityId(userUniversityId, sType, yiyuLabel);
        List<Map> data = new ArrayList<>();
        for (Object o : collegeStuNumList) {
            Object[] oo = (Object[]) o;
            HashMap<String, Object> item = new HashMap<>();
            item.put("id", oo[0]);
            item.put("name", oo[1]);
            item.put("num", oo[2]);
            data.add(item);
        }
        res.put("data", data);

        res.put("errorCode", 200);
        res.put("errorMsg", "查询成功");

        return res;
    }

    @Override
    public JSONObject getCollegeSourceDistribution(JSONObject param) {

        // 获取参数
        Integer collegeId = param.getInteger("collegeId");
        String sType = param.getString("sType");
        String yiyuLabelInt = param.getString("yiyuLabel");

        // 处理参数
        String yiyuLabel = null;
        switch (yiyuLabelInt) {
            case "" : {
                yiyuLabel = "";
                break;
            }
            case "0" : {
                yiyuLabel = "轻度抑郁";
                break;
            }
            case "1" : {
                yiyuLabel = "中度抑郁";
                break;
            }
            case "2" : {
                yiyuLabel = "重度抑郁";
                break;
            }
        }

        JSONObject res = new JSONObject();

        List<Object> collegeStuNumList = studentInfoRepository.countProvinceByCollegeId(collegeId, sType, yiyuLabel);
        List<Map> stuData = new ArrayList<>();
        for (Object o : collegeStuNumList) {
            Object[] oo = (Object[]) o;
            HashMap<String, Object> item = new HashMap<>();
            item.put("name", oo[0]);
            item.put("num", oo[1]);
            stuData.add(item);
        }
        res.put("stuData", stuData);

        List<Object> collegeGenderList = studentInfoRepository.countGenderByCollegeId(collegeId, sType, yiyuLabel);
        List<Map> sexRatio = new ArrayList<>();
        for (Object o : collegeGenderList) {
            Object[] oo = (Object[]) o;
            HashMap<String, Object> item = new HashMap<>();
            item.put("name", oo[0]);
            item.put("value", oo[1]);
            sexRatio.add(item);
        }
        res.put("sexRatio", sexRatio);

        res.put("errorCode", 200);
        res.put("errorMsg", "查询成功");

        return res;
    }

    @Override
    public JSONObject getEvaluationReport(JSONObject param) {

        // 获取参数
        Integer userUniversityId = param.getInteger("userUniversityId");
        String sType = param.getString("sType");
        String collegeId = param.getString("collegeId");

        JSONObject res = new JSONObject();

        if (userUniversityId == null || userUniversityId == -1) {
            res.put("errorCode", 500);
            res.put("errorMsg", "当前用户不属于任何学校");
            return res;
        }

        Integer userNum = baseUserRepository.countUserByUniversityId(userUniversityId, sType, collegeId);
        res.put("personNum", userNum);

        Integer answerNum = answer2Repository.countEvaluationFromAnswerByUniversityId(userUniversityId, sType, collegeId, "1970-1-1", "3000-1-1");
        Integer ncovresultNum = scaleResultRepository.countEvaluationFromNcovresultByUniversityId(userUniversityId, sType, collegeId, "1970-1-1", "3000-1-1");
        Integer miniNum = miniScaleRepository.countEvaluationFromMiniscaleByUniversityId(userUniversityId, sType, collegeId, "1970-1-1", "3000-1-1");
        Integer newstudentsaleNum = newStudentScaleRepository.countEvaluationFromNewstudentscaleByUniversityId(userUniversityId, sType, collegeId, "1970-1-1", "3000-1-1");

        res.put("evaNum", answerNum / 20 + ncovresultNum / 4 + miniNum + newstudentsaleNum);

        Integer seriousNum = studentResultRepository.countSeriousStuNumByUniversityId(userUniversityId, sType, collegeId, "1970-1-1", "3000-1-1");
        res.put("seriousNum", seriousNum);

        List<Integer> evaTrend = new ArrayList<>();
        List<Integer> seriousTrend = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -6);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < 6; i++) {
            calendar.add(Calendar.MONTH, 1);
            String startDate, endDate;

            calendar.set(Calendar.DAY_OF_MONTH, 1);
            startDate = dateFormat.format(calendar.getTime());

            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            endDate = dateFormat.format(calendar.getTime());

            answerNum = answer2Repository.countEvaluationFromAnswerByUniversityId(userUniversityId, sType, collegeId, startDate, endDate);
            ncovresultNum = scaleResultRepository.countEvaluationFromNcovresultByUniversityId(userUniversityId, sType, collegeId, startDate, endDate);
            miniNum = miniScaleRepository.countEvaluationFromMiniscaleByUniversityId(userUniversityId, sType, collegeId, startDate, endDate);
            newstudentsaleNum = newStudentScaleRepository.countEvaluationFromNewstudentscaleByUniversityId(userUniversityId, sType, collegeId, startDate, endDate);
            evaTrend.add(answerNum / 20 + ncovresultNum / 4 + miniNum + newstudentsaleNum);

            seriousNum = studentResultRepository.countSeriousStuNumByUniversityId(userUniversityId, sType, collegeId, startDate, endDate);
            seriousTrend.add(seriousNum);
        }
        res.put("trendDate", evaTrend);
        res.put("trendDate2", seriousTrend);

        res.put("errorCode", 200);
        res.put("errorMsg", "查询成功");

        return res;
    }
}
