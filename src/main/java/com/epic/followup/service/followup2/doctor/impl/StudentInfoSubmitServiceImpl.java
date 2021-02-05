package com.epic.followup.service.followup2.doctor.impl;

import com.alibaba.fastjson.JSON;
import com.epic.followup.model.followup2.doctor.StudentInfoSubmitModel;
import com.epic.followup.model.followup2.student.WechatFileModel;
import com.epic.followup.repository.followup2.doctor.StudentInfoSubmitRepository;
import com.epic.followup.repository.followup2.student.StudentInfoRepository;
import com.epic.followup.service.followup2.doctor.StudentInfoSubmitService;
import com.epic.followup.temporary.DealMessageResponse;
import com.epic.followup.temporary.followup2.session.BaseUserSession;
import com.epic.followup.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : zx
 * @version V1.0
 */
@Service
public class StudentInfoSubmitServiceImpl implements StudentInfoSubmitService {

    private StudentInfoSubmitRepository studentInfoSubmitRepository;

    @Autowired
    public StudentInfoSubmitServiceImpl(StudentInfoSubmitRepository studentInfoSubmitRepository){
        this.studentInfoSubmitRepository = studentInfoSubmitRepository;
    }

    @Override
    public boolean save(HttpSession session, MultipartFile file) throws IOException {

        InputStream in = file.getInputStream();
        // 获取系统时间
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        if (file.isEmpty()) {
            return false;
        }

        try {
            String path1 = "./StudentInfo/students/";
            path1 = path1 + session.getAttribute("department");
            //创建一个文件夹
            Date date = new Date();
            //文件夹的名称
            String path2 = new SimpleDateFormat("yyyyMMdd").format(date);
            String paths = path1 + "/" + path2;

            //如果不存在,创建文件夹
            File f = new File(paths);

            String fileName = file.getOriginalFilename(); // 获取原名称
            String fileNowName = (String)session.getAttribute("department") + "-" +
                    session.getAttribute("college") + "-" +
                    session.getAttribute("userName") + "-" +
                    UUIDUtil.getUUID2() + "." + fileName.substring(fileName.lastIndexOf(".") + 1); // 生成唯一的名字 没有需要的包
            File dest = new File(paths + "/" + fileNowName);
            String relativePath = paths + "/" + fileNowName;
            dest=new File(dest.getAbsolutePath());

            long lastModified=dest.lastModified();

            if (!f.exists()) {
                f.mkdirs();
                //如果文件夹不存在创建一个
                file.transferTo(dest);
            } else {
                file.transferTo(dest);
            }
            StudentInfoSubmitModel s = new StudentInfoSubmitModel();
            s.setPath(relativePath);
            s.setUploadTime(date);
            s.setType(1);
            s.setSucc(1);
            this.studentInfoSubmitRepository.save(s);

        } catch (Exception e) {

            return false;
        }
        return true;
    }
}
