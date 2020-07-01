package com.weilai9.dao.vo.wechat;

import lombok.Data;


/**
 * @author xjh
 */
@Data
public class WxOrderVo {


    /**
     * <pre>
     * 字段名：商品描述.
     * 变量名：body
     * 是否必填：是
     * 类型：String(128)
     * 示例值： 腾讯充值中心-QQ会员充值
     * 描述：商品简单描述，该字段须严格按照规范传递，具体请见参数规定
     * </pre>
     */
    private String body;


    /**
     * <pre>
     * 字段名：备注.
     * 变量名：attach
     * 是否必填：否
     * 类型：String(127)
     * 示例值： 深圳分店
     * 描述：  附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
     * </pre>
     */
    private String attach;

    /**
     * <pre>
     * 字段名：商户订单号.
     * 变量名：out_trade_no
     * 是否必填：是
     * 类型：String(32)
     * 示例值：20150806125346
     * 描述：商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
     * </pre>
     */
    private String outTradeNo;


    /**
     * <pre>
     * 字段名：总金额.
     * 变量名：total_fee
     * 是否必填：是
     * 类型：Int
     * 示例值： 888
     * 描述：订单总金额，单位为分，详见支付金额
     * </pre>
     */
    private Integer totalFee;



    /**
     * <pre>
     * 字段名：商品Id.
     * 变量名：product_id
     * 是否必填：否
     * 类型：String(32)
     * 示例值：12235413214070356458058
     * 描述：trade_type=NATIVE，此参数必传。此id为二维码中包含的商品Id，商户自行定义。
     * </pre>
     */
    private String productId;


}
