package com.news.controller;

import com.news.model.SearchHintKeys;
import com.news.service.NewsService;
import com.news.service.QuantizationService;
import com.news.service.SearchHintKeysService;
import com.news.support.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping(value = "/TKQuantization")
public class QuantizationController {


    /**a
     * 获取集合分页查询
     */
    @ResponseBody
    @PostMapping(value = "/getQuantPage")
    public Response getQuantPage(String days,int pageSize,int pageNumber,int fPageNumber,float floatNum,String subfield) {

        return quantizationService.findQuantizations(days,pageSize, pageNumber,fPageNumber,floatNum,subfield);

    }


    @Autowired
    private QuantizationService quantizationService;
}
