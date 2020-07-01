package com.weilai9.service.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.SysConfig;
import com.weilai9.dao.vo.base.AddConfigVO;
import com.weilai9.dao.vo.base.UpdateConfigVO;

/**
 * 系统配置表(SysConfig)表服务接口
 *
 * @author china.fuyao@outlook.com
 * @since 2020-03-25 18:29:29
 */
public interface SysConfigService extends IService<SysConfig> {
    /**
     * 新增配置
     *
     * @param addConfigVO
     * @param customerId
     * @return
     */
    Result addConfig(AddConfigVO addConfigVO, Long customerId);

    /**
     * 修改配置
     *
     * @param updateConfigVO
     * @param customerId
     * @return
     */
    Result updateConfig(UpdateConfigVO updateConfigVO, Long customerId);
}