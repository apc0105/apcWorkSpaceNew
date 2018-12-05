package com.news.controller;

import com.news.model.SearchHintKeys;
import com.news.service.NewsService;
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
@RequestMapping(value = "/TKNewsServlet")
public class NewsServiceController {

    /**
     * 获取news集合
     */
    @ResponseBody
    @PostMapping(value = "/SmartSearch")
    public Response getSmartSearch(HttpServletRequest request, int nDirection, String token, String search_value,float flPriceChng) {

        if (StringUtils.isEmpty(token)) {
            token = "NWS_" + request.getRemoteHost() + UUID.randomUUID().toString();
        }

        return newsService.findNews(nDirection, token, search_value, 1,flPriceChng);

    }

    /**a
     * 获取news集合分页查询
     */
    @ResponseBody
    @PostMapping(value = "/SmartSearchPage")
    public Response getSmartSearchPage(int nDirection,String token,int pageNumber,float flPriceChng) {

        return newsService.findNews(nDirection,token,"", pageNumber,flPriceChng);

    }

    /**
     * 获取news详情
     */
    @ResponseBody
    @PostMapping(value = "/SmartSearchEdge")
    public Response getSmartSearchEdge(String token, String code) {

        return newsService.findNewsInfo(token, code);

    }

    /**
     * 获取关键字联想查询
     */
    @ResponseBody
    @PostMapping(value = "/searchHintKeys")
    public Response getSearchHintKeys(String keyWords){

        List<SearchHintKeys> searchHintKeys=searchHintKeysService.findByKey(keyWords);

        return Response.ok(searchHintKeys);
    }

    @Autowired
    private NewsService newsService;

    @Autowired
    private SearchHintKeysService searchHintKeysService;
}
