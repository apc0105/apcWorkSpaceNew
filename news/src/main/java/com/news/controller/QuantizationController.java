package com.news.controller;

import com.news.service.QuantizationService;
import com.news.service.impl.QuantizationServiceImpl;
import com.news.support.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    private static Logger log = LoggerFactory.getLogger(QuantizationController.class);
}
