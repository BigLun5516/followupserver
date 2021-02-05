package com.epic.followup.controller;

import com.alibaba.fastjson.JSON;
import com.epic.followup.model.WechatFileModel;
import com.epic.followup.service.WechatFileService;
import com.epic.followup.service.WechatUserService;
import com.epic.followup.temporary.DealMessageResponse;
import com.epic.followup.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : zx
 * @version V1.0
 */

@Controller
@RequestMapping("/upload")
public class Upload {

    @Autowired
    WechatUserService wechatUserService;
    @Autowired
    WechatFileService wechatFileService;


    @RequestMapping(value = "")
    @ResponseBody
    public String upload(@RequestParam(value = "file", required = true) MultipartFile file,
                         @RequestParam(value = "openid", required = true) String openId,
                         @RequestParam(value = "session_key", required = true) String sessionKey,
                         @RequestParam(value = "picOrVid", required = true) String picOrVid,
                         @RequestParam(value = "fileName", required = true) String fileName,
                         @RequestParam(value = "question", required = true) int question) throws Exception {

        int type = Integer.valueOf(picOrVid);
        DealMessageResponse dm = new DealMessageResponse();

        // 校验用户身份合法性
        if (!wechatUserService.checkUser(openId , sessionKey)){
            dm.setErrorCode(500);
            dm.setErrorMsg("/wechat/chart: 用户登录状态异常");
            return JSON.toJSONString(dm);
        }

        String fileNowName = null;
        Map<String, Object> param = new HashMap<>();
        String relativePath = null;


        InputStream in = file.getInputStream();
        // 获取系统时间
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        if (file.isEmpty()) {
            dm.setErrorCode(504);
            dm.setErrorMsg("文件不能为空");
            return JSON.toJSONString(dm);
        }

        try {
            String path1 = "./fufiles/";
            path1 = path1 + "upload";
            //创建一个文件夹
            Date date = new Date();
            //文件夹的名称
            String path2 = new SimpleDateFormat("yyyyMMdd").format(date);
            String paths = path1 + path2;

            //如果不存在,创建文件夹
            File f = new File(paths);

            fileName = file.getOriginalFilename(); // 获取原名称
            fileNowName = UUIDUtil.getUUID2() + "." + fileName.substring(fileName.lastIndexOf(".") + 1); // 生成唯一的名字 没有需要的包
            File dest = new File(paths + "/" + fileNowName);
            relativePath = paths + "/" + fileNowName;
            dest=new File(dest.getAbsolutePath());

            long lastModified=dest.lastModified();

            if (!f.exists()) {
                f.mkdirs();
                //如果文件夹不存在创建一个
                file.transferTo(dest);
            } else {
                file.transferTo(dest);
            }

            WechatFileModel wfm = new WechatFileModel();

           wfm.setOpenId(openId);
           wfm.setPath(relativePath);
           wfm.setUploadTime(date);
           wfm.setType(type);
           wfm.setNumber(question);
           wechatFileService.insert(wfm);

        } catch (Exception e) {
            e.printStackTrace();
            dm.setErrorMsg("上传失败");
            dm.setErrorCode(505);
            return JSON.toJSONString(dm);
        }
        dm.setErrorCode(200);
        dm.setErrorMsg("succ");
        return JSON.toJSONString(dm);
    }
}
