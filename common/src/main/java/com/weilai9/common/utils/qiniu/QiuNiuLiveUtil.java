package com.weilai9.common.utils.qiniu;


import cn.hutool.json.JSONUtil;
import com.qiniu.pili.*;
import com.weilai9.common.config.qiniu.QiNiuConfig;
import com.weilai9.common.constant.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 七牛直播
 *
 * @author china.fuyao@outlook.com
 * @date 2020-04-29 13:48
 */
@Slf4j
@Component
public class QiuNiuLiveUtil {

    /**
     * 获取hub对象
     *
     * @return
     */
    //private Hub hub = new Client(QiNiuConfig.accessKey, QiNiuConfig.secretKey).newHub(QiNiuConfig.hubName);

    private static Hub getHub() {
        return new Client(QiNiuConfig.accessKey, QiNiuConfig.secretKey).newHub(QiNiuConfig.hubName);
    }

    /**
     * 通过类型获取直播名称前缀
     *
     * @param liveType
     * @return
     */
    private static String prefix(Integer liveType) {
        //TODO 业务类别，不同前缀
        return "";
    }

    /**
     * 创建流
     *
     * @param customerId
     * @param liveType
     * @return
     */
    public Result createLiveStream(Integer customerId, Integer liveType) {
        try {
            String prefix = prefix(liveType);
            String streamName = prefix + System.currentTimeMillis() + "_" + customerId;
            Stream stream = getHub().create(streamName);
            System.out.println(JSONUtil.toJsonStr(stream));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.Error("创建失败");
    }

    /**
     * 获取流
     *
     * @param streamName
     * @return
     */
    public Result getLiveStream(String streamName) {
        try {
            Stream stream = getHub().get(streamName);
            System.out.println(JSONUtil.toJsonStr(stream));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.Error("创建失败");
    }

    /**
     * 启用流
     *
     * @param streamName
     * @return
     */
    public Result enableStream(String streamName) {
        try {
            Stream stream = getHub().get(streamName);
            stream.enable();
            stream = getHub().get(streamName);
            System.out.println(JSONUtil.toJsonStr(stream));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.Error("创建失败");
    }

    /**
     * 禁用流
     *
     * @param streamName
     * @return
     */
    public Result disableStream(String streamName) {
        try {
            Stream stream = getHub().get(streamName);
            stream.disable();
            stream = getHub().get(streamName);
            System.out.println(JSONUtil.toJsonStr(stream));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.Error("创建失败");
    }

    /**
     * 获取流状态
     *
     * @param streamName
     * @return
     */
    public Stream.LiveStatus liveStatus(String streamName) {
        Stream stream;
        try {
             stream = getHub().get(streamName);
            Stream.LiveStatus status = stream.liveStatus();
            return status;
        } catch (Exception e) {
            stream = null;
            log.info("离线状态：{}", streamName);
        }
        return null;
    }

    /**
     * 批量获取流状态
     *
     * @param streamNames
     * @return
     */
    public Hub.BatchLiveStatus[] batchLiveStatus(String[] streamNames) {
        try {
            Hub.BatchLiveStatus[] batchLiveStatus = getHub().batchLiveStatus(streamNames);
            return batchLiveStatus;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取在线推流列表
     *
     * @param liveType
     * @param size
     * @param marker
     * @return
     */
    public Result getLiveListOnLine(Integer liveType, Integer size, String marker) {
        try {
            String prefix = prefix(liveType);
            Hub.ListRet listRet = getHub().listLive(prefix, size, marker);
            Map<String, Object> data = new HashMap<>(2);
            data.put("marker", listRet.omarker);
            List<Map<String, String>> list = new ArrayList<>(listRet.keys.length);
            for (String key : listRet.keys) {
                Map<String, String> map = new HashMap<>(2);
                map.put("streamName", key);
                map.put("streamStatus", "1");
                list.add(map);
            }
            data.put("list", list);
            return new Result(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取推流列表
     *
     * @param liveType
     * @param size
     * @param marker
     * @return
     */
    public Result getLiveListByAll(Integer liveType, Integer size, String marker) {
        try {
            String prefix = prefix(liveType);
            Hub.ListRet listRet = getHub().list(prefix, size, marker);
            Map<String, Object> data = new HashMap<>(2);
            data.put("marker", listRet.omarker);
            List<Map<String, String>> list = new ArrayList<>(listRet.keys.length);
            for (String key : listRet.keys) {
                Map<String, String> map = new HashMap<>(2);
                map.put("streamName", key);
                map.put("streamStatus", null);
                list.add(map);
            }
            data.put("list", list);
            return new Result(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取推流列表有状态
     *
     * @param liveType
     * @param size
     * @param marker
     * @return
     */
    public Result getLiveListByStatus(Integer liveType, Integer size, String marker) {
        try {
            String prefix = prefix(liveType);
            Hub.ListRet listRet = getHub().list(prefix, size, marker);
            Map<String, Object> data = new HashMap<>(2);
            data.put("marker", listRet.omarker);
            List<Map<String, String>> list = new ArrayList<>(listRet.keys.length);
            for (String key : listRet.keys) {
                Map<String, String> map = new HashMap<>(2);
                map.put("streamName", key);
                map.put("streamStatus", null == liveStatus(key) ? "0" : "1");
                list.add(map);
            }
            data.put("list", list);
            return new Result(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) {
//        new QiuNiuLiveBroadcast().createLiveStream(1, 1);
        Result listRet = new QiuNiuLiveUtil().getLiveListByAll(1, 2, "");
        System.out.println(JSONUtil.toJsonStr(listRet));
//        String marker = MapUtil.getStr(data, "marker");
//        Result b = new QiuNiuLiveBroadcast().getLiveListByAll(1, 1, marker, false);
//        System.out.println(JSONUtil.toJsonStr(b.getData()));
//        Hub.BatchLiveStatus[] bs = new QiuNiuLiveBroadcast().batchLiveStatus(data.keys);
//        for (Hub.BatchLiveStatus b : bs) {
//            System.out.println(b.clientIP);
//        }
    }
    //- RTMPPublishURL()         #生成带授权凭证的 RTMP 推流地址
//- RTMPPlayURL()            #生成 RTMP 播放地址
//- HLSPlayURL()             #生成 HLS 播放地址
//- HDLPlayURL()             #生成 HDL(HTTP-FLV) 播放地址
//- SnapshotPlayURL()        #生成直播封面地址
//- hub.Create()             #创建流
//- hub.Stream()             #获得流
//- hub.List()               #查询流列表
//- hub.ListLive()           #查询直播列表
//- stream.Info()            #查询流信息
//- stream.Disable()         #禁播流（秒级禁推/禁播）
//- stream.Enable()          #启用流
//- stream.LiveStatus()      #查询直播实时信息
//- stream.Save()            #保存直播回放
//- stream.HistoryActivity() #查询直播历史记录
}

