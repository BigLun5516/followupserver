package com.epic.followup.controller.managementSys.pic;

import com.alibaba.fastjson.JSONObject;
import com.epic.followup.service.managementSys.pic.PicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
@RequestMapping("/managementSystem/pic")
public class PicController {

    @Autowired
    private PicService picService;

    // 搜索图文
    @PostMapping("/find")
    @ResponseBody
    public JSONObject findPic(@RequestBody JSONObject params){
        return picService.findPic(params);
    }

    // 新增图文
    @PostMapping("/insert")
    @ResponseBody
    public JSONObject insertPic(@RequestBody JSONObject params){
        return picService.insertPic(params);
    }

    // 删除图文
    @PostMapping("/delete")
    @ResponseBody
    public JSONObject deletePic(@RequestBody JSONObject params){
        return picService.deletePic(params);
    }

    // 编辑图文
    @PostMapping("/edit")
    @ResponseBody
    public JSONObject editPic(@RequestBody JSONObject params){
        return picService.editPic(params);
    }
}
