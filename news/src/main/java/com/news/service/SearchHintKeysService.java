package com.news.service;

import com.news.model.SearchHintKeys;

import java.util.List;

public interface SearchHintKeysService {

    void saveSearchHintKeys(SearchHintKeys searchHintKeys);

    List<SearchHintKeys> findAll();

    List<SearchHintKeys> findByKey(String keyWords);

}
