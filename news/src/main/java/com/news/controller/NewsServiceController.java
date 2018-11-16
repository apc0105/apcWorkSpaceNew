package com.news.controller;

import com.news.service.NewsService;
import com.news.support.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
@RequestMapping(value = "/TKNewsServlet")
public class NewsServiceController {

    /**
     * 获取news集合
     */
    @ResponseBody
    @PostMapping(value = "/SmartSearch")
    public Response getSmartSearch(HttpServletRequest request, int nDirection, String token, String search_value) {

        if (StringUtils.isEmpty(token)) {
            token = "NWS_" + request.getRemoteHost() + UUID.randomUUID().toString();
        }

        return newsService.findNews(nDirection, token, search_value, 1);

    }

    /**
     * 获取news集合分页查询
     */
    @ResponseBody
    @PostMapping(value = "/SmartSearchPage")
    public Response getSmartSearchPage(int nDirection,String token,int pageNumber) {

        return newsService.findNews(nDirection,token,"", pageNumber);

    }

    /**
     * 获取news详情
     */
    @ResponseBody
    @PostMapping(value = "/SmartSearchEdge")
    public Response getSmartSearchEdge(String token, String code) {

        return newsService.findNewsInfo(token, code);

    }

    @Autowired
    private NewsService newsService;
}
