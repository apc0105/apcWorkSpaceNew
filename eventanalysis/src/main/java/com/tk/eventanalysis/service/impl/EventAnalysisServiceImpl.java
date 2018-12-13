package com.tk.eventanalysis.service.impl;

import com.alibaba.fastjson.JSON;
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
            // log.info("-----------upload start-----baseFilePath--------"+baseFilePath+"------");
            FileInputStream fs = (FileInputStream) file.getInputStream();

            String fileName = file.getOriginalFilename();
            String suffix = fileName.substring(fileName.indexOf('.'), fileName.length());

            String filePath = System.currentTimeMillis() + suffix;

            File localfile = new File(baseFilePath + filePath);
            //  log.info("-----------upload start-----localfile--------"+localfile+"------");
            FileOutputStream fos = new FileOutputStream(localfile);//上传地址
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = fs.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            fs.close();
            //  log.info("-----------upload end----------");

            //上传成功，调取接口
            String requestUrl = env.getProperty("clientUrl") + filePath;

            //  log.info("-----------http start-----requestUrl--------"+requestUrl+"------");

            String result = httpServiceUtil.executePost(requestUrl, "");

            //  log.info("-----------http result----------"+result+"--------");

            if (!TypeChecker.isEmpty(result)) {

                resultMap.put("actives", JSON.parse(result));

            }
            //  log.info("-----------http end-------------------");
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
        //  log.info("-----------rediskey keyword--------"+keyword+"-----------");

        if (!redisUtil.hasKey(keyword)) {
            //    log.info("-----------rediskey null-------------------");
            return Response.fail();
        }
        //  log.info("-----------redisValue------------"+redisUtil.get(keyword)+"-------");

        List<EventAnalysis> list = JSON.parseArray(redisUtil.get(keyword).toString(), EventAnalysis.class);

        //  log.info("-----------listValue------------"+list+"-------");

        List<EventAnalysis> eventAnalysisList = getEventAnalysisPage(pageNumber, pageSize, list);

        int totalPageNum = (list.size() + pageSize - 1) / pageSize;

        resultMap.put("current_page", pageNumber);
        resultMap.put("total_page", totalPageNum);
        resultMap.put("eventAnalysisList", eventAnalysisList);

        return Response.ok(resultMap);
    }


    /**
     * 数据分页操作
     */
    private List<EventAnalysis> getEventAnalysisPage(int pageNumber, int pageSize, List<EventAnalysis> list) {

        List<EventAnalysis> eventAnalyses = new ArrayList<EventAnalysis>();

        int currIdx = (pageNumber > 1 ? (pageNumber - 1) * pageSize : 0);

        for (int i = 0; i < pageSize && i < list.size() - currIdx; i++) {
            EventAnalysis eventAnalysis = new EventAnalysis();

            EventAnalysis ea = list.get(currIdx+i);

            BeanUtils.copyProperties(ea, eventAnalysis);

            eventAnalyses.add(eventAnalysis);
        }

        return eventAnalyses;
    }

    @Autowired
    private HttpServiceUtil httpServiceUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private Environment env;

    private static Logger log = LoggerFactory.getLogger(EventAnalysisServiceImpl.class);
}
