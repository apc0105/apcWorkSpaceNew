package com.tk.eventanalysis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.WebApplicationInitializer;

@SpringBootApplication(scanBasePackages = "com.tk.eventanalysis")
class EventAnalysisApplication extends SpringBootServletInitializer implements WebApplicationInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(EventAnalysisApplication.class);
    }
    public static void main(String[] args) {
        SpringApplication.run(EventAnalysisApplication.class, args);
    }
}
