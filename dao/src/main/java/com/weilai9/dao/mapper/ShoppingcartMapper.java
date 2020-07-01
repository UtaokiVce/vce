package com.weilai9.dao.mapper;

import com.weilai9.dao.entity.Shoppingcart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weilai9.dao.entity.Store;
import com.weilai9.dao.vo.shoppingCart.CartGoodsVo;

/**
 * 购物车表(Shoppingcart)表数据库访问层
 *
 * @author makejava
 * @since 2020-04-26 18:18:31
 */
public interface ShoppingcartMapper  extends BaseMapper<Shoppingcart> {
    /**
     * 根据购物车商品ID获取商品详细信息
     * @param goodsId
     * @return
     */
    CartGoodsVo queryGoodsByGoodsId(Integer goodsId, Integer goodsSkuId, Integer customerId,Integer storeId);

    /**
     * 根据购物车商家ID查询商家信息
     *
     * @param storeId
     * @return
     */
    Store queryStoreByStoreId(Integer storeId);
}