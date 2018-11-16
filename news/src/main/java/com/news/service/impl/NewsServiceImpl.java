package com.news.service.impl;

import ai.hual.sentiment.pojo.xsd.InferenceResult;
import com.news.model.NewsInfo;
import com.news.service.NewsService;
import com.news.support.Response;
import com.news.util.RedisUtil;
import com.tk.ws.TKNewsServiceClient;
import com.tk.ws.TKNewsServiceInferenceExceptionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.util.*;

@Service
public class NewsServiceImpl implements NewsService {

    @Override
    public Response findNews(int nDirection, String token, String search_value, int pageNumber) {

        Map<String, Object> resultMap = new HashMap<String, Object>();

        InferenceResult[] results = getInferenceResults(nDirection, token, search_value);
        if (results == null) {
            return Response.fail();
        }
        int pageSize = 10;
        List<NewsInfo> newsInfos = new ArrayList<NewsInfo>();
        int currIdx = (pageNumber > 1 ? (pageNumber - 1) * pageSize : 0);
        for (int i = 0; i < pageSize && i < results.length - currIdx; i++) {
            NewsInfo newsInfo = new NewsInfo();
            newsInfo.setName(results[currIdx + i].getName());
            newsInfo.setCode(results[currIdx + i].getCode());
            newsInfo.setScore(results[currIdx + i].getScore());
            newsInfos.add(newsInfo);
        }

        int totalPageNum = (results.length + pageSize - 1) / pageSize;

        resultMap.put("nodes", newsInfos);
        resultMap.put("current_page", pageNumber);
        resultMap.put("total_page", totalPageNum);
        resultMap.put("total_num", results.length);
        resultMap.put("token", token);

        return Response.ok(resultMap);
    }

    private InferenceResult[] getInferenceResults(int nDirection, String token, String search_value) {
        InferenceResult[] results = null;

        if (redisUtil.hasKey(token)) {
            results = (InferenceResult[]) redisUtil.get(token);
        } else {
            try {
                TKNewsServiceClient client = new TKNewsServiceClient();
                results = client.getRelatedCompaniesByKey(search_value, 0, 0, 0, nDirection);//UP
                redisUtil.set(token, results, 1800);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (TKNewsServiceInferenceExceptionException e) {
                e.printStackTrace();
            }
        }

        List<InferenceResult> resultList = new ArrayList<InferenceResult>(results.length);
        Collections.addAll(resultList, results);

        return results;
    }



    @Override
    public Response findNewsInfo(String token, String code) {
        return null;
    }


    @Autowired
    private RedisUtil redisUtil;
}
