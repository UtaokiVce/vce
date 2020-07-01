package com.weilai9.dao.vo.ordersVo;

import com.weilai9.dao.entity.Orders;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel("总订单VO")
public class OrderVo {

    @ApiModelProperty(value = "总订单")
    private Orders orders;

    @ApiModelProperty(value = "订单结束时间")
    private String endtime;

    @ApiModelProperty(value = "商家分组")
    private List<OrderStoreVo> orderStoreVoList;


}
