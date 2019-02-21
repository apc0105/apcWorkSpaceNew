package com.news.service.impl;

import ai.hual.sentiment.pojo.xsd.Edge;
import ai.hual.sentiment.pojo.xsd.InferenceResult;
import ai.hual.sentiment.pojo.xsd.Node;
import ai.hual.sentiment.pojo.xsd.Route;
import com.alibaba.fastjson.JSON;
import com.news.model.NewsInfo;
import com.news.model.NodeEdge;
import com.news.model.WebNode;
import com.news.service.NewsService;
import com.news.support.Response;
import com.news.util.RedisUtil;
import com.news.util.SortList;

import com.tk.ws.TKNewsServiceClient;
import com.tk.ws.TKNewsServiceInferenceExceptionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.rmi.RemoteException;
import java.util.*;

@Service("newsService")
public class NewsServiceImpl implements NewsService {

    @Override
    public Response findNews(int nDirection, String token, String search_value, int pageSize, int pageNumber, int fPageNumber, float flPriceChng) {

        Map<String, Object> resultMap = new HashMap<String, Object>();

        List<InferenceResult> results = getInferenceResults(nDirection, token, search_value, flPriceChng);

        if (results == null) {
            return Response.fail();
        }

        List<NewsInfo> allInfo = new ArrayList<NewsInfo>();
        for (int i = 0; i < results.size(); i++) {
            NewsInfo newsInfo = new NewsInfo();
            newsInfo.setName(results.get(i).getName());
            newsInfo.setCode(results.get(i).getCode());
            newsInfo.setScore(results.get(i).getScore());
            allInfo.add(newsInfo);
        }

        List<NewsInfo> newsInfos = null;
        List<NewsInfo> fnewsInfos = null;
        int totalPageNum = 0;
        int ftotalPageNum = 0;
        if (ObjectUtils.isEmpty(flPriceChng) || flPriceChng == 0) {
            newsInfos = getNewsInfoToPage(allInfo, pageNumber, pageSize);
            totalPageNum = (results.size() + pageSize - 1) / pageSize;
        } else {
            newsInfos = getNewsInfoToPage(getZNewsInfo(allInfo), pageNumber, pageSize);
            totalPageNum = (getZNewsInfo(allInfo).size() + pageSize - 1) / pageSize;

            fnewsInfos = getNewsInfoToPage(getFNewsInfo(allInfo), fPageNumber, pageSize);
            ftotalPageNum = (getFNewsInfo(allInfo).size() + pageSize - 1) / pageSize;
        }

        resultMap.put("nodes", newsInfos);
        resultMap.put("fnodes", fnewsInfos);

        resultMap.put("current_page", pageNumber);
        resultMap.put("fcurrent_page", fPageNumber);

        resultMap.put("total_page", totalPageNum);
        resultMap.put("ftotal_page", ftotalPageNum);

        resultMap.put("total_num", results.size());
        resultMap.put("token", token);

        return Response.ok(resultMap);
    }

    private List<InferenceResult> getInferenceResults(int nDirection, String token, String search_value, float flPriceChng) {
        List<InferenceResult> results = null;

        String clientUrl = env.getProperty("clientUrl");
        String quantClientUrl = env.getProperty("quantClientUrl");
        int nMaxNum = Integer.parseInt(env.getProperty("nMaxNum"));
        int nMaxDepth = Integer.parseInt(env.getProperty("nMaxDepth"));
        int nOrderType = Integer.parseInt(env.getProperty("nOrderType"));
        int flWeightThreshold = Integer.parseInt(env.getProperty("flWeightThreshold"));

        if (redisUtil.hasKey(token)) {

            results = JSON.parseArray(redisUtil.get(token).toString(), InferenceResult.class);

        } else {
            try {
                if (ObjectUtils.isEmpty(flPriceChng) || flPriceChng == 0) {
                    TKNewsServiceClient client = new TKNewsServiceClient(clientUrl);
                    InferenceResult[] inferenceResults = client.getRelatedCompaniesByKey(search_value, nMaxNum, nMaxDepth, nOrderType, nDirection, flWeightThreshold);

                    if (!ObjectUtils.isEmpty(inferenceResults) && inferenceResults.length > 0) {
                        results = Arrays.asList(inferenceResults);

                        redisUtil.set(token, JSON.toJSONString(results), 3600 * 24);
                    }
                }
          /*      else {
                    TKQuantizationServiceClient quantizationServiceClient = new TKQuantizationServiceClient(quantClientUrl);
                    results = quantizationServiceClient.getRelatedCompaniesByKey(search_value, flPriceChng, nMaxNum, nMaxDepth, nOrderType, nDirection, flWeightThreshold);
                }*/

            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (TKNewsServiceInferenceExceptionException e) {
                e.printStackTrace();
            }
        }

        return results;
    }

    //对List进行分页
    private List<NewsInfo> getNewsInfoToPage(List<NewsInfo> list, int pageNumber, int pageSize) {

        List<NewsInfo> newsInfos = new ArrayList<NewsInfo>();
        int currIdx = (pageNumber > 1 ? (pageNumber - 1) * pageSize : 0);
        for (int i = 0; i < pageSize && i < list.size() - currIdx; i++) {
            NewsInfo ni = list.get(currIdx + i);

            NewsInfo newsInfo = new NewsInfo();

            BeanUtils.copyProperties(ni, newsInfo);

            newsInfos.add(newsInfo);
        }
        return newsInfos;
    }

    //对List进行正反分类
    //正
    private List<NewsInfo> getZNewsInfo(List<NewsInfo> list) {

        List<NewsInfo> newsInfos = new ArrayList<NewsInfo>();
        for (NewsInfo info : list) {
            if (info.getScore() >= 0) {
                newsInfos.add(info);
            }
        }
        sortList.Sort(newsInfos, "getScore", "desc");
        return newsInfos;
    }

    //反
    private List<NewsInfo> getFNewsInfo(List<NewsInfo> list) {

        List<NewsInfo> newsInfos = new ArrayList<NewsInfo>();
        for (NewsInfo info : list) {
            if (info.getScore() < 0) {
                newsInfos.add(info);
            }
        }
        sortList.Sort(newsInfos, "getScore", "desc");
        return newsInfos;
    }


    @Override
    public Response findNewsInfo(String token, String code) {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<WebNode> nodes = new ArrayList<WebNode>();
        List<NodeEdge> edges = new ArrayList<NodeEdge>();

        List<InferenceResult> results = JSON.parseArray(redisUtil.get(token).toString(), InferenceResult.class);

        if (ObjectUtils.isEmpty(results)) {
            return Response.fail();
        }

        for (int i = 0; i < results.size(); i++) {
            if (results.get(i).getCode().equals(code)) {
                for (Route route : results.get(i).getRoutes()) {
                    for (Node node : route.getNodes()) {

                        WebNode webNode = new WebNode();
                        webNode.setKey(node.getName());
                        webNode.setText(node.getName());

                        if (env.getProperty("fillterKeyWords").indexOf(node.getType()) > -1) {
                            webNode.setCategory("User");
                        } else {
                            webNode.setCategory("Supplier");
                        }

                        boolean flag = false;

                        if (!ObjectUtils.isEmpty(nodes) && nodes.size() > 0) {
                            for (WebNode node1 : nodes) {
                                if (node1.getKey().equals(webNode.getKey())) {
                                    flag = true;
                                    break;
                                }
                            }
                        }

                        if (!flag) {
                            nodes.add(webNode);
                        }
                    }
                    for (Edge edge : route.getEdges()) {
                        NodeEdge nodeEdge = new NodeEdge();
                        nodeEdge.setFrom(edge.getSource());
                        nodeEdge.setTo(edge.getTarget());
                        nodeEdge.setText(edge.getRelation());
                        edges.add(nodeEdge);
                    }
                }
            }
        }

        resultMap.put("nodes", nodes);
        resultMap.put("edges", edges);

        return Response.ok(resultMap);
    }

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private Environment env;

    @Autowired
    private SortList sortList;

    private static Logger log = LoggerFactory.getLogger(NewsServiceImpl.class);
}
