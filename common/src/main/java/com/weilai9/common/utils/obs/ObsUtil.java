package com.weilai9.common.utils.obs;

import com.obs.services.ObsClient;
import com.obs.services.model.AccessControlList;
import com.obs.services.model.ObsBucket;
import com.obs.services.model.PutObjectResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;

import static com.weilai9.common.utils.wechat.SysConst.*;

/**
 * 华为云对象存储服务器使用工具类
 * @author xujinhao
 */
public class ObsUtil {

    private   static ObsClient getObsClient(){
        String endPoint = HUAWEIYUN_END_POINT;
        String ak = HUAWEIYUN_AK;
        String sk = HUAWEIYUN_SK;
        // 创建ObsClient实例
        ObsClient obsClient = new ObsClient(ak, sk, endPoint);
        return obsClient;
    }

    /**
     *  上传文件，返回url
     * @param fileName 文件名
     * @param file 文件
     * @return
     */
    public static String  uploadFile(String fileName, MultipartFile file) {
        try {
            ObsClient obsClient = getObsClient();
            // 使用访问OBS
            PutObjectResult putObjectResult = obsClient.putObject("image-space-weilai9", fileName, new ByteArrayInputStream(file.getBytes()));
            String objectUrl = putObjectResult.getObjectUrl();
            // 关闭obsClient
            obsClient.close();
            return  objectUrl;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
