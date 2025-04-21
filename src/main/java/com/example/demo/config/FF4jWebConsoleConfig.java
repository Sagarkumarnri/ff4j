package com.example.demo.config;

import org.ff4j.FF4j;
import org.ff4j.web.FF4jDispatcherServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FF4jWebConsoleConfig {

    @Bean
    public FF4jDispatcherServlet ff4jDispatcherServlet(FF4j ff4j) {
        FF4jDispatcherServlet servlet = new FF4jDispatcherServlet();
        servlet.setFf4j(ff4j);
        return servlet;
    }

    @Bean
    public ServletRegistrationBean<FF4jDispatcherServlet> ff4jServletRegistration(FF4jDispatcherServlet servlet) {
        return new ServletRegistrationBean<>(servlet, "/ff4j-web-console/*");
    }
}
