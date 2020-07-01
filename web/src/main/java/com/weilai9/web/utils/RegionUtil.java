package com.weilai9.web.utils;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.weilai9.dao.entity.Customer;
import com.weilai9.service.api.ApiCustomerService;

import javax.annotation.Resource;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author china.fuyao@outlook.com
 * @date 2020-04-07 10:32
 */
public class RegionUtil implements Serializable {
    @Resource
    ApiCustomerService apiCustomerService;

    /**
     * 获取指定范围
     *
     * @param lng   经度
     * @param lat   纬度
     * @param range 范围 原来对传入的经纬度进行范围计算
     */
    private Map<String, BigDecimal> getRangeResult(String lng, String lat, BigDecimal range) {
        Map<String, BigDecimal> map = new HashMap<>(4);
        BigDecimal leftLng = new BigDecimal(lng).subtract(range);
        BigDecimal rightLng = new BigDecimal(lng).add(range);
        BigDecimal leftLat = new BigDecimal(lat).subtract(range);
        BigDecimal rightLat = new BigDecimal(lat).add(range);

        map.put("leftLng", leftLng);
        map.put("rightLng", rightLng);
        map.put("leftLat", leftLat);
        map.put("rightLat", rightLat);
        return map;
    }

    /**
     * 获取指定范围的用户
     */
    public List<Map<String, Object>> getCustomerListByRange(String lng, String lat, BigDecimal range) {
        Map<String, BigDecimal> rangeMap = getRangeResult(lng, lat, range);
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<Customer>();
        queryWrapper.between("address_lng", MapUtil.get(rangeMap, "leftLng", BigDecimal.class), MapUtil.get(rangeMap, "rightLng", BigDecimal.class));
        queryWrapper.between("address_lat", MapUtil.get(rangeMap, "leftLat", BigDecimal.class), MapUtil.get(rangeMap, "rightLat", BigDecimal.class));
        List<Customer> customerList = apiCustomerService.list(queryWrapper);
        if (null != customerList && customerList.size() > 0) {
            List<Map<String, Object>> dataList = new ArrayList<>(customerList.size());
            Map<String, Object> map;
            for (Customer customer : customerList) {
                map = new HashMap<>(5);
                map.put("customerName", customer.getCustomerName());
                dataList.add(map);
            }
            return dataList;
        }
        return new ArrayList<>(0);
    }
}
