package com.news.service.impl;

import com.news.model.ApiInfo;
import com.news.service.ApiInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/11/9.
 */
@Service
public class ApiInfoServiceImpl implements ApiInfoService {

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public void saveApiInfo(ApiInfo apiInfo) {
        mongoTemplate.save(apiInfo);
    }

    @Override
    public List<ApiInfo> findAll() {
        return mongoTemplate.findAll(ApiInfo.class);
    }

    @Override
    public List<ApiInfo> findListByParam(ApiInfo apiInfo) {
        Query query = new Query();

        Criteria criteria = Criteria.where("packageType").is(apiInfo.getPackageType());

        if (!StringUtils.isEmpty(apiInfo.getPackageName())) {
            criteria.and("packageName").is(apiInfo.getPackageName());
        }
        if (!StringUtils.isEmpty(apiInfo.getPackageRelativeAddress())) {
            criteria.and("packageRelativeAddress").is(apiInfo.getPackageRelativeAddress());
        }
        if (!StringUtils.isEmpty(apiInfo.getPackageVersion())) {
            criteria.and("packageVersion").is(apiInfo.getPackageVersion());
        }
        if (!StringUtils.isEmpty(apiInfo.getDocName())) {
            criteria.and("docName").is(apiInfo.getDocName());
        }
        if (!StringUtils.isEmpty(apiInfo.getDocRelativeAddress())) {
            criteria.and("docRelativeAddress").is(apiInfo.getDocRelativeAddress());
        }
        if (!ObjectUtils.isEmpty(apiInfo.isDependencyPackage())) {
            criteria.and("isDependencyPackage").is(apiInfo.isDependencyPackage());
        }
        query.addCriteria(criteria);

        return mongoTemplate.find(query, ApiInfo.class);
    }

    @Override
    public List<String> findFieldValues(String field) {
        return mongoTemplate.findDistinct(field, ApiInfo.class, String.class);
    }
}
