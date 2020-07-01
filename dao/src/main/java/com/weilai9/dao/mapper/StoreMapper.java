package com.weilai9.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.weilai9.dao.entity.Store;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface StoreMapper extends BaseMapper<Store> {

    /**
     *  推荐门店列表
     * @param num
     * @return
     */
    List<Map<String,Object>> getCommendStoreList(@Param("num") Integer num);


    /**
     * 获取最大推荐值
     * @return
     */
    Integer getMaxRemNum();
}
