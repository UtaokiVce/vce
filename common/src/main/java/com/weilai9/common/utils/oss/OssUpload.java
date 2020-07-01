package com.weilai9.common.utils.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * 阿里云对象存储服务器使用工具类
 * @author v
 */
public class OssUpload {

    private static String endpoint = "oss-cn-beijing.aliyuncs.com";
    private static  String accessKeyId = "LTAI4GKdPHRV9ytVQfpbYS1w";
    private  static String accessKeySecret = "WNLK77XTh2Kb19D8jB2vSLbzJwr0tq";
    private  static String bucketName = "weilai9";
    private  static String folder = "xqsh/";


    public static String  uploadFile(MultipartFile file) {

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        //上传文件名
        String filename = file.getOriginalFilename();
        filename = System.currentTimeMillis()+filename;

        PutObjectRequest putObjectRequest = null;

        // 添加 ContentType
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType("image/jpg");
        try {
            putObjectRequest = new PutObjectRequest(bucketName, folder+filename, new ByteArrayInputStream(file.getBytes()),objectMetadata);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ossClient.putObject(putObjectRequest);
        ossClient.shutdown();

        return bucketName+"."+endpoint+"/"+folder+filename;
    }
}
