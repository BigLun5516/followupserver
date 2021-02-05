package com.epic.followup.service.managementSys.impl;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.model.followup2.student.StudentInfo;
import com.epic.followup.model.managementSys.CollegeModel;
import com.epic.followup.model.managementSys.PsychologicalCenterModel;
import com.epic.followup.model.managementSys.PsychologicalConsultantModel;
import com.epic.followup.model.managementSys.UniversityModel;
import com.epic.followup.repository.followup2.student.Answer2Repository;
import com.epic.followup.repository.followup2.student.Result2Repository;
import com.epic.followup.repository.followup2.student.ScaleResult2Repository;
import com.epic.followup.repository.followup2.student.StudentInfoRepository;
import com.epic.followup.repository.managementSys.*;
import com.epic.followup.service.managementSys.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author : zmm
 * @version V1.0
 */

@Service
public class OperationServiceImpl implements OperationService {

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private StudentInfoRepository studentInfoRepository;

    @Autowired
    private Answer2Repository answer2Repository;

    @Autowired
    private PsychologicalInfoRepository psychologicalInfoRepository;

    @Autowired
    private ScaleResult2Repository scaleResult2Repository;

    @Autowired
    private PsychologicalCenterRepository psychologicalCenterRepository;

    @Autowired
    private PsychologicalConsultantRepository psychologicalConsultantRepository;

    @Autowired
    private Result2Repository result2Repository;

    @Autowired
    private CollegeRepository collegeRepository;

    // 运行数据初始模块接口
    @Override
    public JSONObject getInitialInfo() {

        // 基础数据
        Map<String,Object> basicData = new HashMap<>();
        basicData.put("schoolNum", (int)universityRepository.count());
        basicData.put("userNum", (int)studentInfoRepository.count());
        basicData.put("evaNum", (int)answer2Repository.count());
        basicData.put("consultTime", psychologicalInfoRepository.getAllConsultationLength());

        // 重点高校前五
        List<UniversityModel> universityModelOrderByStar = universityRepository.getUniversityModelOrderByStar();
        List<Map<String, Object>> keyUniversity = new ArrayList<>();
        int i;
        for(i=1; i<5; i++){
            Map<String, Object> item = new HashMap<>();
            UniversityModel universityModel = universityModelOrderByStar.get(i);
            item.put("provinve", universityModel.getProvince());
            item.put("city", universityModel.getCity());
            item.put("name", universityModel.getUniversityName());
            // 占比*总人数
            item.put("activePeople", universityModel.getActiveNum());
            item.put("basePeople", universityModel.getAllNum());
            item.put("director", universityModel.getUniversityManager());
            item.put("evaNum", null);
            item.put("praise", null);
            item.put("star", universityModel.getStar());
            keyUniversity.add(item);
            System.out.println(i);
        }

        // 用户使用系统满意度
        Map<String, Object> satisfaction = new HashMap<>();
        satisfaction.put("五星好评", null);
        satisfaction.put("四星好评", null);
        satisfaction.put("三星好评", null);
        satisfaction.put("二星好评", null);
        satisfaction.put("一星好评", null);

        // 心理指标雷达图(18 19 20)
        List<Map<String, Object>> radar = new ArrayList<>();
        int year;
        for(year=2018; year<2021; year++){
            Integer suicide = scaleResult2Repository.getScaleByYear(year, 0, 4);
            Integer depress = scaleResult2Repository.getScaleByYear(year, 1, 4);
            Integer anxiety = scaleResult2Repository.getScaleByYear(year, 2, 4);
            Integer insomnia = scaleResult2Repository.getScaleByYear(year, 3, 7);
            Integer stress = scaleResult2Repository.getScaleByYear(year, 4, 8);
            Map<String, Object> item = new HashMap<>();
            item.put("suicide", suicide);
            item.put("depress", depress);
            item.put("anxiety", anxiety);
            item.put("insomnia", insomnia);
            item.put("other", stress);

            radar.add(item);
        }

        // 评测结果时间分布图
//        String startTime = "2021-01-30 20:00:00";
//        String endTime = "2021-01-30 23:59:59";
//        System.out.println(scaleResult2Repository.getScaleByDayTime(startTime, endTime));
        List<Map<String, Object>> timeGraph = new ArrayList<>();
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formatDate = formatter.format(date);
        String startTime, endTime;
        for(int start=0; start<=20; start=start+4){
            int end= start+3;
            startTime = formatDate+" "+start+":00:00";
            endTime = formatDate+" "+end+":59:59";
            System.out.println(startTime + " " + endTime);

            Integer suicide = scaleResult2Repository.getScaleByDayTime(startTime, endTime, 0, 4);
            Integer depress = scaleResult2Repository.getScaleByDayTime(startTime, endTime, 1, 4);
            Integer anxiety = scaleResult2Repository.getScaleByDayTime(startTime, endTime, 2, 4);
            Integer insomnia = scaleResult2Repository.getScaleByDayTime(startTime, endTime, 3, 7);
            Integer stress = scaleResult2Repository.getScaleByDayTime(startTime, endTime, 4, 8);

            Map<String, Object> item = new HashMap<>();
            item.put("suicide", suicide);
            item.put("depress", depress);
            item.put("anxiety", anxiety);
            item.put("insomnia", insomnia);
            item.put("other", stress);
            timeGraph.add(item);

        }

        // 高校活跃人数排行榜
        List<UniversityModel> universityModelOrderByActiveNum = universityRepository.getUniversityModelOrderByActiveNum();
        List<Map<String, Object>> activeRank = new ArrayList<>();
        int length;
        if(universityModelOrderByActiveNum.size()<10){
            length = universityModelOrderByActiveNum.size();
        }else {
            length = 10;
        }
        for(i=0; i<length; i++){
            Map<String, Object> item = new HashMap<>();
            UniversityModel universityModel = universityModelOrderByActiveNum.get(i);
            item.put("name", universityModel.getUniversityName());
            // 占比*总人数
            item.put("activeNum", universityModel.getActiveNum());
            item.put("totalNum", universityModel.getAllNum());
            activeRank.add(item);
        }

        JSONObject res = new JSONObject();
        res.put("basicData",basicData);
        res.put("keyUniversity",keyUniversity);
        res.put("satisfaction",satisfaction);
        res.put("radar",radar);
        res.put("timeGraph", timeGraph);
        res.put("activeRank", activeRank);
        res.put("errorCode", 200);
        res.put("errorMsg", "查询成功");
        return res;
    }

    // 心理咨询中心运营数据接口
    @Override
    public JSONObject getPsychologicalConsultData(String universityName) {

        UniversityModel universityModel = this.universityRepository.findByUniversityName(universityName);

        JSONObject res = new JSONObject();
        if(universityModel == null){
            res.put("errorCode", 500);
            res.put("errorMsg", "未查到该高校");
            return res;
        }

        int i;

        // 心理咨询中心基本数据
        UniversityModel byUniversityName = universityRepository.findByUniversityName(universityName);
        PsychologicalCenterModel byCenterId = psychologicalCenterRepository.findByCenterId(byUniversityName.getCenterId());
        Integer consultantNum = psychologicalConsultantRepository.getConsultantNum(byCenterId.getCenterId());
        Map<String, Object> basicData = new HashMap<>();
        basicData.put("centerName", byCenterId.getCenterName());
        basicData.put("director", byCenterId.getCenterManager());
        basicData.put("totalNum", byCenterId.getTotalNum());
        basicData.put("consultantNum", consultantNum);


        // 高校中各个学院的评测人次排名
        List<Map<String, Object>> evaRank = new ArrayList<>();
        List<Object> collegeUserCount = studentInfoRepository.getCollegeUserCount(universityName);

        int length;
        if(collegeUserCount.size()<10){
            length = collegeUserCount.size();
        }else {
            length = 10;
        }
        for(i=0; i<length; i++){
            Map<String, Object> item = new HashMap<>();
            Object[] obj = (Object[])collegeUserCount.get(i);
            item.put("name", obj[0]);
            item.put("evaNum", obj[1]);
            evaRank.add(item);
        }

        // 高校用户评测结果时间分布图
        List<Map<String, Object>> timeGraph = new ArrayList<>();
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formatDate = formatter.format(date);
        String startTime, endTime;
        for(int start=0; start<=20; start=start+4){
            int end= start+3;
            startTime = formatDate+" "+start+":00:00";
            endTime = formatDate+" "+end+":59:59";
            System.out.println(startTime + " " + endTime);
            Integer suicide = scaleResult2Repository.getScaleByDayTimeAndUni(startTime, endTime, 0, 4, universityName);
            Integer depress = scaleResult2Repository.getScaleByDayTimeAndUni(startTime, endTime, 1, 4, universityName);
            Integer anxiety = scaleResult2Repository.getScaleByDayTimeAndUni(startTime, endTime, 2, 4, universityName);
            Integer insomnia = scaleResult2Repository.getScaleByDayTimeAndUni(startTime, endTime, 3, 7, universityName);
            Integer stress = scaleResult2Repository.getScaleByDayTimeAndUni(startTime, endTime, 4, 8, universityName);

            Map<String, Object> item = new HashMap<>();
            item.put("suicide", suicide);
            item.put("depress", depress);
            item.put("anxiety", anxiety);
            item.put("insomnia", insomnia);
            item.put("other", stress);
            timeGraph.add(item);
        }

        // 用户使用系统满意度
        Map<String, Object> satisfaction = new HashMap<>();
        satisfaction.put("五星好评", null);
        satisfaction.put("四星好评", null);
        satisfaction.put("三星好评", null);
        satisfaction.put("二星好评", null);
        satisfaction.put("一星好评", null);

        // 高校男女人数
        List<StudentInfo> byDepartment = studentInfoRepository.findByDepartment(universityName);
        List<Integer> sexRatio = new ArrayList<>();
        int male = 0;
        int female = 0;
        for(StudentInfo studentInfo: byDepartment){
            if(studentInfo.getGender().equals("男")){
                male ++;
            }else if(studentInfo.getGender().equals("女")){
                female ++;
            }
        }
        sexRatio.add(0,male);
        sexRatio.add(1,female);


        // 心理指标雷达图(18 19 20)
        List<Map<String, Object>> radar = new ArrayList<>();
        int year;
        for(year=2018; year<2021; year++){
            Integer suicide = scaleResult2Repository.getScaleByYearAndUni(year, 0, 4, universityName);
            Integer depress = scaleResult2Repository.getScaleByYearAndUni(year, 1, 4, universityName);
            Integer anxiety = scaleResult2Repository.getScaleByYearAndUni(year, 2, 4, universityName);
            Integer insomnia = scaleResult2Repository.getScaleByYearAndUni(year, 3, 7, universityName);
            Integer stress = scaleResult2Repository.getScaleByYearAndUni(year, 4, 8, universityName);
            Map<String, Object> item = new HashMap<>();
            item.put("suicide", suicide);
            item.put("depress", depress);
            item.put("anxiety", anxiety);
            item.put("insomnia", insomnia);
            item.put("other", stress);

            radar.add(item);
        }

        // 高校关注学生 严重
        List<Map<String, Object>> seriousStu = getDegreeStudnetByUniversty(universityName, 2);

        //高校关注学生 一般
        List<Map<String, Object>> generalStu = getDegreeStudnetByUniversty(universityName, 1);

        //高校关注学生 轻微
        List<Map<String, Object>> slightStu = getDegreeStudnetByUniversty(universityName, 0);

        // 心理咨询中心的咨询师服务时长排行
        List<Map<String, Object>> consultantRank = new ArrayList<>();
        List<Object> consultationTimeList = psychologicalInfoRepository.getConsultationTimeList(universityName);
        Integer totalTime = psychologicalInfoRepository.getConsultationTimeUni(universityName);
        for(i=0;i<consultationTimeList.size();i++){
            Map<String, Object> item = new HashMap<>();
            Object[] obj = (Object[])consultationTimeList.get(i);
            Integer time = Integer.valueOf(obj[0].toString());
            item.put("id", obj[1]);
            item.put("time", time);
            item.put("name", obj[2]);
            Integer ratio = time*100/ totalTime;
            item.put("ratio", ratio);
            consultantRank.add(item);
        }

        res.put("basicData", basicData);
        res.put("evaRank", evaRank);
        res.put("timeGraph", timeGraph);
        res.put("sexRatio", sexRatio);
        res.put("satisfaction", satisfaction);
        res.put("radar", radar);
        res.put("seriousStu", seriousStu);
        res.put("generalStu", generalStu);
        res.put("slightStu", slightStu);
        res.put("consultantRank", consultantRank);
        res.put("errorCode", 200);
        res.put("errorMsg", "查询成功");

        return res;
    }

    // 高校运营数据接口
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
        List<Map<String, Object>> seriousStu = getDegreeStudnetByUniversty(universityName, 2);
        List<Map<String, Object>> generalStu = getDegreeStudnetByUniversty(universityName, 1);
        List<Map<String, Object>> slightStu = getDegreeStudnetByUniversty(universityName, 0);
        res.put("seriousStu", seriousStu);
        res.put("generalStu", generalStu);
        res.put("slightStu", slightStu);

        // 学校对应的心理咨询师
        List<PsychologicalConsultantModel> consultantModelList = psychologicalConsultantRepository.findByCenterId(universityModel.getCenterId());
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

    // 院系运营数据接口
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
        Map<String,Object> basicData = new HashMap<>();
        basicData.put("universityName", universityName);
        basicData.put("collegeName", collegeName);
        basicData.put("director", collegeModel.getCollegeManager());
        basicData.put("counselor", collegeModel.getCollegeCounselor());
        basicData.put("reacherNum", collegeModel.getTeacherNum());

        Integer maleNum = studentInfoRepository.countByCollegeIdAndGender(collegeModel.getCollegeId(), "男");
        Integer femaleNum = studentInfoRepository.countByCollegeIdAndGender(collegeModel.getCollegeId(), "女");
        basicData.put("stuNum", maleNum + femaleNum);

        // 学院关注学生人数
        List<Long> listSlightCollege = studentInfoRepository.getListUserIdCollege(collegeName, getSlightUserId(universityName));
        List<Long> listGeneralCollege = studentInfoRepository.getListUserIdCollege(collegeName, getGeneralUserId(universityName));
        List<Long> listSeriousCollege = studentInfoRepository.getListUserIdCollege(collegeName, getSeriousUserId(universityName));
        int slightNum = listSlightCollege.size();
        int generalNum = listGeneralCollege.size();
        int seriousNum = listSeriousCollege.size();
        basicData.put("slightNum", slightNum);
        basicData.put("generalNum", generalNum);
        basicData.put("seriousNum", seriousNum);
        res.put("basicData", basicData);


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
            String startTime = year + "-1-1";
            String endTime = year + "-12-31";
            Map<String, Object> item = new HashMap<>();
            for (int scaleId = 0; scaleId < 5; scaleId++) {
                Integer count = scaleResult2Repository.countScaleByTimeAndCollegeId(
                        startTime, endTime, scaleId, scoreList.get(scaleId), collegeModel.getCollegeId());
                item.put(scaleList.get(scaleId), count);
            }
            radar.add(item);
        }
        res.put("radar", radar);

        // 学院关注学生
        List<Map<String, Object>> seriousStu = getDegreeStudnetByCollege(universityName, collegeName, 2);
        List<Map<String, Object>> generalStu = getDegreeStudnetByCollege(universityName, collegeName, 1);
        List<Map<String, Object>> slightStu = getDegreeStudnetByCollege(universityName, collegeName, 0);
        res.put("seriousStu", seriousStu);
        res.put("generalStu", generalStu);
        res.put("slightStu", slightStu);

        // 高校中各个学院的评测人次排名
        List<Map<String, Object>> evaRank = new ArrayList<>();
        List<Object> collegeUserCount = studentInfoRepository.getCollegeUserCount(universityName);

        int length;
        if(collegeUserCount.size()<10){
            length = collegeUserCount.size();
        }else {
            length = 10;
        }
        int i;
        for(i=0; i<length; i++){
            Map<String, Object> item = new HashMap<>();
            Object[] obj = (Object[])collegeUserCount.get(i);
            item.put("name", obj[0]);
            item.put("evaNum", obj[1]);
            evaRank.add(item);
        }
        res.put("evaRank", evaRank);


        res.put("errorCode", 200);
        res.put("errorMsg", "查询成功");
        return res;
    }


    // 获取学校不同程度学生
    private  List<Map<String, Object>> getDegreeStudnetByUniversty(String department, int degree){
        List<Long> userId;
        switch (degree){
            case 0: // 轻度
            {
                userId = getSlightUserId(department);
                break;
            }
            case 1: {
                userId = getGeneralUserId(department);
                break;
            }
            case 2: {
                userId = getSeriousUserId(department);
                break;
            }
            default:
                return null;
        }
        List<Map<String, Object>> degreeStudent = getDegreeByUserId(userId);
        return degreeStudent;
    }

    // 获取学院不同程度学生
    private  List<Map<String, Object>> getDegreeStudnetByCollege(String department, String college, int degree){
        List<Long> userId;
        switch (degree){
            case 0: // 轻度
            {
                userId = studentInfoRepository.getListUserIdCollege(college, getSlightUserId(department));
                break;
            }
            case 1: {
                userId = studentInfoRepository.getListUserIdCollege(college, getGeneralUserId(department));
                break;
            }
            case 2: {
                userId = studentInfoRepository.getListUserIdCollege(college, getSeriousUserId(department));
                break;
            }
            default:
                return null;
        }
        List<Map<String, Object>> degreeStudentCollege = getDegreeByUserId(userId);
        return degreeStudentCollege;
    }

    // 根据List userId 获取不同程度学生信息
    private  List<Map<String, Object>> getDegreeByUserId(List<Long> userId){
        List<Map<String, Object>> degreeStudent = new ArrayList<>();
        int length;
        if (userId.size() < 10) {
            length = userId.size();
        } else {
            length = 10;
        }
        int i;
        for (i = 0; i < length; i++) {
            Long user = userId.get(i);
            Map<String, Object> item = getUserMap(user);
            degreeStudent.add(item);
        }
        return degreeStudent;
    }


    // 获取重度 学生Id
    private List<Long> getSeriousUserId(String university){

        List<Long> suicideUserId = scaleResult2Repository.getDegreeUserId(0, 14, 20, university);
        List<Long> depressUserId = scaleResult2Repository.getDegreeUserId(1, 14, 28, university);
        List<Long> anxietyUserId = scaleResult2Repository.getDegreeUserId(2, 14, 22, university);
        List<Long> insomniaUserId = scaleResult2Repository.getDegreeUserId(3, 21, 29, university);
        List<Long> stressUserId = scaleResult2Repository.getDegreeUserId(4, 43, 89, university);
        List<Long> seriousUserId = new ArrayList<>();

        seriousUserId.addAll(suicideUserId);
        seriousUserId.addAll(depressUserId);
        seriousUserId.addAll(anxietyUserId);
        seriousUserId.addAll(insomniaUserId);
        seriousUserId.addAll(stressUserId);
        // 去重
        seriousUserId = new ArrayList<Long>(new LinkedHashSet<>(seriousUserId));
        return seriousUserId;
    }

    // 获取中度学生Id
    private List<Long> getGeneralUserId(String university){
        List<Long> suicideUserId = scaleResult2Repository.getDegreeUserId(0, 9, 15, university);
        List<Long> depressUserId = scaleResult2Repository.getDegreeUserId(1, 9, 15, university);
        List<Long> anxietyUserId = scaleResult2Repository.getDegreeUserId(2, 9, 15, university);
        List<Long> insomniaUserId = scaleResult2Repository.getDegreeUserId(3, 14, 22, university);
        List<Long> stressUserId = scaleResult2Repository.getDegreeUserId(4, 25, 44, university);
        List<Long> generalUserId = new ArrayList<>();

        generalUserId.addAll(suicideUserId);
        generalUserId.addAll(depressUserId);
        generalUserId.addAll(anxietyUserId);
        generalUserId.addAll(insomniaUserId);
        generalUserId.addAll(stressUserId);
        // 去重
        generalUserId = new ArrayList<Long>(new LinkedHashSet<>(generalUserId));
        List<Long> seriousUserId = getSeriousUserId(university);
        generalUserId.removeAll(seriousUserId);

        return generalUserId;
    }

    // 获取轻度学生Id
    private List<Long> getSlightUserId(String university){
        List<Long> suicideUserId = scaleResult2Repository.getDegreeUserId(0, 4, 10, university);
        List<Long> depressUserId = scaleResult2Repository.getDegreeUserId(1, 4, 10, university);
        List<Long> anxietyUserId = scaleResult2Repository.getDegreeUserId(2, 4, 10, university);
        List<Long> insomniaUserId = scaleResult2Repository.getDegreeUserId(3, 7, 15, university);
        List<Long> stressUserId = scaleResult2Repository.getDegreeUserId(4, 8, 26, university);
        List<Long> slightUserId = new ArrayList<>();

        slightUserId.addAll(suicideUserId);
        slightUserId.addAll(depressUserId);
        slightUserId.addAll(anxietyUserId);
        slightUserId.addAll(insomniaUserId);
        slightUserId.addAll(stressUserId);
        // 去重
        slightUserId = new ArrayList<Long>(new LinkedHashSet<>(slightUserId));
        List<Long> seriousUserId = getSeriousUserId(university);
        slightUserId.removeAll(seriousUserId);
        List<Long> generalUserId = getGeneralUserId(university);
        slightUserId.removeAll(generalUserId);

        return slightUserId;
    }


    //症状 程度
    private String getLevel(int num, int score){
        switch (num){
            case 0:
                // 自杀检测
                if (score >= 0 && score <= 4){
                    return "";
                }else if (score >= 5 && score <= 9){
                    return "轻度自杀 ";
                }else if (score >= 10 && score <= 14){
                    return "中度自杀 ";
                }else if (score >= 15 && score <= 19){
                    return "重度自杀 ";
                }
                break;

            case 1:
                // 抑郁症筛查量表
                if (score >= 0 && score <= 4){
                    return "";
                }else if (score >= 5 && score <= 9){
                    return "轻度抑郁 ";
                }else if (score >= 10 && score <= 14){
                    return "中度抑郁 ";
                }else if (score >= 15 && score <= 27){
                    return "重度抑郁 ";
                }
                break;
            case 2:
                // 广泛性焦虑量表
                if (score >= 0 && score <= 4){
                    return "";
                }else if (score >= 5 && score <= 9){
                    return "轻度焦虑 ";
                }else if (score >= 10 && score <= 14){
                    return "中度焦虑 ";
                }else if (score >= 15 && score <= 21){
                    return "重度焦虑 ";
                }
                break;

            case 3:
                // 失眠严重指数
                if (score >= 0 && score <= 7){
                    return "";
                }else if (score >= 8 && score <= 14){
                    return "轻度失眠 ";
                }else if (score >= 15 && score <= 21){
                    return "中度失眠 ";
                }else if (score >= 22 && score <= 28){
                    return "重度失眠 ";
                }
                break;
            case 4:
                // 事件影响量表
                if (score >= 0 && score <= 8){
                    return "";
                }else if (score >= 9 && score <= 25){
                    return "轻度应激 ";
                }else if (score >= 26 && score <= 43){
                    return "中度应激 ";
                }else if (score >= 44 && score <= 88){
                    return "重度应激 ";
                }
                break;

            default:
                return "";
        }
        return "";
    }


    // 获取用户信息
    private Map<String, Object> getUserMap(Long userId) {
        Map<String, Object> item = new HashMap<>();
        Integer scoreSuicide = scaleResult2Repository.getScoreByUserId(0, userId);
        Integer scoreDepress = scaleResult2Repository.getScoreByUserId(1, userId);
        Integer scoreAnxiety = scaleResult2Repository.getScoreByUserId(2, userId);
        Integer scoreInsomnia = scaleResult2Repository.getScoreByUserId(3, userId);
        Integer scoreStress = scaleResult2Repository.getScoreByUserId(4, userId);
        String symptom = "";
        if(scoreSuicide != null){
            symptom += getLevel(0,scoreSuicide);
        }
        if(scoreDepress != null){
            symptom += getLevel(1, scoreDepress);
        }
        if(scoreAnxiety != null){
            symptom += getLevel(2, scoreAnxiety);
        }
        if(scoreInsomnia != null){
            symptom += getLevel(3, scoreInsomnia);
        }
        if(scoreStress != null){
            symptom += getLevel(4, scoreStress);
        }

        Optional<StudentInfo> byUserId = studentInfoRepository.findByUserId(userId);
        item.put("name", byUserId.get().getStname());
        item.put("age", byUserId.get().getAge());
        item.put("symptom", symptom);
        item.put("intervention", null);
        return item;
    }

}
