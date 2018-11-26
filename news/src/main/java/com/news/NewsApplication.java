package com.news;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.WebApplicationInitializer;

@SpringBootApplication(scanBasePackages = { "com.news","ai.hual.sentiment","com.tk.ws"})
@EnableCaching
public class NewsApplication extends SpringBootServletInitializer implements WebApplicationInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(NewsApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(NewsApplication.class, args);
    }

}
