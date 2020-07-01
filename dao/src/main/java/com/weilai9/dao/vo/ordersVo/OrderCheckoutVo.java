package com.weilai9.dao.vo.ordersVo;

import com.weilai9.dao.entity.Orders;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel("批量下单VO")
public class OrderCheckoutVo {

    @ApiModelProperty(value = "总订单")
    private Orders orders;

    @ApiModelProperty(value = "购物车ID")
    private String shopIds;

}
