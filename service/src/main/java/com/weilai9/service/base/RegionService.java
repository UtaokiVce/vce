package com.weilai9.service.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.Region;

public interface RegionService extends IService<Region> {
    /**
     * 完整地区列表
     * @return
     * @param current
     * @param size
     * @param regionName
     */
    Result listAll(Integer current, Integer size, String regionName);

    /**
     * 省级地区列表
     *
     * @param current
     * @param size
     * @param regionPid
     * @param regionName
     * @return
     */
    Result assignList(Integer current, Integer size, Integer regionPid, String regionName);
}
