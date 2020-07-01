package com.weilai9.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weilai9.dao.entity.Orders;
import com.weilai9.dao.vo.ordersVo.OrderGoodsVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 订单表(Orders)表数据库访问层
 *
 * @author makejava
 * @since 2020-04-27 21:19:45
 */
public interface OrdersMapper  extends BaseMapper<Orders> {
    Orders queryByOrderNo(String orderNo);
    OrderGoodsVo getOrderGoodsVoByCondition(Integer goodsId,Integer goodsSku,Integer orderId);

    List<Map<String,Object>> getStoreOrdersList(@Param("state") Integer state,@Param("storeId") Integer storeId);
}