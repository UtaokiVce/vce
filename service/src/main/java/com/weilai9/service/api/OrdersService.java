package com.weilai9.service.api;

import com.baomidou.mybatisplus.extension.service.IService;
import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.Goodscomment;
import com.weilai9.dao.entity.Orders;
import com.weilai9.dao.vo.ordersVo.AddOrderVo;
import springfox.documentation.annotations.ApiIgnore;

import java.math.BigDecimal;

/**
 * 订单表(Orders)表服务接口
 *
 * @author makejava
 * @since 2020-04-28 09:14:28
 */
public interface OrdersService extends IService<Orders>{
    /**
     * 添加总订单
     * @param
     * @return
     */
    Result addOrder(AddOrderVo addOrderVo);

    /**
     * 添加商品订单
     * @param
     * @return
     */
    Result addOrderGoods(Integer orderId,Integer shopId,Integer orderType);

    /**
     * 取消订单
     * @param orderId
     * @return
     */
    Result cancelOrder(Integer orderId);

    /**
     * 获取订单列表
     * @return
     */
    Result getOrderSList(TokenUser tokenUser,Integer state);

    /**
     * 修改订单状态
     * @param tokenUser
     * @param orderId
     * @param state
     * @return
     */
    Result changeOrderState(TokenUser tokenUser,Integer orderId,Integer state);


    /**
     * 下单扣减商品库存
     * @param goodsId
     * @param goodsSkuId
     * @return
     */
    Result reduceGoodsStock(Integer goodsId,Integer goodsSkuId,Integer num);

    /**
     * 支付失败回滚库存
     * @param goodsId
     * @param goodsSkuId
     * @returnl
     */
    Result rollBackGoodsStock(Integer goodsId,Integer goodsSkuId,Integer num);

    /**
     * 添加订单商品评价
     * @param goodscomment
     * @return
     */
    Result addOrderRate(Goodscomment goodscomment);

    /**
     * 购物车结算页
     * @param shopIds
     * @return
     */
    Result goodsSettlement(String shopIds);


    /**
     * 获取商品库存
     * @param goodsId
     * @param goodsSkuId
     * @param storeId
     * @return
     */
    Integer getGoodsStockNum(Integer goodsId,Integer goodsSkuId,Integer storeId);


    /**
     * 秒杀商品下单
     * @param ordertype
     * @param goodsallmoney
     * @param realmoney
     * @param storeId
     * @param goodsId
     * @param goodsSkuId
     * @return
     */
    Result secKillGoodsCheckout(Integer ordertype, BigDecimal goodsallmoney, BigDecimal realmoney, Integer storeId, Integer goodsId, Integer goodsSkuId, Integer num);


    /**
     * 根据二维码获取商品信息
     * @param qrState
     * @param tokenUser
     * @return
     */
    Result goodsQr(Integer qrState,TokenUser tokenUser);

    /**
     * 订单支付
     * @param tokenUser
     * @param orderNo
     * @param realmoney
     * @param
     * @return
     */
    Result orderPay(TokenUser tokenUser,String orderNo,BigDecimal realmoney);

    /**
     * 常规下单直接购买
     * @param ordertype
     * @param goodsallmoney
     * @param realmoney
     * @param storeId
     * @param goodsId
     * @param goodsSkuId
     * @param num
     * @return
     */
    Result directGoodsCheckout(Integer ordertype, BigDecimal goodsallmoney, BigDecimal realmoney, Integer storeId, Integer goodsId, Integer goodsSkuId, Integer num);


    /**
     * 获取商店订单列表
     * @param state
     * @return
     */
    Result getStoreOrderList(Integer pageno,Integer pagesize,Integer state,TokenUser tokenUser);


    /**
     * 获取订单详情
     * @param orderId
     * @param tokenUser
     * @return
     */
    Result getOrderInfo(Integer orderId,TokenUser tokenUser);


    /**
     * 订单发货
     * @param orderId
     * @param trackingNo
     * @param tokenUser
     * @return
     */
    Result trackingNo(Integer orderId,String trackingNo,TokenUser tokenUser);
}