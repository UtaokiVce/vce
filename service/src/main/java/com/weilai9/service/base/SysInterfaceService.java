package com.weilai9.service.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.weilai9.dao.entity.SysInterface;

import java.util.List;

/**
 * (Interface)表服务接口
 *
 * @author china.fuyao@outlook.com
 * @since 2020-03-26 11:42:31
 */
public interface SysInterfaceService extends IService<SysInterface> {
    /**
     * 缓存接口
     * @return
     */
    List<SysInterface> redisUrl();
}