package com.weilai9.web.controller.rabbitmq.customer;

import com.weilai9.common.utils.wechat.HuaweiSmsUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 监听类
 */
@Component
@RabbitListener(queues = "SmsDirectQueue")//监听的队列名称 TestDirectQueue
public class SmsDirectReceiver {

    @RabbitHandler
    public void process(Map map) {

        String phoneNum = map.get("phoneNum").toString();
        String content = map.get("content").toString();
        try {
            HuaweiSmsUtil.sendEms(phoneNum,content);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

