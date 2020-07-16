package com.weilai9.web.controller.alipay;


import com.alipay.api.AlipayApiException;
import com.weilai9.common.constant.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: 调用支付宝正式环境
 */
@RestController
@Api(tags = "支付宝支付")
@RequestMapping("alipay")
public class AlipayController {

    @Resource
    private AlipayUtil alipayUtil;

    @ApiOperation(value = "app支付",notes = "app支付")
    @GetMapping("app")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "主体",name = "body",required = true),
            @ApiImplicitParam(value = "商品说明",name = "subject",required = true),
            @ApiImplicitParam(value = "价格（元）",name = "totalAmount",required = true),
            @ApiImplicitParam(value = "商家订单号（不能重复）",name = "outTradeNo",required = true)
    })
    public Result alipayApp(String body, String subject, String totalAmount, String outTradeNo) {
       return alipayUtil.alipayApp(body, subject, totalAmount, outTradeNo);
    }


    @ApiOperation(value = "当面付",notes = "当面付")
    @GetMapping("face")
    public void alipay(HttpServletResponse httpResponse) throws IOException {

    }

    @ApiOperation(value = "回调",notes = "回调")
    @PostMapping("returnUrl")
    public String returnUrl(HttpServletRequest request, HttpServletResponse response)
            throws IOException, AlipayApiException {
        return this.alipayUtil.returnUrl(request,response);
    }

}
