package com.news.service;

import com.news.support.Response;

public interface QuantizationService {
    Response findQuantizations(String days, int pageSize, int pageNumber, int fPageNumber, float floatNum,String subfield);
}
