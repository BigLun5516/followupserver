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
    @PostMapping("/searchPic")
    @ResponseBody
    public JSONObject searchPic(@RequestBody JSONObject params){
        return picService.searchPic(params);
    }
}
