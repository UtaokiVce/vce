package com.weilai9.common.config.aliyun.oss;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OSSConfig {
    //OSS
    public static String bucket;
    public static String endpoint;
    public static String accessKeyId;
    public static String accessKeySecret;
    public static Integer expireTime;
    //STS
    public static String roleArn;
    public static Long stsTokenExpireTime;

    @Value("${aliyun.oss.bucket}")
    private void setBucket(String bucket) {
        OSSConfig.bucket = bucket;
    }

    @Value("${aliyun.oss.endpoint}")
    private void setEndpoint(String endpoint) {
        OSSConfig.endpoint = endpoint;
    }

    @Value("${aliyun.oss.accessKeyId}")
    private void setAccessKeyId(String accessKeyId) {
        OSSConfig.accessKeyId = accessKeyId;
    }

    @Value("${aliyun.oss.accessKeySecret}")
    private void setAccessKeySecret(String accessKeySecret) {
        OSSConfig.accessKeySecret = accessKeySecret;
    }

    @Value("${aliyun.oss.expireTime}")
    private void setExpireTime(Integer expireTime) {
        OSSConfig.expireTime = expireTime;
    }

    @Value("${aliyun.oss.sts.roleArn}")
    private void setRoleArn(String roleArn) {
        OSSConfig.roleArn = roleArn;
    }

    @Value("${aliyun.oss.sts.stsTokenExpireTime}")
    private void setStsTokenExpireTime(Long stsTokenExpireTime) {
        OSSConfig.stsTokenExpireTime = stsTokenExpireTime;
    }
}
