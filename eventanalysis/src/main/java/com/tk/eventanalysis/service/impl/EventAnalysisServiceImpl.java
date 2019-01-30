package com.tk.eventanalysis.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tk.eventanalysis.model.AnalysisDetail;
import com.tk.eventanalysis.model.EventAnalysis;
import com.tk.eventanalysis.service.EventAnalysisService;
import com.tk.eventanalysis.support.Response;
import com.tk.eventanalysis.util.HttpServiceUtil;
import com.tk.eventanalysis.util.RedisUtil;
import com.tk.eventanalysis.util.TypeChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("eventAnalysisService")
public class EventAnalysisServiceImpl implements EventAnalysisService {


    @Override
    public Response uploadFile(MultipartFile file) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        try {

            String baseFilePath = env.getProperty("filePath.uploadPath");
            FileInputStream fs = (FileInputStream) file.getInputStream();

            String fileName = file.getOriginalFilename();
            String suffix = fileName.substring(fileName.lastIndexOf('.'), fileName.length());

            String filePath = System.currentTimeMillis() + suffix;

            File localfile = new File(baseFilePath + filePath);
            FileOutputStream fos = new FileOutputStream(localfile);//上传地址
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = fs.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            fs.close();

            //上传成功，调取接口
            String requestUrl = env.getProperty("newsEventUrl") + filePath;


            String result = httpServiceUtil.executePost(requestUrl, "");


            if (!TypeChecker.isEmpty(result)) {

                resultMap.put("actives", JSON.parse(result));

            }
        } catch (IOException e) {
            e.printStackTrace();

            return Response.error();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error();
        }

        return Response.ok(resultMap);
    }

    @Override
    public Response getAnalysisData(int pageNumber, int pageSize, String keyword) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        if (!redisUtil.hasKey(keyword)) {
            return Response.fail();
        }

        List<EventAnalysis> list = JSON.parseArray(redisUtil.get(keyword).toString(), EventAnalysis.class);


        List<EventAnalysis> eventAnalysisList = getEventAnalysisPage(pageNumber, pageSize, list);

        int totalPageNum = (list.size() + pageSize - 1) / pageSize;

        resultMap.put("current_page", pageNumber);
        resultMap.put("total_page", totalPageNum);
        resultMap.put("eventAnalysisList", eventAnalysisList);

        return Response.ok(resultMap);
    }


    /**
     * 事件分析数据分页操作
     */
    private List<EventAnalysis> getEventAnalysisPage(int pageNumber, int pageSize, List<EventAnalysis> list) {

        List<EventAnalysis> eventAnalyses = new ArrayList<EventAnalysis>();

        int currIdx = (pageNumber > 1 ? (pageNumber - 1) * pageSize : 0);

        for (int i = 0; i < pageSize && i < list.size() - currIdx; i++) {
            EventAnalysis eventAnalysis = new EventAnalysis();

            EventAnalysis ea = list.get(currIdx + i);

            BeanUtils.copyProperties(ea, eventAnalysis);

            eventAnalyses.add(eventAnalysis);
        }

        return eventAnalyses;
    }

    @Override
    public Response getAnalysisDetail(int pageNumber, int pageSize, String uid, int index, String newstarget) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        String requestUrl = env.getProperty("newsDetailsUrl");

        String jsonParameters= "{\"UID\":\"" +uid+ "\",\"index\":" +index+ ",\"newstarget\":\"" +newstarget+ "\"}";

        String result = httpServiceUtil.executePost(requestUrl, jsonParameters);

        List<AnalysisDetail> list=new ArrayList<AnalysisDetail>();
        List<AnalysisDetail> eventAnalysisDetailList=new ArrayList<AnalysisDetail>();
        int totalPageNum=1;

        if (!TypeChecker.isEmpty(result)) {
            JSONObject object= (JSONObject) JSON.parse(result);

            String UID=object.getString("UID");

            list= JSON.parseArray((String) redisUtil.get(UID),AnalysisDetail.class);

        }

        if(list.size()>0){
            eventAnalysisDetailList = getAnalysisDetailPage(pageNumber, pageSize, list);
            totalPageNum = (list.size() + pageSize - 1) / pageSize;
        }

        resultMap.put("current_page", pageNumber);
        resultMap.put("total_page", totalPageNum);
        resultMap.put("eventAnalysisDetailList", eventAnalysisDetailList);
        return Response.ok(resultMap);
    }

    /**
     * 事件分析详情数据分页操作
     */
    private List<AnalysisDetail> getAnalysisDetailPage(int pageNumber, int pageSize, List<AnalysisDetail> list) {

        List<AnalysisDetail> analysisDetails = new ArrayList<AnalysisDetail>();

        int currIdx = (pageNumber > 1 ? (pageNumber - 1) * pageSize : 0);

        for (int i = 0; i < pageSize && i < list.size() - currIdx; i++) {
            AnalysisDetail analysisDetail = new AnalysisDetail();

            AnalysisDetail detail = list.get(currIdx + i);

            BeanUtils.copyProperties(detail, analysisDetail);

            analysisDetails.add(analysisDetail);
        }

        return analysisDetails;
    }

    @Autowired
    private HttpServiceUtil httpServiceUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private Environment env;

    private static Logger log = LoggerFactory.getLogger(EventAnalysisServiceImpl.class);
}
