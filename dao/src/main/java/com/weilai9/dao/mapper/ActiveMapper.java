package com.weilai9.dao.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.weilai9.dao.entity.Active;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 活动表(Active)表数据库访问层
 *
 * @author makejava
 * @since 2020-04-27 14:21:47
 */
public interface ActiveMapper  extends BaseMapper<Active> {

    /**
     *  活动商品列表
     * @param page
     * @param storeId
     * @param activeType
     * @return
     */
    IPage<Map> getStoreActiveGoodsList(IPage page, @Param("storeId") Integer storeId, @Param("activeType")Integer activeType, @Param("state")Integer state, @Param("priceSort")Integer priceSort);

    /**
     *  秒杀商品列表
     * @param page
     * @param timename
     * @return
     */
    IPage<Map> getSecendGoodsList(IPage page, @Param("timename") String timename);

    /**
     *  查询活动商品数量
     * @param storeId
     * @param goodsSkuId
     * @param startTime
     * @param endTime
     * @return
     */
    int getActiveCount( @Param("storeId") Integer storeId,@Param("goodsSkuId")Integer goodsSkuId,@Param("startTime")String startTime,@Param("endTime")String endTime);

    List<Map<String,Object>> getSecendTimeList();

    /**
     *  团购列表
     * @param page
     * @return
     */
    IPage<Map> getGroupsGoodsList(IPage page,@Param("title")String title,@Param("newtime")Integer newtime,@Param("shopnum")Integer shopnum,@Param("inte")Integer inte,@Param("price")Integer price);

    List<Map<String,Object>> getGroupList();
}