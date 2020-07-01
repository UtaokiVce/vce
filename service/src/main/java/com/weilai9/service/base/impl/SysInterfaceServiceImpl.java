package com.weilai9.service.base.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weilai9.common.constant.RedisKey;
import com.weilai9.common.utils.redis.RedisUtil;
import com.weilai9.dao.entity.SysInterface;
import com.weilai9.dao.mapper.SysInterfaceMapper;
import com.weilai9.service.base.SysInterfaceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * 系统接口(SysInterface)表服务实现类
 *
 * @author china.fuyao@outlook.com
 * @since 2020-03-26 11:42:31
 */
@Service("sysInterfaceService")
public class SysInterfaceServiceImpl extends ServiceImpl<SysInterfaceMapper, SysInterface> implements SysInterfaceService {

    @Resource
    RedisUtil redisUtil;

    /**
     * 缓存接口
     */
    public List<SysInterface> redisUrl() {
        List<SysInterface> list = this.list(new QueryWrapper<SysInterface>().eq("if_status", 1));
        for (SysInterface in : list) {
            if (StringUtils.isNotBlank(in.getIfUrl())) {
                redisUtil.set(RedisKey.AUTH_URL_ID + in.getIfUrl(), in.getIfId());
            }
        }
        redisUtil.set(RedisKey.IF_LIST, list);
        return list;
    }
}