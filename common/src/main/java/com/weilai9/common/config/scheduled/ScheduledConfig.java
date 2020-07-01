package com.weilai9.common.config.scheduled;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import static java.util.concurrent.Executors.newScheduledThreadPool;

/**
 * @author ：china.fuyao@outlook.com
 * @date ：Created in 2019-07-24 10:18:04
 * @description： 定时器配置
 */
@Configuration
@ConfigurationProperties(prefix = "scheduled")
public class ScheduledConfig implements SchedulingConfigurer {

    private Integer poolSize;

    public Integer getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(Integer poolSize) {
        this.poolSize = poolSize;
    }

    /**
     * 所有的定时任务都放在一个线程池中，定时任务启动时使用不同都线程。
     *
     * @param taskRegistrar
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        //设定一个指定长度的定时任务线程池
        taskRegistrar.setScheduler(newScheduledThreadPool(poolSize));
    }


}