package com.news.service;

import com.news.model.ApiInfo;

import java.util.List;

/**
 * Created by Administrator on 2018/11/9.
 */
public interface ApiInfoService {

    //保存ApiInfo
    void saveApiInfo(ApiInfo apiInfo);

    //查询集合
    List<ApiInfo> findAll();

    //通过参数查询集合
    List<ApiInfo> findListByParam(ApiInfo apiInfo);

    //查询某字段下值集合
    List<String> findFieldValues(String field);
}
