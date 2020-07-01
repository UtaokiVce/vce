package com.weilai9.web.controller.app;

import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.service.admin.AdminStoreService;
import com.weilai9.service.api.ActiveService;
import com.weilai9.service.api.ApiGoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

@Api(tags = "App核销相关接口")
@RestController
@RequestMapping("/app/qr")
public class AppQRController {

    @Resource
    private AdminStoreService adminStoreService;



    @ApiOperation(value = "商品核销", notes = "商品核销")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "核销码", name = "QrCode", required = true),
    })
    @GetMapping("/goodsQr")
    public Result goodsQr(String QrCode,@ApiIgnore TokenUser tokenUser) {
        return this.adminStoreService.goodsQr(QrCode, tokenUser);
    }

    @ApiOperation(value = "根据核销码查询商品信息", notes = "根据核销码查询商品信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "核销码", name = "QrCode", required = true),
    })
    @GetMapping("/goodsQrInfo")
    public Result goodsQrInfo(String QrCode,@ApiIgnore TokenUser tokenUser) {
        return this.adminStoreService.goodsQrInfo(QrCode, tokenUser);
    }




}