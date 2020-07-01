package com.weilai9.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.weilai9.dao.entity.MsgInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface MsgInfoMapper extends BaseMapper<MsgInfo> {
    /**
     * 获取系统消息列表
     *
     * @param page
     * @param code
     * @param key
     * @return
     */
    IPage<Map> getSysMsgList(IPage page, @Param("code") int code, @Param("key") String key);
}
