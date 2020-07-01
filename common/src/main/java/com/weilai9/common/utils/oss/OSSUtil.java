package com.weilai9.common.utils.oss;

import cn.hutool.core.util.CharsetUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.weilai9.common.config.aliyun.oss.OSSConfig;
import com.weilai9.common.constant.Result;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * OSS
 */
@Component
public class OSSUtil {

    private static OSS client = new OSSClientBuilder().build(OSSConfig.endpoint, OSSConfig.accessKeyId, OSSConfig.accessKeySecret);

    public static Result getUploadImgPolicy() {
        // oss上传目录
        String imgDir = "img/" + new SimpleDateFormat("yyyy-MM").format(new Date());
        String host = "https://" + OSSConfig.bucket + "." + OSSConfig.endpoint;
        try {
            long expireEndTime = System.currentTimeMillis() + OSSConfig.expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConditions = new PolicyConditions();
            policyConditions.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConditions.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, imgDir);

            String postPolicy = client.generatePostPolicy(expiration, policyConditions);
            byte[] binaryData = postPolicy.getBytes(CharsetUtil.UTF_8);
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = client.calculatePostSignature(postPolicy);

            Map<String, String> respMap = new LinkedHashMap<String, String>();
            respMap.put("accessKeyId", OSSConfig.accessKeyId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            String pattern = "yyyy-MM-dd'T'HH:mm:ss:SSSZZ";
            respMap.put("expire", DateFormatUtils.format(expiration, pattern));
            respMap.put("dir", imgDir);
            respMap.put("host", host);
            respMap.put("filename", System.currentTimeMillis() + "");
            respMap.put("expire", String.valueOf(expireEndTime / 1000));

            return new Result(respMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.Error("获取失败");
    }

    /**
     * 获取 sts oss token
     *
     * @return
     */
    public static Result getOSSTokenBySTS() {

        // RoleSessionName 是临时Token的会话名称，自己指定用于标识你的用户，主要用于审计，或者用于区分Token颁发给谁
        // 但是注意RoleSessionName的长度和规则，不要有空格，只能有'-' '_' 字母和数字等字符
        // 具体规则请参考API文档中的格式要求
        String roleSessionName = System.currentTimeMillis() + "";
        // 若policy为空，则用户将获得该角色下所有权限
        String policy = "{\"Statement\":[{\"Action\":[\"oss:*\"],\"Effect\":\"Allow\",\"Resource\":[\"acs:oss:*:*:*\"]}],\"Version\":\"1\"}";

        try {
            // 添加endpoint（直接使用STS endpoint，前两个参数留空，无需添加region ID）
            DefaultProfile.addEndpoint("", "", OSSConfig.endpoint);
            // 构造default profile（参数留空，无需添加region ID）
            IClientProfile profile = DefaultProfile.getProfile("", OSSConfig.accessKeyId, OSSConfig.accessKeySecret);
            // 用profile构造client
            DefaultAcsClient client = new DefaultAcsClient(profile);
            final AssumeRoleRequest request = new AssumeRoleRequest();
            request.setSysMethod(MethodType.POST);
            request.setRoleArn(OSSConfig.roleArn);
            request.setRoleSessionName(roleSessionName);
            request.setPolicy(policy);
            // 设置凭证有效时间
            request.setDurationSeconds(OSSConfig.stsTokenExpireTime);
            final AssumeRoleResponse response = client.getAcsResponse(request);
            Map<String, String> map = new HashMap<>(6);
            map.put("Expiration", response.getCredentials().getExpiration());
            map.put("Access Key Id", response.getCredentials().getAccessKeyId());
            map.put("Access Key Secret", response.getCredentials().getAccessKeySecret());
            map.put("Security Token", response.getCredentials().getSecurityToken());
            map.put("RequestId", response.getRequestId());
            return new Result(map);
        } catch (ClientException e) {
            return Result.Error(e.getErrMsg());
        }
    }

    /**
     * 通过sts临时token上传图片
     *
     * @param accessKeyId
     * @param accessKeySecret
     * @param securityToken
     * @param file
     * @return
     */
    public static Result uploadImgBySTS(String accessKeyId, String accessKeySecret, String securityToken, File file) {
        OSS ossClient = new OSSClientBuilder().build(OSSConfig.endpoint, accessKeyId, accessKeySecret, securityToken);
        String key = "";
        PutObjectRequest putObjectRequest = new PutObjectRequest(OSSConfig.bucket, key, file);
        PutObjectResult result = ossClient.putObject(putObjectRequest);
        int code = result.getResponse().getStatusCode();
        if ("200".equals(code)) {
            return Result.OK();
        }
        return Result.Error("上传失败");
    }

    /**
     * 服务器上传图片
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static String uploadImg(InputStream file) throws Exception {
        String fileName = UpFileNameType.buildFileName(UpFileNameType.TIMESTAMP, "1.png");
        StringBuilder imgDir = new StringBuilder("img/");
        String dir = new SimpleDateFormat("yyyy-MM").format(new Date());
        imgDir.append(dir).append("/");
        imgDir.append(fileName);
        client.putObject(OSSConfig.bucket, imgDir.toString(), file);
        return imgDir.toString();
    }


    /**
     * 上传文件命名类型
     */
    public enum UpFileNameType {
        LOCAL(1, "本地命名"),
        RANDOM(2, "随机命名"),
        TIMESTAMP(3, "时间戳命名"),
        UUID_TIMESTAMP(4, "UUID-时间戳命名");

        private int value;
        private String text;

        private UpFileNameType(int value, String text) {
            this.value = value;
            this.text = text;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        /**
         * 根据上传文件命名类型生成文件名
         *
         * @param type
         * @param source
         * @return
         */
        public static String buildFileName(UpFileNameType type, String source) {
            String fileName = source;
            String fileExt = null;
            if (StringUtils.isNotBlank(source)) {
                fileExt = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
            }
            switch (type) {
                case LOCAL:
                    return fileName;
                case RANDOM:
                    int len = 10;
                    String chars = "ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678";
                    int maxPos = chars.length();
                    for (int i = 0; i < len; i++) {
                        fileName += chars.charAt((int) Math.floor(Math.random()
                                * maxPos));
                    }
                    break;
                case TIMESTAMP:
                    fileName = String.valueOf(System.currentTimeMillis());
                    break;
                case UUID_TIMESTAMP:
                    fileName = UUID.randomUUID().toString().replace("-", "")
                            .toLowerCase()
                            + String.valueOf(System.currentTimeMillis());
                    break;
                default:
                    return fileName;
            }
            fileName += fileExt;
            return fileName;
        }

    }

}
