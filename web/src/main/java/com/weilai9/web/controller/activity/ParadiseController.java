package com.weilai9.web.controller.activity;

import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.Paradise;
import com.weilai9.dao.vo.activity.AddParadiseVO;
import com.weilai9.service.admin.ParadiseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

/**
 * 乐园信息表(Paradise)表控制层
 *
 * @author makejava
 * @since 2020-05-08 10:09:42
 */
@RestController
@RequestMapping("paradise")
@Api(tags = "乐园相关接口")
public class ParadiseController {
    /**
     * 服务对象
     */
    @Resource
    private ParadiseService paradiseService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */

    @GetMapping("selectOne")
    @ApiOperation(value = "根据id查询乐园", notes = "根据id查询乐园")
    @ApiImplicitParam(value = "主键", name = "id", required = true)
    public Result selectOne(Integer id) {
        Paradise paradise = paradiseService.queryById(id);
        return new Result(paradise);
    }

    /**
     * 查询多条数据
     */
    @GetMapping("getAll")
    @ApiOperation(value = "查询全部乐园", notes = "查询多条数据")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "当前页", name = "current", required = true),
            @ApiImplicitParam(value = "每页条数", name = "size", required = true),
            @ApiImplicitParam(value = "乐园名", name = "name")
    })

    public Result getAll(Integer current, Integer size,String name) {
        Result paradise = paradiseService.getAll(current, size,name);
        return paradise;
    }


    /**
     * 查询该商家的所有乐园
     */
    @RequestMapping(value = "getParadiseByStore", method = RequestMethod.GET)
    @ApiOperation(value = "查询该商家的所有乐园", notes = "查询该商家的所有乐园")
    public Result getParadiseByStore(int page,int size,@ApiIgnore TokenUser tokenUser) {
        Result result = paradiseService.getParadiseByStore( page, size, tokenUser.getCustomerId());
        return result;
    }

    /**
     * 查询单条乐园详情--商家
     */
    @RequestMapping(value = "getById", method = RequestMethod.GET)
    @ApiOperation(value = "查询单条乐园详情--商家", notes = "查询单条乐园详情--商家")
    @ApiImplicitParam(value = "乐园id", name = "id", required = true)
    public Result getById(Integer id) {
        Result result = paradiseService.getDetail(id);
        return result;
    }

    /**
     * 删除乐园
     */
    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除乐园", notes = "删除乐园")
    @ApiImplicitParam(value = "乐园id", name = "id", required = true)
    public Result delete(Integer id) {
        Result result = paradiseService.detele(id);
        return result;
    }

    /**
     * 修改乐园
     */
    @RequestMapping(value = "updateParadise", method = RequestMethod.PUT)
    @ApiOperation(value = "修改乐园", notes = "修改乐园")
    public Result updateParadise(@RequestBody Paradise obj) {
        Paradise result = paradiseService.update(obj);
        return new Result(result);
    }

    /**
     * 开园/闭园
     */
    @RequestMapping(value = "switchPar", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(value = "乐园id", name = "id", required = true),
            @ApiImplicitParam(value = "状态", name = "status", required = true)
    })
    @ApiOperation(value = "开园/闭园", notes = "开园/闭园")
    public Result switchPar(Integer id,Integer status) {
        Result result = paradiseService.switchPar(id, status);
        return result;
    }


    /**
     * 添加乐园
     */
    @RequestMapping(value = "addParadise", method = RequestMethod.POST)
    @ApiOperation(value = "添加乐园", notes = "添加乐园")
    public Result addParadise(@RequestBody AddParadiseVO obj,@ApiIgnore TokenUser tokenUser) {
        Result result = paradiseService.addParadise(obj,tokenUser.getCustomerId());
        return result;
    }


    /**
     * 查询乐园参与列表
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "查询乐园参与列表", notes = "查询乐园参与列表")
    @RequestMapping(value = "/getParaRegistration", method = RequestMethod.GET)
    public Result getParaRegistration(Integer current, Integer size, Integer id) {
        return paradiseService.getParaRegistration(current, size, id);

    }

    //========================用户端============================

    /**
     * 乐园列表
     *
     * @param
     * @return
     */
    @ApiOperation(value = "乐园列表", notes = "乐园列表")
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    public Result getList() {
        return paradiseService.getList();

    }


    /**
     * 查询单条乐园详情--用户
     */
    @RequestMapping(value = "getById2", method = RequestMethod.GET)
    @ApiOperation(value = "查询单条乐园详情--用户", notes = "查询单条乐园详情--用户")
    @ApiImplicitParam(value = "乐园id", name = "id", required = true)
    public Result getById2(Integer id) {
        Result result = paradiseService.getDetail2(id);
        return new Result(result);
    }


    /**
     * 查询我的预约
     */
    @RequestMapping(value = "getMyParadise", method = RequestMethod.GET)
    @ApiOperation(value = "查询我的预约", notes = "查询我的预约,根据状态查询，不传status为查询全部状态")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "乐园状态", name = "status")
    })
    public Result getMyParadise(Integer status) {
        Result result = paradiseService.getMyParadise(status);
        return new Result(result);
    }



    //支付
    @ApiOperation(value = "乐园支付信息", notes = "乐园支付信息")
    @RequestMapping(value = "/parPay", method = RequestMethod.GET)
    public Result parPay(Integer orderId) {
        Result result = paradiseService.parPay(orderId);
        return result;

    }



    //首页乐园列表
    @RequestMapping(value = "getParList", method = RequestMethod.GET)
    @ApiOperation(value = "首页乐园列表", notes = "首页乐园列表")
    public Result getParList() {
        return paradiseService.getParList();
    }



}