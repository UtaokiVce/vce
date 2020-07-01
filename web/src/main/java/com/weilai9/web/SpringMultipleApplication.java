package com.weilai9.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAsync
@EnableScheduling
@EnableCaching
@EnableTransactionManagement
@SpringBootApplication(scanBasePackages = "com.weilai9")
//@MapperScan(basePackages = "com.weilai9.dao.mapper")
public class SpringMultipleApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(SpringMultipleApplication.class, args);
    }
}
