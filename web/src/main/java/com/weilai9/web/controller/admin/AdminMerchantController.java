package com.weilai9.web.controller.admin;

import com.weilai9.common.constant.Result;
import com.weilai9.dao.vo.store.AddGoodsVO;
import com.weilai9.service.api.ApiGoodsService;
import com.weilai9.service.api.GoodscommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "Admin商家端相关接口")
@RestController
@RequestMapping("/admin/merchant")
public class AdminMerchantController {

    @Resource
    private ApiGoodsService apiGoodsService;
    @Resource
    private GoodscommentService goodscommentService;

    @ApiOperation(value = "商家商品列表", notes = "商家商品列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "页数", name = "pageno", required = true),
            @ApiImplicitParam(value = "每页条数", name = "pagesize", required = true),
            @ApiImplicitParam(value = "商品名称", name = "goodsName", required = false),
            @ApiImplicitParam(value = "一级分类", name = "oneType", required = false),
            @ApiImplicitParam(value = "二级分类", name = "twoType", required = false),
    })
    @GetMapping("/getAdminGoodsList")
    public Result getAdminGoodsList(Integer pageno,Integer pagesize,String goodsName,Integer oneType,Integer twoType) {
        return apiGoodsService.getAdminGoodsList(pageno,pagesize,goodsName,oneType,twoType);
    }

}