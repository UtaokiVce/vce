package com.weilai9.common.config.qiniu;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 七牛配置
 *
 * @author china.fuyao@outlook.com
 * @date 2020-04-29 14:30
 */
@Configuration
public class QiNiuConfig {
    public static String accessKey;
    public static String secretKey;
    public static String hubName;
    public static String publishDomain;
    public static String liveDomain;

    @Value("${qiniu.video.live.accessKey}")
    private void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    @Value("${qiniu.video.live.secretKey}")
    private void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    @Value("${qiniu.video.live.hubName}")
    private void setHubName(String hubName) {
        this.hubName = hubName;
    }

    @Value("${qiniu.video.live.publishDomain}")
    private void setPublishDomain(String publishDomain) {
        this.publishDomain = publishDomain;
    }

    @Value("${qiniu.video.live.liveDomain}")
    private void setLiveDomain(String liveDomain) {
        this.liveDomain = liveDomain;
    }
}
