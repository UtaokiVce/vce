package com.weilai9.web.controller.admin;

import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.SecondTime;
import com.weilai9.dao.entity.Store;
import com.weilai9.service.admin.AdminStoreService;
import com.weilai9.service.api.ActiveService;
import com.weilai9.service.api.ApiStoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;


@Api(tags = "Admin商店接口")
@RestController
@RequestMapping("/admin/store")
public class AdminStoreController {
    @Resource
    AdminStoreService adminStoreService;

    @Resource
    ApiStoreService apiStoreService;

    @ApiOperation(value = "添加门店，传id为编辑", notes = "添加门店")
    @PostMapping("/addStore")
    public Result addSecondTime(@RequestBody Store store,@ApiIgnore TokenUser tokenUser) {
        return this.adminStoreService.addStore(store, tokenUser);
    }

    @ApiOperation(value = "获取商家列表", notes = "获取商家列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "页数", name = "pageno", required = true),
            @ApiImplicitParam(value = "每页条数", name = "pagesize", required = true),
            @ApiImplicitParam(value = "商店名", name = "storeName", required = false),
    })
    @GetMapping("/getStoreList")
    public Result getStoreList(Integer pageno,Integer pagesize,String storeName) {
        return this.adminStoreService.getStoreList(pageno, pagesize, storeName);
    }

    @ApiOperation(value = "修改商店推荐状态", notes = "修改商店推荐状态")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "状态（0否 1是）", name = "isRem", required = true),
            @ApiImplicitParam(value = "商店Id", name = "storeId", required = true)

    })
    @GetMapping("/changeRem")
    public Result changeRem(Integer isRem,Integer storeId) {
        return this.adminStoreService.changeRem(isRem, storeId);
    }



    @ApiOperation(value = "门店修改状态,ID不传为本用户门店", notes = "门店启用/禁用")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "状态（0禁用  1启用 3通过 4拒绝）", name = "state", required = true),
            @ApiImplicitParam(value = "门店id", name = "storeId", required = false),
    })
    @GetMapping("/changeStoreState")
    public Result changeStoreState(Integer state,Integer storeId,@ApiIgnore TokenUser tokenUser) {
        return this.adminStoreService.changeStoreState(state, storeId, tokenUser);
    }


    @ApiOperation(value = "后台推荐商家列表", notes = "后台推荐商家列表")
    @GetMapping("/getCommendStoreList")
    public Result getCommendStoreList() {
        return this.apiStoreService.adminGetCommendStoreList();
    }

    @ApiOperation(value = "后台推荐商家排序", notes = "后台推荐商家排序")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "id", name = "id", required = true),
            @ApiImplicitParam(value = "操作 1上移 2下移 3置顶", name = "operation", required = true)
    })
    @GetMapping("/commendStoreSort")
    public Result commendStoreSort(Integer id, Integer operation) {
        return apiStoreService.adminCommendStoreSort(id, operation);
    }

    @ApiOperation(value = "商店取消推荐", notes = "商店取消推荐")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "门店id", name = "storeId", required = true)
    })
    @GetMapping("/store2NotRem")
    public Result store2NotRem(Integer storeId) {
        return this.apiStoreService.store2NotRem(storeId);
    }
}
