package com.news.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * Create by itw_wangpj on 2018/5/11
 */
@Configuration
public class AxiosInterceptorConfig extends WebMvcConfigurationSupport {
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        //
        InterceptorRegistration ir = registry.addInterceptor(axiosInterceptor);
        //配置需要拦截的路径：
        ir.addPathPatterns("/**");

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/resources/**").addResourceLocations("file:" + env.getProperty("filepath.readpath"),"classpath:/static/");

        super.addResourceHandlers(registry);
    }
    @Autowired
    private Environment env;
    @Autowired
    private AxiosInterceptor axiosInterceptor;

}
