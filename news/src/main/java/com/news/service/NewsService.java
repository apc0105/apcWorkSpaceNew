package com.news.service;

import com.news.support.Response;

public interface NewsService {

    Response findNews(int nDirection, String token, String search_value, int pageSize, int pageNumber,int fPageNumber,float flPriceChng);

    Response findNewsInfo(String token, String code);

}
