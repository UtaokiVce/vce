package test;


import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @description: 天气定时任务
 * @author: Mr.Q
 * @create: 2019-05-30 17:20
 **/
@Configuration
@EnableScheduling
public class Timer {

    public static void main(String[] args) {
        Runnable runnable = new Runnable() {
            public void run() {
                // 把run方法里的内容换成你要执行的内容
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                System.out.println("当前的系统时间为：" + sdf.format(new Date()));
            }
        };
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        //public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit);
        //command--执行的任务,initialDelay--延迟开始,period--间隔时间,unit--时间单位
        service.scheduleAtFixedRate(runnable, 0, 5, TimeUnit.SECONDS);
    }
}
