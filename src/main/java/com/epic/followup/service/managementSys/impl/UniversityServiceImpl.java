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
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.ParseException;
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

    // 日期格式
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 查询所有高校的信息
     */

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

    /**
     * 根据条件查询
     */
    @Override
    public JSONObject findUniversity(JSONObject params) {
        // 获取请求参数
        String universityName = params.getString("universityName");
        String universityDirector = params.getString("universityDirector");
        Integer universityStatus = params.getInteger("universityStatus");
        List<String> filterDates = params.getObject("filterDates", List.class);
        Integer pageNum = params.getInteger("pageNum");
        Integer pageSize = params.getInteger("pageSize");

        JSONObject res = new JSONObject();
        Specification<UniversityModel> spec;

        spec = new Specification<UniversityModel>() {

            @SneakyThrows
            @Override
            public Predicate toPredicate(Root<UniversityModel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if(universityName != null){
                    predicateList.add(criteriaBuilder.like(root.get("universityName"), "%" + universityName + "%"));
                }
                if (universityDirector != null){
                    predicateList.add(criteriaBuilder.equal(root.get("universityManager"), universityDirector));
                }
                if (universityStatus != null){
                    predicateList.add(criteriaBuilder.equal(root.get("universityStatus"), universityStatus));
                }
                if (filterDates.get(0) != ""){
                    predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("joinDate"), dateFormat.parse(filterDates.get(0))));
                }
                if (filterDates.get(1) != ""){
                    predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("joinDate"), dateFormat.parse(filterDates.get(1))));
                }

                Predicate[] arr = new Predicate[predicateList.size()];
                return criteriaBuilder.and(predicateList.toArray(arr));
            }
        };

        Page<UniversityModel> universityModelPages = universityRepository.findAll(spec, PageRequest.of(pageNum - 1, pageSize));

        // 高校总数量
        List<UniversityModel> universityModelList = universityRepository.findAll(spec);
        res.put("totalNum", universityModelList.size());

        // 高校管理表
        List<Map<String, Object>> universityManagementTable = new ArrayList<>();
        List<String> directorList = new ArrayList<>();
        for (UniversityModel universityModel : universityModelPages){
            Map<String, Object> item = new HashMap<>();
            item.put("id", universityModel.getUniversityId());
            item.put("universityName", universityModel.getUniversityName());
            item.put("universityAddress", universityModel.getAddress());
            item.put("universityStatus", universityModel.getUniversityStatus());
            item.put("notice", universityModel.getNotice());
            String universityManager = universityModel.getUniversityManager();
            item.put("universityDirector", universityManager);
            item.put("directorPhone", universityModel.getManagerPhone());
            item.put("createTime", universityModel.getJoinDate());
            universityManagementTable.add(item);

            if (universityManager != null){
                directorList.add(universityManager);
            }
        }
        res.put("universityManagementTable", universityManagementTable);

        // 所有负责人名单
        res.put("directorList", directorList);

        res.put("errorCode", 200);
        res.put("errorMsg", "查询成功");

        return res;
    }

    /**
     * 删除
     */
    @Override
    public JSONObject deleteUniversity(JSONObject params) {

        // 请求参数
        Integer id = params.getInteger("id");

        JSONObject res = new JSONObject();

        try {
            universityRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            res.put("errorCode", 500);
            res.put("errorMsg", "删除失败");
            return res;
        }

        res.put("errorCode", 200);
        res.put("errorMsg", "删除成功");
        return res;
    }

    /**
     * 新增
     */
    @Override
    public JSONObject insertUniversity(JSONObject params) {

        String universityName = params.getString("universityName");
//        String campus = params.getString("campus");
        String universityAddress = params.getString("universityAddress");
        Integer universityStatus = params.getInteger("universityStatus");
        String notice = params.getString("notice");
        String universityDirector = params.getString("universityDirector");
        String directorPhone = params.getString("directorPhone");
        String createTime = params.getString("createTime");

        JSONObject res = new JSONObject();

        UniversityModel universityModel = new UniversityModel();
        universityModel.setUniversityName(universityName);
//        universityModel.setCampus(campus);
        universityModel.setAddress(universityAddress);
        universityModel.setUniversityStatus(universityStatus);
        universityModel.setNotice(notice);
        universityModel.setUniversityManager(universityDirector);
        universityModel.setManagerPhone(directorPhone);

        try {
            Date createDate = dateFormat.parse(createTime);
            universityModel.setJoinDate(createDate);
        } catch (ParseException e) {
            e.printStackTrace();
            res.put("errorCode", 500);
            res.put("errorMsg", "插入失败");
            return res;
        }

        UniversityModel save = universityRepository.save(universityModel);
        res.put("errorCode", 200);
        res.put("errorMsg", "插入成功");
        return res;
    }

    /**
     * 编辑
     */
    @Override
    public JSONObject editUniversity(JSONObject params) {

        Integer id = params.getInteger("id");
        String universityName = params.getString("universityName");
//        String campus = params.getString("campus");
        String universityAddress = params.getString("universityAddress");
        Integer universityStatus = params.getInteger("universityStatus");
        String notice = params.getString("notice");
        String universityDirector = params.getString("universityDirector");
        String directorPhone = params.getString("directorPhone");
        String createTime = params.getString("createTime");

        JSONObject res = new JSONObject();

        Optional<UniversityModel> optional = universityRepository.findById(id);
        if (!optional.isPresent()){
            res.put("errorCode", 500);
            res.put("errorMsg", "编辑失败");
            return res;
        }
        UniversityModel universityModel = optional.get();
        universityModel.setUniversityName(universityName);
//        universityModel.setCampus(campus);
        universityModel.setAddress(universityAddress);
        universityModel.setUniversityStatus(universityStatus);
        universityModel.setNotice(notice);
        universityModel.setUniversityManager(universityDirector);
        universityModel.setManagerPhone(directorPhone);

        try {
            Date createDate = dateFormat.parse(createTime);
            universityModel.setJoinDate(createDate);
        } catch (ParseException e) {
            e.printStackTrace();
            res.put("errorCode", 500);
            res.put("errorMsg", "编辑失败");
            return res;
        }

        UniversityModel save = universityRepository.save(universityModel);
        res.put("errorCode", 200);
        res.put("errorMsg", "编辑成功");
        return res;
    }

    // 获取所有高校名称
    @Override
    public JSONObject getAllUniversityName() {
        List<String> allUniversityName = universityRepository.getAllUniversityName();
        JSONObject res = new JSONObject();
        if(allUniversityName.isEmpty()){
            res.put("errorCode", 500);
            res.put("errorMsg", "学校列表为空");
        }
        res.put("schoolNameList", allUniversityName);
        res.put("errorCode", 200);
        res.put("errorMsg", "查询学校名称成功");
        return res;
    }
}
