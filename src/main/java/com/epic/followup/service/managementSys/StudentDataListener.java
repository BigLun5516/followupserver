package com.epic.followup.service.managementSys;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.epic.followup.model.followup2.student.StudentInfo;
import com.epic.followup.model.managementSys.CollegeModel;
import com.epic.followup.model.managementSys.StudentExcelData;
import com.epic.followup.model.managementSys.UniversityModel;
import com.epic.followup.repository.followup2.student.StudentInfoRepository;
import com.epic.followup.repository.managementSys.CollegeRepository;
import com.epic.followup.repository.managementSys.UniversityRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 处理导入学生数据Excel数据的监听器
 */
public class StudentDataListener extends AnalysisEventListener<StudentExcelData> {

    /**
     * 每隔BATCH_COUNT条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 3000;
    List<StudentInfo> list = new ArrayList<>();

    private StudentInfoRepository studentRepository;

    private UniversityRepository universityRepository;

    private CollegeRepository collegeRepository;

    public StudentDataListener() {
        super();
    }

    public StudentDataListener(
            StudentInfoRepository studentRepository, UniversityRepository universityRepository
            , CollegeRepository collegeRepository){
        this.studentRepository = studentRepository;
        this.universityRepository = universityRepository;
        this.collegeRepository = collegeRepository;
    }

    @Override
    public void invoke(StudentExcelData studentExcelData, AnalysisContext analysisContext) {

        UniversityModel universityModel = universityRepository.findByUniversityName(studentExcelData.getUniversityName());

        CollegeModel collegeModel = collegeRepository.findCollegeByCollegeNameAndUniversityName(studentExcelData.getCollegeName(), studentExcelData.getUniversityName());

        StudentInfo studentInfo = new StudentInfo();
        studentInfo.setUniversityId(universityModel.getUniversityId());
        studentInfo.setCollegeId(collegeModel.getCollegeId());
        studentInfo.setDepartment(studentExcelData.getUniversityName());
        studentInfo.setCollege(studentExcelData.getCollegeName());
        studentInfo.setStid(studentExcelData.getStudentId());
        studentInfo.setStype(studentExcelData.getStudenType());
        studentInfo.setYear(studentExcelData.getYear());
        studentInfo.setProvince(studentExcelData.getProvince());
        studentInfo.setAge(studentExcelData.getAge());
        studentInfo.setCreateTime(new Date());
        studentInfo.setGender(studentExcelData.getGender());

        list.add(studentInfo);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        saveData();
    }

    private void saveData() {
        studentRepository.saveAll(list);
    }
}
