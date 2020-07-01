package com.weilai9.web.controller.alipay;


import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.weilai9.common.utils.alipay.AlipayUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
    public String alipayApp() {
       return alipayUtil.alipayApp();
    }


    @ApiOperation(value = "当面付",notes = "当面付")
    @GetMapping("face")
    public void alipay(HttpServletResponse httpResponse) throws IOException {

    }

    @ApiOperation(value = "回调",notes = "回调")
    @GetMapping("returnUrl")
    public String returnUrl(HttpServletRequest request, HttpServletResponse response)
            throws IOException, AlipayApiException {
        return null;
    }
}
