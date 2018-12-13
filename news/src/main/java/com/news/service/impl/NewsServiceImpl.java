package com.news.service.impl;

import ai.hual.sentiment.pojo.xsd.Edge;
import ai.hual.sentiment.pojo.xsd.InferenceResult;
import ai.hual.sentiment.pojo.xsd.Node;
import ai.hual.sentiment.pojo.xsd.Route;
import com.news.model.NewsInfo;
import com.news.model.NodeEdge;
import com.news.model.WebNode;
import com.news.service.NewsService;
import com.news.support.Response;
import com.news.util.RedisUtil;
import com.tk.quantization.TKQuantizationServiceClient;
import com.tk.quantization.TKQuantizationServiceInferenceExceptionException;
import com.tk.ws.TKNewsServiceClient;
import com.tk.ws.TKNewsServiceInferenceExceptionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.rmi.RemoteException;
import java.util.*;

@Service("newsService")
public class NewsServiceImpl implements NewsService {

    @Override
    public Response findNews(int nDirection, String token, String search_value, int pageNumber,float flPriceChng) {

        Map<String, Object> resultMap = new HashMap<String, Object>();

        InferenceResult[] results = getInferenceResults(nDirection, token, search_value, flPriceChng);
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

    private InferenceResult[] getInferenceResults(int nDirection, String token, String search_value,float flPriceChng) {
        InferenceResult[] results = null;

        String clientUrl = env.getProperty("clientUrl");
        String quantClientUrl = env.getProperty("quantClientUrl");
        int nMaxNum = Integer.parseInt(env.getProperty("nMaxNum"));
        int nMaxDepth = Integer.parseInt(env.getProperty("nMaxDepth"));
        int nOrderType = Integer.parseInt(env.getProperty("nOrderType"));
        int flWeightThreshold = Integer.parseInt(env.getProperty("flWeightThreshold"));

        if (redisUtil.hasKey(token)) {
            results = (InferenceResult[]) redisUtil.get(token);
        } else {
            try {
                if(ObjectUtils.isEmpty(flPriceChng)||flPriceChng==0){
                    TKNewsServiceClient client = new TKNewsServiceClient(clientUrl);
                    results = client.getRelatedCompaniesByKey(search_value, nMaxNum, nMaxDepth, nOrderType, nDirection, flWeightThreshold);
                } else {
                    TKQuantizationServiceClient quantizationServiceClient = new TKQuantizationServiceClient(quantClientUrl);
                    results = quantizationServiceClient.getRelatedCompaniesByKey(search_value, flPriceChng, nMaxNum, nMaxDepth, nOrderType, nDirection, flWeightThreshold);
                }

                redisUtil.set(token, results, 1800);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (TKNewsServiceInferenceExceptionException e) {
                e.printStackTrace();
            } catch (TKQuantizationServiceInferenceExceptionException e) {
                e.printStackTrace();
            }
        }

        return results;
    }


    @Override
    public Response findNewsInfo(String token, String code) {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<WebNode> nodes = new ArrayList<WebNode>();
        List<NodeEdge> edges = new ArrayList<NodeEdge>();

        InferenceResult[] results = (InferenceResult[]) redisUtil.get(token);

        if (ObjectUtils.isEmpty(results)) {
            return Response.fail();
        }

        for (int i = 0; i < results.length; i++) {
            if (results[i].getCode().equals(code)) {
                for (Route route : results[i].getRoutes()) {
                    for (Node node : route.getNodes()) {

                        WebNode webNode = new WebNode();
                        webNode.setKey(node.getName());
                        webNode.setText(node.getName());

                        boolean flag=false;

                        if(!ObjectUtils.isEmpty(nodes)&&nodes.size()>0){
                            for(WebNode node1:nodes){
                                if(node1.getKey().equals(webNode.getKey())){
                                    flag=true;
                                    break;
                                }
                            }
                        }

                        if(!flag){
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
}
