package com.wingsglory.foru.server;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by hezhujun on 2017/6/1.
 * 应用上下文
 */
@Configuration
@ComponentScan(basePackages = "com.wingsglory.foru.server",
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = EnableWebMvc.class)})
@ImportResource(locations = {"classpath:spring-mybatis.xml"})
@EnableScheduling
public class RootConfig {

}
