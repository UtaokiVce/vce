package com.weilai9.web.controller.activity;

import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.ActivityMsg;
import com.weilai9.dao.entity.ActivitySignup;
import com.weilai9.dao.entity.SysDict;
import com.weilai9.dao.vo.activity.AddActivityMsgVO;
import com.weilai9.dao.vo.activity.UpdateActivityMsgVO;
import com.weilai9.service.admin.ActivityMsgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.List;

/**
 * (ActivityMsg)表控制层
 *
 * @author makejava
 * @since 2020-04-26 17:56:00
 */
@Api(tags = "活动相关接口")
@RestController
@RequestMapping("activityMsg")
public class ActivityMsgController {
    /**
     * 服务对象
     */
    @Resource
    private ActivityMsgService activityMsgService;

    /**
     * 查询所有活动
     *
     * @return
     */
    @ApiOperation(value = "查询所有活动", notes = "获取所有活动列表")
    @RequestMapping(value = "/getActivity", method = RequestMethod.GET)
    public Result getAll(int page,int size,String introductory) {
        return activityMsgService.getAll(page, size,introductory);
    }


    /**
     * 添加(发布)活动
     * @param
     * @return
     */
    @ApiOperation(value = "添加(发布)活动", notes = "添加(发布)活动")
    @RequestMapping(value = "/addActivity", method = RequestMethod.POST)
    public Result addActivity(@RequestBody AddActivityMsgVO vo,@ApiIgnore TokenUser tokenUser) {
        Result result = activityMsgService.addActivity(vo,tokenUser.getCustomerId());
        return result;

    }

    /**
     * 修改/编辑活动
     *
     * @param activityMsg
     * @return
     */
    @ApiOperation(value = "修改活动", notes = "修改活动")
    @RequestMapping(value = "/updateActivity", method = RequestMethod.PUT)
    public Result updateActivity(@RequestBody UpdateActivityMsgVO activityMsg) {
        Result result = activityMsgService.updateActivity(activityMsg);
        return result;

    }

    /**
     * 根据id删除活动
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除活动", notes = "删除活动")
    @ApiImplicitParam(value = "活动id", name = "id", required = true)
    @RequestMapping(value = "/delActivity", method = RequestMethod.DELETE)
    public Result delActivity(Integer id) {
        Result result = activityMsgService.delActivity(id);
        return result;

    }


    /**
     * 查询该商家下的所有活动
     *
     * @param
     * @return
     */
    @ApiOperation(value = "该商家下的所有活动", notes = "查询该商家下的所有活动")
    @RequestMapping(value = "/getActByStore", method = RequestMethod.GET)
    public Result getActByStore(int page,int size,@ApiIgnore TokenUser tokenUser) {

        return activityMsgService.getActByStore(page, size, tokenUser.getCustomerId());

    }

    /**
     * 查询单条活动详情
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "活动详情", notes = "查询单条活动详情")
    @ApiImplicitParam(value = "活动id", name = "id", required = true)
    @RequestMapping(value = "/getActById", method = RequestMethod.GET)
    public Result getActById(Integer id) {
        return activityMsgService.getActById(id);

    }

    /**
     * 查询活动报名信息列表
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "参与列表", notes = "查询活动报名信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "当前页数", name = "current", required = true),
            @ApiImplicitParam(value = "每页数量", name = "size", required = true),
            @ApiImplicitParam(value = "活动id", name = "id", required = true)
    })
    @RequestMapping(value = "/getActRegistration", method = RequestMethod.GET)
    public Result getActRegistration(Integer current, Integer size, Integer id) {
        return activityMsgService.getActRegistration(current, size, id);

    }

    /**
     * 再次发起
     *
     * @param id
     * @return
     */
    @ApiImplicitParam(value = "活动id", name = "id", required = true)
    @ApiOperation(value = "再次发起", notes = "再次发起")
    @RequestMapping(value = "/again", method = RequestMethod.GET)
    public Result again(Integer id) {
        return activityMsgService.again(id);

    }

    /**
     * 查看审核不通过原因
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "审核不通过原因", notes = "查看审核不通过原因")
    @ApiImplicitParam(value = "活动id", name = "id", required = true)
    @RequestMapping(value = "/getRefusedReason", method = RequestMethod.GET)
    public Result getRefusedReason(Integer id) {
        return activityMsgService.getRefusedReason(id);

    }

//======================用户端=========================

    /**
     * 根据活动类型查询所有活动,未指定活动类型则查询全部
     *
     * @param activityType
     * @return
     */
    @ApiOperation(value = "活动分类", notes = "根据活动类型查询所有活动")
    @RequestMapping(value = "/getActByType", method = RequestMethod.GET)
    @ApiImplicitParam(value = "活动类型", name = "activityType")
    public Result getActByType(String activityType) {
        Result result = activityMsgService.getActByType(activityType);
        return result;
    }


    /**
     * 获取所有活动类型
     *
     * @return
     */
    @ApiOperation(value = "查询活动类型", notes = "查询所有活动类型")
    @RequestMapping(value = "/getActType", method = RequestMethod.GET)
    public Result getActType() {
        Result result = activityMsgService.getActType();
        return result;

    }


    /**
     * 查询我的活动
     *
     * @param status 状态，不传查询全部
     * @return
     */
    @ApiOperation(value = "查询我的活动", notes = "查询我的活动")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "活动状态，不传查询全部", name = "status")
    })
    @RequestMapping(value = "/getActByUser", method = RequestMethod.GET)
    public Result getActByUser(Integer status) {
        Result result = activityMsgService.getActByUser(status);
        return result;

    }


    /**
     * 添加活动类型
     *
     * @return
     */
    @ApiOperation(value = "添加活动类型", notes = "添加活动类型")
    @RequestMapping(value = "/addActType", method = RequestMethod.POST)
    public Result addActType(@RequestBody SysDict obj) {
        Result result = activityMsgService.addActType(obj);
        return result;

    }

    //支付
    @ApiOperation(value = "活动支付信息", notes = "活动支付信息")
    @RequestMapping(value = "/actPay", method = RequestMethod.GET)
    public Result actPay(Integer orderId) {
        Result result = activityMsgService.actPay(orderId);
        return result;

    }

    //首页活动入口
    @ApiOperation(value = "首页活动入口", notes = "首页活动入口")
    @RequestMapping(value = "/getActIndex", method = RequestMethod.GET)
    public Result getActIndex() {
        Result result = activityMsgService.getActIndex();
        return result;

    }


    /**
     * 活动上下架
     *
     * @param
     * @return
     */
    @ApiOperation(value = "活动上下架", notes = "活动上下架")
    @RequestMapping(value = "/switchAct", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(value = "活动id", name = "id", required = true),
            @ApiImplicitParam(value = "状态", name = "status", required = true)
    })
    public Result switchAct(Integer id,Integer status) {
        Result result = activityMsgService.switchAct(id, status);
        return result;

    }





}