package com.epic.followup.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * @author : zx
 * @version V1.0
 */

@Controller
@RequestMapping("/")
public class AiDoctor {

    @RequestMapping("")
    public String index() {
        return "redirect:/static/psychologicalManager/index.html";
    }

    /*
     * 用于微信认证
     */
    @GetMapping("kled6RWUya.txt")
    public String downloadZipFile(HttpServletRequest request, HttpServletResponse response){
        try{
            String filePathAndName = "kled6RWUya.txt";
            String fileRealName = "kled6RWUya.txt";
            // 以流的形式下载文件。
            File f = new File(filePathAndName);
            InputStream fis = new BufferedInputStream(new FileInputStream(filePathAndName));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            response.reset();
            // 设置response的Header
            response.setCharacterEncoding("utf-8");
            request.setCharacterEncoding("utf-8");
            response.addHeader("Content-Length", "" + f.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/x-download");
            String Agent = request.getHeader("User-Agent");
            if (null != Agent) {
                Agent = Agent.toLowerCase();
                if (Agent.indexOf("firefox") != -1) {
                    response.addHeader("Content-Disposition", String.format("attachment;filename*=utf-8'zh_cn'%s", URLEncoder.encode(fileRealName, "utf-8")));
                } else{
                    response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileRealName,"utf-8"));
                }
            }
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
            return null;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
