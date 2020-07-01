package com.weilai9.web.controller.admin;

import com.weilai9.common.constant.Result;
import com.weilai9.dao.vo.admin.AddCategoryVO;
import com.weilai9.service.api.ApiCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

@Api(tags = "Admin分类相关接口")
@RestController
@RequestMapping("/admin/category")
public class AdminCategoryController {

    @Resource
    private ApiCategoryService apiCategoryService;

    @ApiOperation(value = "admin分类列表", notes = "admin分类列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "类型 1商品 2服务", name = "cateType", required = true),
    })
    @GetMapping("/getAdminCategoryList")
    public Result getAdminCategoryList(Integer cateType) {
        return apiCategoryService.getCategoryByTypeList(cateType);
    }


    @ApiOperation(value = "分类管理列表", notes = "分类管理列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "页数", name = "pageno", required = true),
            @ApiImplicitParam(value = "每页条数", name = "pagesize", required = true),
            @ApiImplicitParam(value = "类型 1商品 2服务", name = "cateType", required = false),
            @ApiImplicitParam(value = "名称", name = "name", required = false),
    })
    @GetMapping("/getCategoryList")
    public Result getCategoryList(Integer pageno,Integer pagesize,Integer cateType,String name) {
        return apiCategoryService.getCategoryList(pageno,pagesize,cateType,name);
    }

    @ApiOperation(value = "分类禁用/启用", notes = "分类禁用/启用")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "分类id", name = "categoryId", required = true),
            @ApiImplicitParam(value = "状态 0禁用 1启用", name = "state", required = true)
    })
    @PostMapping("/doCategoryEnable")
    public Result doCategoryEnable(Integer categoryId,Integer state) {
        if(state==null){
            state=0;
        }
        return apiCategoryService.doCategoryEnable(categoryId,state);
    }

    @ApiOperation(value = "新增/编辑分类", notes = "新增/编辑分类")
    @PostMapping("/saveCategory")
    public Result saveCategory(@ApiIgnore AddCategoryVO addCategoryVO) {
        return apiCategoryService.saveCategory(addCategoryVO);
    }

    @ApiOperation(value = "根据父id获取分类列表", notes = "根据父id获取分类列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "分类id", name = "categoryId", required = true)
    })
    @GetMapping("/getCategoryListByParentId")
    public Result getCategoryListByParentId(Integer categoryId) {
        return apiCategoryService.getCategoryListByParentId(categoryId);
    }


}