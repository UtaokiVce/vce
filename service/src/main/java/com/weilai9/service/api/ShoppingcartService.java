package com.weilai9.service.api;

import com.baomidou.mybatisplus.extension.service.IService;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.Shoppingcart;
import com.weilai9.dao.vo.shoppingCart.CartGoodsVo;

/**
 * 购物车表(Shoppingcart)表服务接口
 *
 * @author makejava
 * @since 2020-04-26 18:19:42
 */
public interface ShoppingcartService extends IService<Shoppingcart>{
    /**
     * 添加商品进购物车
     * @param shoppingcart
     * @return
     */
    Result addGoods2Cart(Shoppingcart shoppingcart);

    /**
     * 购物车商品数量增加
     * @param shopid
     * @return
     */
    Result addNum(Integer shopid);

    /**
     * 购物车商品数量减少
     * @param shopid
     * @return
     */
    Result reduceNum(Integer shopid);

    /**
     * 删除购物车中商品
     * @param shopid
     * @return
     */
    Result deleteGoods(Integer shopid);
    /**
     * 根据用户ID查询购物车列表
     * @param
     * @return
     */
    Result queryGoodsByCustomerId();

    /**
     * 根据商品ID查询购物车商品详情
     * @param goodsId
     * @return
     */
    CartGoodsVo queryGoodsByGoodsId(Integer goodsId, Integer goodsSkuId, Integer storeId);


    /**
     * 结算页清单
     * @param
     * @return
     */
    Result getAccount(String shopIds);
}