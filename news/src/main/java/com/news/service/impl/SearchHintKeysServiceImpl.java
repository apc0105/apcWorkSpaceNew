package com.news.service.impl;

import com.alibaba.fastjson.JSON;
import com.news.model.SearchHintKeys;
import com.news.service.SearchHintKeysService;
import com.news.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service("searchHintKeysService")
public class SearchHintKeysServiceImpl implements SearchHintKeysService {

    @Override
    public void saveSearchHintKeys(SearchHintKeys searchHintKeys) {
        mongoTemplate.save(searchHintKeys);
    }

    @Override
    public List<SearchHintKeys> findAll() {
        return mongoTemplate.findAll(SearchHintKeys.class);
    }

    @Override
    public List<SearchHintKeys> findByKey(String keyWords) {
//        log.info("------start findByKey---"+keyWords+"--------");

        List<SearchHintKeys> searchHintKeysList = new ArrayList<SearchHintKeys>();

        List<SearchHintKeys> results = new ArrayList<SearchHintKeys>();

//        log.info("---Redis haskey---"+redisUtil.hasKey("searchHintKeys")+"-----------");

        if (!redisUtil.hasKey("searchHintKeys")) {
//            log.info("---Redis is null---start saveToRedis-----------");
            saveSearchHintKeysToRedis();

        }

        searchHintKeysList = JSON.parseArray((String) redisUtil.get("searchHintKeys"),SearchHintKeys.class);

//        log.info("---Get Data From Redis---------searchHintKeysList size---"+searchHintKeysList.size()+"-------");

        if (StringUtils.isEmpty(keyWords)) {
            return searchHintKeysList;
        }

        for (SearchHintKeys searchHintkey : searchHintKeysList) {
            if (searchHintkey.getName().indexOf(keyWords) > -1) {
                results.add(searchHintkey);
            }
        }

        return results;
    }

    @Scheduled(cron = "59 59 23 ? * *")
    public void saveSearchHintKeysToRedis() {

        if (redisUtil.hasKey("searchHintKeys")) {
            redisUtil.del("searchHintKeys");
        }

        List<SearchHintKeys> searchHintKeysList = findAll();

//        log.info("---before save to Redis ---Get list size----"+searchHintKeysList.size()+"-------");

        if (searchHintKeysList.size() > 0) {

            redisUtil.set("searchHintKeys", JSON.toJSONString(searchHintKeysList));

        }

//        log.info("---Redis save success---end saveToRedis-----------");
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RedisUtil redisUtil;

    private static Logger log = LoggerFactory.getLogger(SearchHintKeysServiceImpl.class);
}
