package com.weilai9.common.utils.wechat;

import com.weilai9.dao.entity.Customerqinzuan;
import com.weilai9.dao.entity.Customerrelation;
import com.weilai9.dao.mapper.CustomerqinzuanMapper;
import com.weilai9.dao.mapper.CustomerrelationMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @description: 天气定时任务
 * @author: Mr.Q
 * @create: 2019-05-30 17:20
 **/
@Configuration
@EnableScheduling
public class QzTask {
    @Resource
    CustomerqinzuanMapper customerqinzuanMapper;
    @Resource
    CustomerrelationMapper customerrelationMapper;

    /**
     * 定时任务：更新亲钻数据
     */
//    @Scheduled(cron = "0/30 * * * * ? ")
//    private void weatherTasks() {
//        customerqinzuanMapper.allProfit();
//        customerrelationMapper.updateIntegralByMyself();
//        customerrelationMapper.updateIntegralByTeam();
//        System.out.println("亲钻定时任务测试，执行时间："+ LocalDateTime.now());
//    }

    /**
     * todo 定时任务：清空每日数据
     */
//    @Scheduled(cron = "0/30 * * * * ? ")
//    private void weatherTasks() {
//        customerqinzuanMapper.allProfit();
//        customerrelationMapper.updateIntegralByMyself();
//        customerrelationMapper.updateIntegralByTeam();
//        System.out.println("亲钻定时任务测试，执行时间："+ LocalDateTime.now());
//    }

    /**
     * todo 定时任务：月记录
     */
//    @Scheduled(cron = "0/30 * * * * ? ")
//    private void weatherTasks() {
//        customerqinzuanMapper.allProfit();
//        customerrelationMapper.updateIntegralByMyself();
//        customerrelationMapper.updateIntegralByTeam();
//        System.out.println("亲钻定时任务测试，执行时间："+ LocalDateTime.now());
//    }

}
