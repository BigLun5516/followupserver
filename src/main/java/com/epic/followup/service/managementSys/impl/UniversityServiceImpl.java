package com.epic.followup.service.managementSys.impl;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.managementSys.PsychologicalConsultantModel;
import com.epic.followup.model.managementSys.UniversityModel;
import com.epic.followup.repository.followup2.student.Answer2Repository;
import com.epic.followup.repository.followup2.student.ScaleResult2Repository;
import com.epic.followup.repository.followup2.student.StudentInfoRepository;
import com.epic.followup.repository.managementSys.CollegeRepository;
import com.epic.followup.repository.managementSys.PsychologicalConsultantRepository;
import com.epic.followup.repository.managementSys.UniversityRepository;
import com.epic.followup.service.managementSys.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UniversityServiceImpl implements UniversityService {

    @Autowired
    UniversityRepository universityRepository;

    @Autowired
    PsychologicalConsultantRepository consultantRepository;

    @Autowired
    StudentInfoRepository studentInfoRepository;

    // answer表
    @Autowired
    Answer2Repository answer2Repository;
    
    // ncov_result表
    @Autowired
    ScaleResult2Repository scaleResult2Repository;

    @Autowired
    CollegeRepository collegeRepository;

    // 查询所有高校的信息
    @Override
    public JSONObject findAllUniversity(JSONObject params) {
        Integer pageNum = params.getInteger("pageNum");
        Integer pageSize = params.getInteger("pageSize");

        Page<UniversityModel> universityModelPages = universityRepository.findAll(PageRequest.of(pageNum - 1, pageSize));

        JSONObject res = new JSONObject();

        if(universityModelPages.isEmpty()){
            res.put("errorCode", 500);
            res.put("errorMsg", "未查询到数据");
            return res;
        }
        res.put("errorCode", 200);
        res.put("errorMsg", "查询成功");
        res.put("totalNum", (int)universityRepository.count());
        res.put("page", pageNum);
        List<Map<String, Object>> keyUniversity = new ArrayList<>();
        for(UniversityModel universityModel : universityModelPages){
            Map<String, Object> item = new HashMap<>();
            item.put("provinve", universityModel.getProvince());
            item.put("city", universityModel.getCity());
            item.put("name", universityModel.getUniversityName());
            item.put("activePeople", universityModel.getActiveNum());
            item.put("basePeople", universityModel.getAllNum());
            item.put("director", universityModel.getUniversityManager());

            Integer answerNum = answer2Repository.countAnswerByUniversityId(universityModel.getUniversityId());
            Integer resultNum = scaleResult2Repository.countResultByUniversityId(universityModel.getUniversityId());
            item.put("evaNum", answerNum + resultNum);
//            item.put("praise", null);
//            item.put("star", universityModel.getStar());
            keyUniversity.add(item);
        }
        res.put("keyUniversity", keyUniversity);
        return res;
    }

    // 指定高校数据界面（根据名字）
    @Override
    public JSONObject getUniversityData(JSONObject params){

        String universityName = params.getString("universityName");

        UniversityModel universityModel = this.universityRepository.findByUniversityName(universityName);

        JSONObject res = new JSONObject();
        if(universityModel == null){
            res.put("errorCode", 500);
            res.put("errorMsg", "未查到该高校");
            return res;
        }


        // 高校信息
        Map<String, Object> basicData = new HashMap<>();
        basicData.put("universityName", universityName);
        basicData.put("director", universityModel.getUniversityManager());
        basicData.put("totalNum", universityModel.getAllNum());
        basicData.put("teacherNum", universityModel.getTeacherNum());
        basicData.put("stuNum", universityModel.getStudentNum());
        res.put("basicData", basicData);

        // 学院-评测人数（前十）
        List<Map<String, Object>> evaRank = new ArrayList<>();
        List<Object> evaNum_collegeName = this.collegeRepository.countEvaNumByUniversityId_GroupByCollegeId(universityModel.getUniversityId());
        for (Object o : evaNum_collegeName) {
            Object[] oo = (Object[]) o;
            Map<String , Object> item = new HashMap<>();
            item.put("name", oo[0]);
            item.put("evaNum", oo[1]);
            evaRank.add(item);
        }
        res.put("eavRank", evaRank);

        // 用户评测结果时间分布图
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
                Integer count = scaleResult2Repository.countScaleByTimeAndUniversityId(
                        startTime, endTime, scaleId, scoreList.get(scaleId), universityModel.getUniversityId());
                item.put(scaleList.get(scaleId), count);
            }
            timeGraph.add(item);
        }
        res.put("timeGraph", timeGraph);

        // 高校中男女生人数
        List<Integer> genderList = new ArrayList<>();
        genderList.add(studentInfoRepository
                .countByUniversityIdAndGender(universityModel.getUniversityId()
                        , "男"));
        genderList.add(studentInfoRepository
                .countByUniversityIdAndGender(universityModel.getUniversityId()
                        , "女"));
        res.put("sexRatio", genderList);

        // 高校中心理指标雷达图
        List<Map<String, Object>> radar = new ArrayList<>();
        for (int year = 2018; year < 2021; year++) {
            String startTime = year + "-1-1";
            String endTime = year + "-12-31";
            Map<String, Object> item = new HashMap<>();
            for (int scaleId = 0; scaleId < 5; scaleId++) {
                Integer count = scaleResult2Repository.countScaleByTimeAndUniversityId(
                        startTime, endTime, scaleId, scoreList.get(scaleId), universityModel.getUniversityId());
                item.put(scaleList.get(scaleId), count);
            }
            radar.add(item);
        }
        res.put("radar", radar);

        // 学校关注学生


        // 学校对应的心理咨询师
        List<PsychologicalConsultantModel> consultantModelList = consultantRepository.findByCenterId(universityModel.getCenterId());
        if(consultantModelList.size() == 0){
            res.put("consultants", null);
        } else {
            List<Map<String, Object>> consultants = new ArrayList<>();
            for (PsychologicalConsultantModel consultant : consultantModelList){
                Map<String, Object> item = new HashMap<>();
                item.put("id", consultant.getConsultantId());
                item.put("name", consultant.getConsultantName());
                consultants.add(item);
            }
            res.put("consultants", consultants);
        }


        res.put("errorCode", 200);
        res.put("errorMsg", "查询成功");
        return res;
    }
}
