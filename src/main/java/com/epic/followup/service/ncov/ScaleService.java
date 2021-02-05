package com.epic.followup.service.ncov;

import com.epic.followup.util.CsvToJsonUtil;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.NotDirectoryException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : zx
 * @version V1.0
 * 用于从csv中读取问题并转换json字符串
 */

@Component
public class ScaleService {
    private Map<String, String> scales = new HashMap<>();

    public ScaleService(){
        System.out.println(System.getProperty("user.dir"));
        String dirpath = "./ncov";

        try {
            for (File f : getFiles(dirpath)){
                scales.put(f.getName(), CsvToJsonUtil.getJSONFromFile(f.getAbsolutePath(), "\\,"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // 获取问题
    public String getQuesJson(String file){
        return scales.get(file);
    }

    private static File[] getFiles(String dirpath)throws NotDirectoryException{

        File f = new File(dirpath);
        if (f.isDirectory()) {
            return f.listFiles();
        }else {
            throw new NotDirectoryException(f.getName());
        }
    }

    public static void main(String[] args){
        ScaleService s = new ScaleService();
        System.out.println(s.getQuesJson("3.csv"));
    }
}
