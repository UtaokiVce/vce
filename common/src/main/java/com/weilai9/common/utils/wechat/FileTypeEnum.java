package com.weilai9.common.utils.wechat;

import java.util.Calendar;

/**
 * 上传文件类型定义枚举类
 * 统一定义上传、访问路径
 *
 * @author qyb
 * 2019/07/12
 */
public enum FileTypeEnum {
    /**
     * 文件类型
     */
    INFO_IMG("info/img/"),
    SCENIC_IMG("scenic/img/"),
    SCENIC_VRIMG("scenic/vrImg/"),
    SCENIC_AUDIOS("scenic/audio/"),
    INTRODUCE_IMG("introduce/img/"),
    INTRODUCE_VRIMG("introduce/vrimg"),
    INTRODUCE_AUDIOS("introduce/audio"),
    VISITOR_MARKIMG("visitor/markImg/"),
    VISITOR_REGISTER("visitor/register/"),
    MAP_MARK_ICON("map/icon/"),
    PLANT_RECOGNITION("plant/img/");


    /**
     * 地址属性
     */
    private String savePath;
    private String accessPath;

    /**
     * 获取文件存储路径，年月日组织文件目录
     *
     * @return
     */
    private static String getFileTimePath() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DATE);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        // 按照年月日进行组织文件目录;
        return year + "/" + month + "/" + day + "/";
    }

    FileTypeEnum(String path) {
        String datePath = getFileTimePath();
        this.savePath = SysConst.SAVE_PATH + path + datePath;
        this.accessPath = SysConst.ACCESS_PATH + path + datePath;
    }

    public String getAccessPath() {
        return accessPath;
    }


    public String getSavePath() {
        return savePath;
    }

}
