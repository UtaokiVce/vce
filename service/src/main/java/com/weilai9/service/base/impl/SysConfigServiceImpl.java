package com.weilai9.service.base.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.SysConfig;
import com.weilai9.dao.mapper.SysConfigMapper;
import com.weilai9.dao.vo.base.AddConfigVO;
import com.weilai9.dao.vo.base.UpdateConfigVO;
import com.weilai9.service.base.SysConfigService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 系统配置表(SysConfig)表服务实现类
 *
 * @author china.fuyao@outlook.com
 * @since 2020-03-25 18:29:30
 */
@Service("sysConfigService")
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements SysConfigService {
    /**
     * 新增配置
     *
     * @param addConfigVO
     * @param customerId
     * @return
     */
    @Override
    public Result addConfig(AddConfigVO addConfigVO, Long customerId) {
        SysConfig sc = new SysConfig();
        BeanUtil.copyProperties(addConfigVO, sc);
        sc.setAddUid(customerId);
        sc.setUpdateUid(customerId);
        Date date = new Date();
        sc.setAddTime(date);
        sc.setUpdateTime(date);
        boolean save = this.save(sc);
        if (save) {
            return Result.OK("新增成功");
        }
        return Result.Error("新增失败");
    }

    /**
     * 修改配置
     *
     * @param updateConfigVO
     * @param customerId
     * @return
     */
    @Override
    public Result updateConfig(UpdateConfigVO updateConfigVO, Long customerId) {
        if (null != updateConfigVO) {
            SysConfig one = getOne(new QueryWrapper<SysConfig>().eq("config_key", updateConfigVO.getConfigKey()));
            if (null == one) {
                return Result.Error("配置不存在");
            }
            SysConfig config = new SysConfig();
            BeanUtil.copyProperties(updateConfigVO, config);
            config.setUpdateUid(customerId);
            config.setUpdateTime(new Date());
            boolean update = update(config, new QueryWrapper<SysConfig>().eq("config_key", updateConfigVO.getConfigKey()));
            if (update) {
                return Result.OK("更新成功");
            }
            return Result.Error("更新失败");
        }
        return Result.Error("参数异常");
    }
}