package com.weilai9.web.controller.api;

import com.weilai9.common.constant.Result;
import com.weilai9.service.api.ApiCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "api分类相关接口")
@RestController
@RequestMapping("/api/category")
public class ApiCategoryController {

    @Resource
    private ApiCategoryService apiCategoryService;

    @ApiOperation(value = "api分类列表", notes = "api分类列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "类型 1商品 2服务", name = "cateType", required = true),
    })
    @GetMapping("/getApiCategoryList")
    public Result getApiCategoryList(Integer cateType) {
        return apiCategoryService.getCategoryByTypeList(cateType);
    }


}