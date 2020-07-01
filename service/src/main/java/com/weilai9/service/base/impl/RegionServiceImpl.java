package com.weilai9.service.base.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.Region;
import com.weilai9.dao.mapper.RegionMapper;
import com.weilai9.service.base.RegionService;
import org.springframework.stereotype.Service;

/**
 * (region)表服务实现类
 *
 * @author china.fuyao@outlook.com
 * @since 2020-03-25 18:19:12
 */
@Service("regionService")
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements RegionService {

    /**
     * 完整地区列表
     *
     * @param current
     * @param size
     * @param regionName
     * @return
     */
    @Override
    public Result listAll(Integer current, Integer size, String regionName) {
        IPage<Region> page = new Page<>(null == current ? 1 : current, null == size ? 10 : size);
        QueryWrapper<Region> wrapper = new QueryWrapper<>();
        if (!StrUtil.isNullOrUndefined(regionName)) {
            wrapper.like("region_name", regionName);
        }
        IPage<Region> selectPage = baseMapper.selectPage(page, wrapper);
        return new Result(selectPage);
    }

    /**
     * 省级地区列表
     *
     * @param current
     * @param size
     * @param regionPid
     * @param regionName
     * @return
     */
    @Override
    public Result assignList(Integer current, Integer size, Integer regionPid, String regionName) {
        IPage<Region> page = new Page<>(null == current ? 1 : current, null == size ? 10 : size);
        QueryWrapper<Region> wrapper = new QueryWrapper<>();
        wrapper.eq("region_pid", regionPid);
        if (!StrUtil.isNullOrUndefined(regionName)) {
            wrapper.like("region_name", regionName);
        }
        IPage<Region> selectPage = baseMapper.selectPage(page, wrapper);
        return new Result(selectPage);
    }
}
