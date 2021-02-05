package com.epic.followup.temporary.app.patient;


public class AppInformationRequest {
    //电话
    private String tel;

    //昵称
    private String userName;

    // 头像
    private String img;

    // 性别
    private int gender;

    // 出生年月
    private String birth;

    // 职业
    private String occupation;

    // 职业类型
    private String occupationType;

    // 是否为在校学生
    private int isStudent;

    // 学校
    private String university;

    // 学院
    private String college;

    // 专业
    private String major;

    // 用户类型
    private int userType;

    // 疾病类型
    private int diseaseType;

    // 状态
    private int psychoStatus;

    // 医院
    private int hospital;

    // 科室
    private String department;

    // 医生
    private String therapist;


    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getOccupationType() {
        return occupationType;
    }

    public void setOccupationType(String occupationType) {
        this.occupationType = occupationType;
    }

    public int getIsStudent() {
        return isStudent;
    }

    public void setIsStudent(int isStudent) {
        this.isStudent = isStudent;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getDiseaseType() {
        return diseaseType;
    }

    public void setDiseaseType(int diseaseType) {
        this.diseaseType = diseaseType;
    }

    public int getPsychoStatus() {
        return psychoStatus;
    }

    public void setPsychoStatus(int psychoStatus) {
        this.psychoStatus = psychoStatus;
    }

    public int getHospital() {
        return hospital;
    }

    public void setHospital(int hospital) {
        this.hospital = hospital;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTherapist() {
        return therapist;
    }

    public void setTherapist(String therapist) {
        this.therapist = therapist;
    }
}
