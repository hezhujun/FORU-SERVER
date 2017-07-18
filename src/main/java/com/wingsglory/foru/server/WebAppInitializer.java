package com.wingsglory.foru.server;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * Created by hezhujun on 2017/6/1.
 * web初始化类别
 */
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    // ContextLoaderListener创建的应用上下文中的bean
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{RootConfig.class};
    }

    // DispatcherServlet应用上下文中的bean
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{WebConfig.class};
    }

    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
