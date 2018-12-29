package com.news.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.news.model.Quantization;
import com.news.service.QuantizationService;
import com.news.support.Response;
import com.news.util.RedisUtil;
import com.news.util.SortList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("quantizationService")
public class QuantizationServiceImpl implements QuantizationService {

    @Override
    public Response findQuantizations(String days, int pageSize, int pageNumber, int fPageNumber, float floatNum, String subfield) {

        Map<String, Object> resultMap = new HashMap<String, Object>();

        List<Quantization> newsInfos = null;
        List<Quantization> fnewsInfos = null;

        int totalPageNum = 0;
        int ftotalPageNum = 0;

        List<Quantization> allInfo = getQuantizationFromRedis(days, floatNum);

        newsInfos = getQuantizationToPage(getZFQuantizations(allInfo, subfield, "z"), pageNumber, pageSize);
        totalPageNum = (getZFQuantizations(allInfo, subfield, "z").size() + pageSize - 1) / pageSize;

        fnewsInfos = getQuantizationToPage(getZFQuantizations(allInfo, subfield, "f"), fPageNumber, pageSize);
        ftotalPageNum = (getZFQuantizations(allInfo, subfield, "f").size() + pageSize - 1) / pageSize;

        resultMap.put("nodes", newsInfos);
        resultMap.put("fnodes", fnewsInfos);

        resultMap.put("current_page", pageNumber);
        resultMap.put("fcurrent_page", fPageNumber);

        resultMap.put("total_page", totalPageNum);
        resultMap.put("ftotal_page", ftotalPageNum);

        resultMap.put("total_num", allInfo.size());

        return Response.ok(resultMap);
    }


    /**
     * 从redis中获取所有股票集合并获取对应值
     */
    private List<Quantization> getQuantizationFromRedis(String days, float floatNum) {

        List<Quantization> list = JSON.parseArray(redisUtil.get(env.getProperty("quant.stocklist")).toString(), Quantization.class);
        log.info("-------------------------from redis get data list---" + list + "--size-" + list.size() + "--");
        List<Quantization> newList = new ArrayList<Quantization>();

        if (list != null && list.size() > 0) {
            for (Quantization quant : list) {

                quant.setCorrelation(getCorrelationByCode(quant.getCode()));
                quant.setUpsAndDowns(getUpsAndDownsByCodeAndDays(quant.getCode(), days, floatNum));

                newList.add(quant);
            }
        }

        log.info("------------------------- get data newList---" + newList + "--size-" + newList.size() + "--");
        return newList;
    }

    /**
     * 对List进行分页
     */
    private List<Quantization> getQuantizationToPage(List<Quantization> list, int pageNumber, int pageSize) {

        List<Quantization> quantizations = new ArrayList<Quantization>();

        int currIdx = (pageNumber > 1 ? (pageNumber - 1) * pageSize : 0);

        for (int i = 0; i < pageSize && i < list.size() - currIdx; i++) {
            Quantization q = list.get(currIdx + i);

            Quantization quantization = new Quantization();

            BeanUtils.copyProperties(q, quantization);

            quantizations.add(quantization);
        }
        return quantizations;
    }


    //对List进行正反分类
    //正
    private List<Quantization> getZFQuantizations(List<Quantization> list, String subfield, String zf) {

        List<Quantization> newsInfos = new ArrayList<Quantization>();

        for (Quantization info : list) {
            if (subfield.equals("1")) {
                if (zf.equals("z") && info.getCorrelation() >= 0) {
                    newsInfos.add(info);
                }
                if (zf.equals("f") && info.getCorrelation() < 0) {
                    newsInfos.add(info);
                }
            }
            if (subfield.equals("2")) {
                if (zf.equals("z") && info.getUpsAndDowns() >= 0) {
                    newsInfos.add(info);
                }
                if (zf.equals("f") && info.getUpsAndDowns() < 0) {
                    newsInfos.add(info);
                }
            }
        }

        if (subfield.equals("1")) {
            //sortList.Sort(newsInfos, "getCorrelation", "desc");
            Collections.sort(newsInfos, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    Quantization u1 = (Quantization) o1;
                    Quantization u2 = (Quantization) o2;
                    if (Math.abs(u1.getCorrelation()) < Math.abs(u2.getCorrelation())) {
                        return 1;
                    }
                    if (Math.abs(u1.getCorrelation())> Math.abs(u2.getCorrelation())) {
                        return -1;
                    }
                    return 0;
                }
            });
        }

        if (subfield.equals("2")) {
            // sortList.Sort(newsInfos, "getUpsAndDowns", "desc");
            Collections.sort(newsInfos, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    Quantization u1 = (Quantization) o1;
                    Quantization u2 = (Quantization) o2;
                    if (Math.abs(u1.getUpsAndDowns()) < Math.abs(u2.getUpsAndDowns())) {
                        return 1;
                    }
                    if (Math.abs(u1.getUpsAndDowns()) > Math.abs(u2.getUpsAndDowns())) {
                        return -1;
                    }
                    return 0;
                }
            });
        }

        return newsInfos;
    }

    /**
     * 获取具体相关性
     */
    private float getCorrelationByCode(String code) {
        float correlation = 0;
        correlation = Float.parseFloat(redisUtil.get(code + env.getProperty("quant.correlation")).toString());
        return correlation;
    }

    private float getFloatValue(JSONObject object, float floatNum) {
        float upsAndDowns = 0;
        JSONArray xList = object.getJSONArray("x");
        JSONArray yList = object.getJSONArray("y");
        log.info("------------------------- start--xList size-" + xList.size() + "--yList size-" + yList.size() + "--");
        for (int i = 0; i < xList.size(); i++) {
            float x = xList.getFloat(i);
            if (x == floatNum) {
                upsAndDowns = yList.getFloat(i);
                break;
            }
            if (x > floatNum) {
                if (i == 0) {
                    upsAndDowns = yList.getFloat(i);
                    break;
                }

                float x1, x2, y1, y2;
                log.info("------------------------- start--i-" + i);
                x1 = xList.getFloat(i - 1);
                log.info("------------------------- start--x1-" + x1);
                x2 = x;
                log.info("------------------------- start--x2-" + x2);
                y1 = yList.getFloat(i - 1);
                log.info("------------------------- start--y1-" + y1);
                y2 = yList.getFloat(i);
                log.info("------------------------- start--y2-" + y2);

                upsAndDowns = (((y2 - y1) * (floatNum - x1)) / (x2 - x1)) + y1;

                break;
            }

            if (x < floatNum && i == xList.size() - 1) {
                upsAndDowns = yList.getFloat(i);
                break;
            }
        }
        return upsAndDowns * 100;
    }


    /**
     * 获取具体跌幅数
     * days 0 ：1天， 1 ：多天
     */
    private float getUpsAndDownsByCodeAndDays(String code, String days, float floatNum) {
        float upsAndDowns = 0;

        if (days.equals("0")) {
            JSONObject objectOne = (JSONObject) JSON.parse(redisUtil.get(code + env.getProperty("quant.oneday")).toString());
            upsAndDowns = getFloatValue(objectOne, floatNum);
        }

        if (days.equals("1")) {
            JSONObject objectMulti = (JSONObject) JSON.parse(redisUtil.get(code + env.getProperty("quant.multidays")).toString());
            log.info("------------------------- start--code-" + code + "--");
            upsAndDowns = getFloatValue(objectMulti, floatNum);
        }

        return upsAndDowns;
    }

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private Environment env;

    @Autowired
    private SortList sortList;

    private static Logger log = LoggerFactory.getLogger(QuantizationServiceImpl.class);
}
