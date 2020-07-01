package com.weilai9.web.controller.api;

import com.weilai9.common.config.annotation.MustLogin;
import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.service.api.ApiCustomerService;
import com.weilai9.service.api.ApiGoodsService;
import com.weilai9.service.api.ApiStoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

@Api(tags = "Api首页相关接口")
@RestController
@RequestMapping("/api/headpage")
public class ApiIndexPageController {
    @Resource
    private ApiStoreService apiStoreService;

    @Resource
    private ApiGoodsService apiGoodsService;

    @ApiOperation(value = "首页推荐门店列表", notes = "首页推荐门店列表")
    @GetMapping("/getCommendStoreList")
    public Result getCommendStoreList() {
        return apiStoreService.getCommendStoreList();
    }

    @ApiOperation(value = "首页推荐商品列表", notes = "首页推荐商品列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "页数", name = "pageno", required = true),
            @ApiImplicitParam(value = "每页条数", name = "pagesize", required = true)
    })
    @GetMapping("/getCommendGoodsList")
    public Result getCommendGoodsList(Integer pageno,Integer pagesize) {
        return apiGoodsService.getCommendGoodsList(pageno,pagesize);
    }

    @ApiOperation(value = "商城首页banner", notes = "商城首页banner")
    @GetMapping("/getShoppingBanner")
    public Result getShoppingBanner() {

        return this.apiGoodsService.getShoppingBanner();
    }


    @ApiOperation(value = "门店列表", notes = "门店列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "页数", name = "pageno", required = true),
            @ApiImplicitParam(value = "每页条数", name = "pagesize", required = true),
            @ApiImplicitParam(value = "门店地址", name = "addr", required = false),
            @ApiImplicitParam(value = "门店类型", name = "storeType", required = false)
    })
    @GetMapping("/getStoreList")
    public Result getStoreList(Integer pageno,Integer pagesize,String addr,Integer storeType) {
        return apiGoodsService.storeList(pageno, pagesize, addr, storeType);
    }

    @ApiOperation(value = "门店详情", notes = "门店详情")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "门店ID", name = "storeId", required = true),
    })
    @GetMapping("/getStoreInfo")
    public Result getStoreInfo(Integer storeId) {
        return this.apiGoodsService.storeInfo(storeId);
    }
}
