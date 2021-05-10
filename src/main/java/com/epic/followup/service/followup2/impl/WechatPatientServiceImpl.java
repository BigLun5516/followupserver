package com.epic.followup.service.followup2.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epic.followup.conf.PatientConfig;
import com.epic.followup.conf.WeChatConfig;
import com.epic.followup.model.app.MiniScaleModel;
import com.epic.followup.model.app.PatientBodyInformationModel;
import com.epic.followup.model.app.PatientDiaryModel;
import com.epic.followup.repository.app.MiniScaleRepository;
import com.epic.followup.repository.app.PatientBodyInformationRepository;
import com.epic.followup.repository.app.PatientDiaryRepository;
import com.epic.followup.service.followup2.WechatPatientService;
import com.epic.followup.temporary.DealMessageResponse;
import com.epic.followup.temporary.app.MiniSubmitRequest;
import com.epic.followup.temporary.followup2.session.BaseUserSession;
import com.epic.followup.temporary.wechat.patient.diary.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class WechatPatientServiceImpl implements WechatPatientService {
    @Autowired
    private PatientDiaryRepository patientDiaryRepository;

    @Autowired
    PatientBodyInformationRepository patientBodyInformationRepository;

    @Autowired
    MiniScaleRepository miniScaleRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WeChatConfig weChatConfig;



    @Override
    public upLoadDiaryImgResponse uploadDiaryImg(MultipartFile file){
        upLoadDiaryImgResponse res=new upLoadDiaryImgResponse();
        if(file.getOriginalFilename().equals("")){
            res.setErrorCode(502);
            res.setErrorMsg("文件名不能为空");
            res.setImgUrl("");
        }
        try {
            String path = PatientConfig.store_diary;//"static/diaryImg";
            //获取文件名
            String fileName = file.getOriginalFilename();
            //创建一个UUID用时间戳表示
            String UUID = new Date().getTime() + "-";
            //组合成新文件名避免有重复的文件名
            String newFileName = UUID + fileName;
            File destFile = new File(new File(path).getAbsolutePath()+ "/" + newFileName);
            //判断该文件下的上级文件夹是否存在 不存在创建
            if(!destFile.getParentFile().exists()) {
                destFile.getParentFile().mkdirs();
            }
            //上传文件
            file.transferTo(destFile);//这一步结束就上传成功了。
            res.setErrorCode(200);
            res.setErrorMsg("上传文件成功");
            res.setImgUrl(PatientConfig.diary_query+newFileName);
        } catch (Exception e) {
            e.printStackTrace();
            res.setErrorCode(502);
            res.setErrorMsg("上传文件失败");
            res.setImgUrl("");
        }
        return res;
    }

    @Override
    public savePatientDiaryResponse saveDiary(BaseUserSession bps, savePatientDiaryRequest saveDiaryRequest){
        savePatientDiaryResponse res=new savePatientDiaryResponse();
        PatientDiaryModel diary=new PatientDiaryModel();
        if(saveDiaryRequest.getId()==-1){//新建日记
            diary.setBrief(saveDiaryRequest.getBrief());
            diary.setHtml(saveDiaryRequest.getHtml());
            diary.setImg(saveDiaryRequest.getImg());
            diary.setMood(saveDiaryRequest.getMood());
            diary.setTime(saveDiaryRequest.getTime());
            diary.setTel(bps.getTel());
            //判断是否含有自杀的想法
            String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
            Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            Matcher m_html = p_html.matcher(saveDiaryRequest.getHtml());
            String temp = m_html.replaceAll(""); // 过滤html标签
            JSONObject req = new JSONObject();
            req.put("msg",temp);
            diary.setZisha(Integer.parseInt(this.restTemplate.postForObject(weChatConfig.getIsZisha(), req, String.class)));

            patientDiaryRepository.save(diary);
            List<PatientDiaryModel> ou = patientDiaryRepository.findAllByTel(bps.getTel());
            PatientDiaryModel lasttm=ou.get(ou.size()-1);
            res.setId(lasttm.getId());
            res.setErrorCode(200);
            res.setErrorMsg("日记插入成功");
            return res;
        }else{//更新日记
            Optional<PatientDiaryModel> ou = patientDiaryRepository.findById(saveDiaryRequest.getId());
            if (!ou.isPresent()){
                res.setId((long) -1);
                res.setErrorCode(504);
                res.setErrorMsg("不存在这篇日记");
                return res;
            }
            diary.setId(saveDiaryRequest.getId());
            diary.setBrief(saveDiaryRequest.getBrief());
            diary.setHtml(saveDiaryRequest.getHtml());
            diary.setImg(saveDiaryRequest.getImg());
            diary.setMood(saveDiaryRequest.getMood());
            diary.setTime(saveDiaryRequest.getTime());
            diary.setTel(bps.getTel());
            //判断是否含有自杀的想法
            String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
            Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            Matcher m_html = p_html.matcher(saveDiaryRequest.getHtml());
            String temp = m_html.replaceAll(""); // 过滤html标签
            System.out.println("内容："+temp);
            JSONObject req = new JSONObject();
            req.put("msg",temp);
            diary.setZisha(Integer.parseInt(this.restTemplate.postForObject(weChatConfig.getIsZisha(), req, String.class)));

            patientDiaryRepository.save(diary);
            res.setId(diary.getId());
            res.setErrorCode(200);
            res.setErrorMsg("日记更新成功");
            return res;
        }
    }

    @Override
    public getPatientDiaryResponse getPatientDiaryById(diaryIdRequest diaryId){
        Long id=diaryId.getId();
        getPatientDiaryResponse res=new getPatientDiaryResponse();
        Optional<PatientDiaryModel> ou = patientDiaryRepository.findById(id);
        if (!ou.isPresent()){
            res.setId((long) -1);
            res.setHtml("");
            res.setMood("");
            res.setTime("");
            res.setErrorCode(504);
            res.setErrorMsg("不存在这篇日记");
            return res;
        }
        PatientDiaryModel diary=ou.get();
        res.setId(diary.getId());
        res.setHtml(diary.getHtml());
        res.setMood(diary.getMood());
        res.setTime(diary.getTime());
        res.setErrorCode(200);
        res.setErrorMsg("查找成功");
        return res;
    }

    @Override
    public DealMessageResponse delPatientDiaryById(diaryIdRequest diaryId){
        Long id=diaryId.getId();
        DealMessageResponse res=new DealMessageResponse();
        Optional<PatientDiaryModel> ou = patientDiaryRepository.findById(id);
        if (!ou.isPresent()){
            res.setErrorCode(504);
            res.setErrorMsg("不存在这篇日记");
            return res;
        }
        patientDiaryRepository.deleteById(id);
        res.setErrorCode(200);
        res.setErrorMsg("删除成功");
        return res;
    }

    @Override
    public getAllPatientDiaryResponse getAllPatientDiary(BaseUserSession bps){
        getAllPatientDiaryResponse res=new getAllPatientDiaryResponse();
        List<PatientDiaryModel> ou = patientDiaryRepository.findAllByTel(bps.getTel());
        List<Diary> res_diary=new LinkedList<>();
        for (PatientDiaryModel patientDiaryModel : ou) {
            Diary temp=new Diary();
            temp.setBrief(patientDiaryModel.getBrief());
            temp.setId(patientDiaryModel.getId());
            temp.setImg(patientDiaryModel.getImg());
            temp.setMood(patientDiaryModel.getMood());
            temp.setTime(patientDiaryModel.getTime());
            res_diary.add(temp);
        }
        res.setDiaries(res_diary);
        res.setErrorCode(200);
        res.setErrorMsg("查找成功");
        return res;
    }

    @Override
    public getAllMoodsResponse getAllPatientMoods(BaseUserSession bps) {
        List list = patientDiaryRepository.findBySqlTel(bps.getTel());
        List<MoodList> moodLists = new ArrayList<>();
        for (Object row : list){
            Object[] cells = (Object[]) row;
            Map m = new HashMap();
            moodLists.add(new MoodList(cells[0].toString(), cells[1].toString()));
        }
        Collections.reverse(moodLists);
        getAllMoodsResponse res = new getAllMoodsResponse();
        res.setMoods(moodLists);
        res.setErrorCode(200);
        res.setErrorMsg("查找成功");
        return res;
    }

    @Override
    public DealMessageResponse saveorupdateInformation(BaseUserSession bps, JSONObject information){
        DealMessageResponse res=new DealMessageResponse();
        Optional<PatientBodyInformationModel> ou = patientBodyInformationRepository.findByTimeAndPid(information.getString("datetime"),bps.getUserId());
        if (!ou.isPresent()) {
            PatientBodyInformationModel bi = new PatientBodyInformationModel();
            bi.setAppetite(information.getString("appetite"));
            bi.setTime(information.getString("datetime"));
            bi.setPain(information.getString("pain"));
            bi.setSleep(information.getString("sleep"));
            bi.setWeight(information.getFloat("weight"));
            bi.setPid(bps.getUserId());
            patientBodyInformationRepository.save(bi);
            res.setErrorCode(200);
            res.setErrorMsg("插入成功");
            return res;
        }else{
            PatientBodyInformationModel p=ou.get();
            p.setAppetite(information.getString("appetite"));
            p.setPain(information.getString("pain"));
            p.setSleep(information.getString("sleep"));
            p.setWeight(information.getFloat("weight"));
            patientBodyInformationRepository.save(p);
            res.setErrorCode(200);
            res.setErrorMsg("更新成功");
            return res;
        }
    }

    @Override
    public JSONObject retrieveInformation(BaseUserSession bps,JSONObject information){
        Optional<PatientBodyInformationModel> ou = patientBodyInformationRepository.findByTimeAndPid(information.getString("datetime"),bps.getUserId());
        JSONObject res=new JSONObject();
        if (!ou.isPresent()) {
            res.put("errorCode",502);
            res.put("errorMsg","未查找到信息");
        }else{
            PatientBodyInformationModel p=ou.get();
            res.put("errorCode",200);
            res.put("errorMsg","查找成功");
            res.put("pain",p.getPain());
            res.put("appetite",p.getAppetite());
            res.put("sleep",p.getSleep());
            res.put("weight",p.getWeight());

        }
        return res;
    }

    @Override
    public JSONObject getSevenDaysBodyInfo(BaseUserSession bps) {
        List<PatientBodyInformationModel>  bodyInfoList = patientBodyInformationRepository.findBodyInfoByPid(bps.getUserId());
        JSONObject res=new JSONObject();
        if (bodyInfoList == null || bodyInfoList.isEmpty()) {
            res.put("errorCode",502);
            res.put("errorMsg","未查找到信息");
        }else{
            Collections.reverse(bodyInfoList);
            res.put("errorCode",200);
            res.put("errorMsg","查找成功");
            res.put("data",bodyInfoList);
        }
        return res;
    }

    @Override
    public JSONObject getDaysByMonth(BaseUserSession bps, String month) {
        List<String>  bodyInfoList = patientBodyInformationRepository.findDayByMonth(bps.getUserId(), month);
        JSONObject res=new JSONObject();
        if (bodyInfoList == null || bodyInfoList.isEmpty()) {
            res.put("errorCode",502);
            res.put("errorMsg","未查找到信息");
        }else{
            res.put("errorCode",200);
            res.put("errorMsg","查找成功");
            res.put("data",bodyInfoList);
        }
        return res;
    }

    @Override
    public JSONObject getScaleHistory(Long userId) {

        return null;
    }


    /**
     * 保存mini量表结果
     * @param userId
     * @param miniSubmitRequest
     * @return
     */

    @Override
    public DealMessageResponse saveMiniResult(Long userId, MiniSubmitRequest miniSubmitRequest){
        DealMessageResponse dealMessageResponse = new DealMessageResponse();

        MiniScaleModel miniScaleModel = new MiniScaleModel();
        miniScaleModel.setUserId(userId);
        miniScaleModel.setMiniAnswer(miniSubmitRequest.getAnswer());
        miniScaleModel.setMiniResult(miniSubmitRequest.getResult());
        miniScaleModel.setMiniTime(new Date());
        miniScaleRepository.save(miniScaleModel);


        dealMessageResponse.setErrorCode(200);
        dealMessageResponse.setErrorMsg("succ");

        return dealMessageResponse;
    }
}
