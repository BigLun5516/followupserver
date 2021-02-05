package com.epic.followup.repository.app;

import com.epic.followup.model.app.AppDoctorModel;
import com.epic.followup.temporary.app.doctor.LoginByEmployeeNumRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AppDoctorRepository extends JpaRepository<AppDoctorModel, Long> {

    AppDoctorModel findByEmployeeNum(String employeeNum);
    AppDoctorModel findByEmployeeNumAndPassword(String employeeNum, String password);
    Optional<AppDoctorModel> findByTel(String tel);
    <S extends AppDoctorModel> S save(S doctor);
    AppDoctorModel findByDoctorId(Long id);

    //管理系统
    // 单一条件查询
    @Query(nativeQuery = true, value = "SELECT d.id, d.employee_num, d.name, d.password, d.tel, d.department, d.title, d.photo, d.speciality FROM app_doctor d "+
            "WHERE d.department = ?1 OR d.title =?2 AND d.name LIKE %?3% ")
    List<AppDoctorModel> findByQuery1(String department, String title, String name);

    @Query(nativeQuery = true, value = "SELECT d.id, d.employee_num, d.name, d.password, d.tel, d.department, d.title, d.photo, d.speciality FROM app_doctor d "+
            "WHERE d.department = ?1 OR d.title =?2 OR d.name LIKE %?3% ")
    List<AppDoctorModel> findByQuery6(String department, String title, String name);

    // 两个条件查询
    @Query(nativeQuery = true, value = "SELECT d.id, d.employee_num, d.name, d.password, d.tel, d.department, d.title, d.photo, d.speciality FROM app_doctor d "+
            "WHERE d.department = ?1 AND d.title =?2 ")
    List<AppDoctorModel> findByQuery2(String department, String title);

    @Query(nativeQuery = true, value = "SELECT d.id, d.employee_num, d.name, d.password, d.tel, d.department, d.title, d.photo, d.speciality FROM app_doctor d "+
            "WHERE d.department = ?1 AND d.name LIKE %?2% ")
    List<AppDoctorModel> findByQuery3(String department, String name);

    @Query(nativeQuery = true, value = "SELECT d.id, d.employee_num, d.name, d.password, d.tel, d.department, d.title, d.photo, d.speciality FROM app_doctor d "+
            "WHERE d.title =?1 AND d.name LIKE %?2% ")
    List<AppDoctorModel> findByQuery4(String title, String name);

    // 三个条件查询
    @Query(nativeQuery = true, value = "SELECT d.id, d.employee_num, d.name, d.password, d.tel, d.department, d.title, d.photo, d.speciality FROM app_doctor d "+
            "WHERE d.department = ?1 AND d.title =?2 AND d.name LIKE %?3% ")
    List<AppDoctorModel> findByQuery5(String department, String title, String name);
}
