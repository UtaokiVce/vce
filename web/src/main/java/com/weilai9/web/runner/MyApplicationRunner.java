package com.weilai9.web.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@Order(1)
public class MyApplicationRunner implements ApplicationRunner {
    @Resource
    InterfaceAndRoleUtil interfaceAndRoleUtil;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("初始化更新接口");
        interfaceAndRoleUtil.getInterfaceInfo();
        log.info("缓存接口");
        interfaceAndRoleUtil.redisUrl();
        log.info("缓存角色");
        interfaceAndRoleUtil.redisRole();
        log.info("初始化超级管理员接口权限");
        interfaceAndRoleUtil.initIf();
    }
}
