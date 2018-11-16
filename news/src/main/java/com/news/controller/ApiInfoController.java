package com.news.controller;

import com.news.model.ApiInfo;
import com.news.service.ApiInfoService;
import com.news.support.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/11/9.
 */
@Controller
@RequestMapping(value = "/apiInfo")
public class ApiInfoController {

    /**
     * 获取包类型集合
     */
    @ResponseBody
    @PostMapping(value = "/getPackageTypes")
    public Response getPackageTypes() {

        List<String> list = apiInfoService.findFieldValues("packageType");

        return Response.ok(list);
    }

    /**
     * 通过包类型查询ApiInfo集合
     */
    @ResponseBody
    @PostMapping(value = "/getApiInfos")
    public Response getApiInfos(String packageType) {

        Map<String, Object> result = new HashMap<String, Object>();

        ApiInfo info = new ApiInfo();

        info.setPackageType(packageType);
        //主包
        info.setDependencyPackage(false);
        List<ApiInfo> list = apiInfoService.findListByParam(info);
        result.put("mainPackages", list);

        //依赖包
        info.setDependencyPackage(true);
        List<ApiInfo> Dlist = apiInfoService.findListByParam(info);
        result.put("dependencyPackages", Dlist);

        return Response.ok(result);
    }

    @Autowired
    private ApiInfoService apiInfoService;
}
