package com.weilai9.common.utils.wechat;

import java.util.HashMap;
import java.util.Map;

/**
 * 接口返回工具类
 */
public class ReturnUtil {

    /**
     * 返回一个Map<String, Object>对象，包含：<br>
     * code：错误码<br>
     * data：返回业务数据
     *
     * @param apiStatus 接口访问状态
     * @param data      业务数据
     * @return
     */

    public static Map<String, Object> returnMap(ApiStatus apiStatus, Map<String, Object> data) {
        Map<String, Object> result = new HashMap<>(16);
        result.put("code", apiStatus.getCode());
        result.put("msg", apiStatus.getMsg());
        result.put("data", data);
        return result;
    }

    /**
     * 返回一个Map<String, Object>对象，包含：<br>
     * code：错误码默认为-100，自定义错误描述<br>
     * msg：错误描述<br>
     * data：返回业务数据
     *
     * @param data 业务数据
     * @return
     */

    public static Map<String, Object> returnMap(String msg, Map<String, Object> data) {
        Map<String, Object> result = new HashMap<>(16);
        result.put("code", ApiStatus.CUSTOMIZED_ERROR.getCode());
        result.put("msg", msg);
        result.put("data", data);
        return result;
    }

    /**
     * 返回一个Map<String, Object>对象，包含：<br>
     * code：错误码默认为-100，自定义错误描述<br>
     * msg：错误描述<br>
     *
     * @param msg 错误描述
     * @return
     */

    public static Map<String, Object> returnMap(String msg) {
        Map<String, Object> result = new HashMap<>(16);
        result.put("code", ApiStatus.CUSTOMIZED_ERROR.getCode());
        result.put("msg", msg);
        return result;
    }

    /**
     * 返回一个Map<String, Object>对象，包含：<br>
     * code：错误码<br>
     * msg：错误描述<br>
     * data：返回业务数据
     *
     * @param apiStatus 接口访问状态
     * @return
     */

    public static Map<String, Object> returnMap(ApiStatus apiStatus) {
        Map<String, Object> result = new HashMap<>(16);
        result.put("code", apiStatus.getCode());
        result.put("msg", apiStatus.getMsg());
        return result;
    }
}
