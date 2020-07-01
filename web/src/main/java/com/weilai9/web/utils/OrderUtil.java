package com.weilai9.web.utils;

import java.util.Calendar;
import java.util.UUID;

public class OrderUtil {


    /**
     * 随机订单号 订单生成时间 时分秒 加12位随机数
     *
     * @return
     */
    public static String setOrderNo() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        String time = hour + "" + minute + "" + second;
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0,12);
        return time+uuid;
    }

}
