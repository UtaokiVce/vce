package com.weilai9.web.controller.banner;

import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.Banner;
import com.weilai9.dao.vo.activity.AddBannerVO;
import com.weilai9.dao.vo.activity.UpdateBannerVO;
import com.weilai9.service.admin.BannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * banner表(Banner)表控制层
 *
 * @author makejava
 * @since 2020-06-10 14:19:36
 */
@RestController
@RequestMapping("banner")
@Api(tags = "banner相关接口")
public class BannerController {
    /**
     * 服务对象
     */
    @Resource
    private BannerService bannerService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation(value = "通过主键查询单条数据",notes = "通过主键查询单条数据")
    @ApiImplicitParam(value = "id", name = "id", required = true)
    @GetMapping("selectOne")
    public Banner selectOne(Integer id) {
        return this.bannerService.queryById(id);
    }


     /**
     * 修改数据
     *
     */
    @ApiOperation(value = "修改数据",notes = "修改数据")
    @PostMapping("updateBanner")
    public Result updateBanner(@RequestBody UpdateBannerVO vo) {
        return  bannerService.updateByVO(vo);
    }

 /**
     * 修改状态
     *
     */
    @ApiOperation(value = "修改状态",notes = "修改状态")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "id", name = "id", required = true),
            @ApiImplicitParam(value = "状态 0禁用 1启用", name = "state", required = true)
    })
    @PostMapping("updateState")
    public Result updateState(Integer id,Integer state) {
        return  bannerService.updateState(id, state);
    }



     /**
     * 通过主键删除单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation(value = "通过主键删除单条数据",notes = "通过主键查询单条数据")
    @ApiImplicitParam(value = "id", name = "id", required = true)
    @GetMapping("deleteOne")
    public Result deleteOne(Integer id) {
        return this.bannerService.deleteById(id);
    }


    @ApiOperation(value = "查询详情",notes = "查询详情")
    @ApiImplicitParam(value = "bannerId", name = "bannerId", required = true)
    @GetMapping("getDetails")
    public Result getDetails(Integer bannerId) {

        return bannerService.getDetails(bannerId);

    }


    /**
     * 查询banner
     */
    @ApiOperation(value = "查询banner,app",notes = "查询banner列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "页码", name = "page", required = true),
            @ApiImplicitParam(value = "数量", name = "size", required = true),
            @ApiImplicitParam(value = "banner类型 1:商品 2商家", name = "type"),
            @ApiImplicitParam(value = "banner位置 1:首页 2商城", name = "site")
    })
    @GetMapping("selectList")
    public Result selectList(Integer page, Integer size, Integer type, Integer site) {

        return bannerService.selectList(page, size, type, site,1);
    }

    /**
     * 查询banner列表,后台
     */
    @ApiOperation(value = "查询banner列表,后台",notes = "查询banner列表，后台")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "页码", name = "page", required = true),
            @ApiImplicitParam(value = "数量", name = "size", required = true),
            @ApiImplicitParam(value = "banner类型 1:商品 2商家", name = "type"),
            @ApiImplicitParam(value = "状态", name = "state"),
            @ApiImplicitParam(value = "banner位置 1:首页 2商城", name = "site")
    })
    @GetMapping("selectListAdmin")
    public Result selectList(Integer page, Integer size, Integer type, Integer site,Integer state) {

        return bannerService.selectList(page, size, type, site,state);
    }


    /**
     * 添加banner
     */
    @ApiOperation(value = "添加banner",notes = "添加banner")
    @PostMapping("addBanner")
    public Result addBanner(@RequestBody AddBannerVO vo) {
        return bannerService.addBanner(vo);
    }

    /**
     * 移动banner顺序
     */
    @ApiOperation(value = "移动banner顺序",notes = "移动banner顺序")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "id", name = "id", required = true),
            @ApiImplicitParam(value = "操作 1上移 2下移 3置顶", name = "operation", required = true)
    })
    @PostMapping("updateBannerSort")
    public Result updateBannerSort(Integer id,Integer operation) {
        return bannerService.updateBannerSort(id, operation);
    }

}