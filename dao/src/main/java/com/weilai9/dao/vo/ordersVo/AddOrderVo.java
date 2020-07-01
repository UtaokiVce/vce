package com.weilai9.dao.vo.ordersVo;

import com.weilai9.dao.entity.Orders;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel("总订单VO")
public class AddOrderVo {

    @ApiModelProperty(value = "总订单合计金额")
    private BigDecimal ordersMoney;

    @ApiModelProperty(value = "商家分组")
    private List<AddOrderStoreVo> addOrderStoreVoList;

}
