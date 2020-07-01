package com.weilai9.web.controller.activity;

import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.ActivitySignup;
import com.weilai9.service.admin.ActivitySignupService;
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
import java.util.Map;

/**
 * (ActivitySignup)表控制层
 *
 * @author makejava
 * @since 2020-04-26 17:56:24
 */
@RestController
@RequestMapping("activitySignup")
@Api(tags = "活动报名信息相关接口")
public class ActivitySignupController {
    /**
     * 服务对象  订单
     */
    @Resource
    private ActivitySignupService activitySignupService;

    /**
     * 查询我的活动
     *
     * @param
     * @param status
     * @return
     */
    @ApiOperation(value = "查询我的活动", notes = "查询我的活动")
    @RequestMapping(value = "/getMyAct", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(value = "活动状态", name = "status")
    })
    public List<Map<String, Object>> getMyAct( String status) {

        List<Map<String, Object>> result = activitySignupService.getMyAct(status);
        return result;
    }

    /**
     * 查询单个订单
     *
     * @param actSignId
     * @return
     */
    @ApiOperation(value = "查询单个订单", notes = "查询单个订单")
    @ApiImplicitParam(value = "订单id", name = "actSignId", required = true)
    @RequestMapping(value = "/getOne", method = RequestMethod.GET)
    public Result getOne(String actSignId) {

        Map<String, Object> result = activitySignupService.getOne(actSignId);

        return new Result(result);
    }

    /**
     * 添加订单
     *
     * @param activitySignup
     * @return
     */
    @ApiOperation(value = "添加订单", notes = "添加订单")
    @RequestMapping(value = "/addOrder", method = RequestMethod.POST)
    public Result addOrder(@RequestBody ActivitySignup activitySignup) {
        return activitySignupService.addOrder(activitySignup);

    }

    /**
     * 修改订单状态
     * @return
     */
    @ApiOperation(value = "修改订单状态", notes = "修改订单状态")
    @RequestMapping(value = "/updateOrderStatus", method = RequestMethod.PUT)
    public Result updateOrderStatus(Integer id,Integer status) {

        return activitySignupService.updateOrderStatus(id,status);

    }


    /**
     * 我要预约
     * 活动id
     *
     * @return
     */
    @RequestMapping(value = "appointment", method = RequestMethod.GET)
    @ApiOperation(value = "我要预约", notes = "我要预约")
    @ApiImplicitParam(value = "乐园id", name = "activityId", required = true)
    public Result appointment(Integer activityId) {
        return activitySignupService.appointment(activityId);
    }


    /**
     * 核销
     *
     * @param orderId 订单id
     * @return
     */
    @RequestMapping(value = "writeOff", method = RequestMethod.PUT)
    @ApiOperation(value = "核销成功", notes = "核销成功")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "报名信息id", name = "orderId", required = true),
            @ApiImplicitParam(value = "成人核销人数", name = "adultNum"),
            @ApiImplicitParam(value = "儿童核销人数", name = "childNum")
    })
    public Result writeOff(Integer orderId,String adultNum,String childNum) {
        Integer adultNum1 = null;
        Integer childNum1 = null;
        if(!(adultNum == null || "null".equals(adultNum))){
            adultNum1 = Integer.parseInt(adultNum);
        }
        if(!(childNum == null || "null".equals(childNum))){
            childNum1 = Integer.parseInt(childNum);
        }
        return activitySignupService.writeOff(orderId,adultNum1,childNum1);
    }

     /**
     * 生成核销二维码
     * @param orderId 订单id
     * @return
     */
    @RequestMapping(value = "writeOff", method = RequestMethod.GET)
    @ApiOperation(value = "生成核销二维码", notes = "生成核销二维码")
    @ApiImplicitParam(value = "报名信息id", name = "orderId", required = true)
    public Result hexiao(Integer orderId) {
        return activitySignupService.hexiao(orderId);
    }

     /**
     * 获取核销二维码
     * @param orderId 订单id
     * @return
     */
    @RequestMapping(value = "getEWM", method = RequestMethod.GET)
    @ApiOperation(value = "获取核销二维码", notes = "获取核销二维码")
    @ApiImplicitParam(value = "报名信息id", name = "orderId", required = true)
    public Result getEWM(Integer orderId) {
        return activitySignupService.getEWM(orderId);
    }


     /**
     * 通过核销码查看订单信息
     * @param
     * @return
     */
    @RequestMapping(value = "analysisHXM", method = RequestMethod.GET)
    @ApiOperation(value = "通过核销码查看订单信息", notes = "通过核销码查看订单信息")
    @ApiImplicitParam(value = "核销码", name = "hxm", required = true)
    public Result analysisHXM(String hxm,@ApiIgnore TokenUser tokenUser) {
        return activitySignupService.analysisHXM(hxm,tokenUser.getCustomerId());
    }


     /**
     * 后台查询所有订单
     * @param
     * @return
     */
    @RequestMapping(value = "getAllOrder", method = RequestMethod.GET)
    @ApiOperation(value = "后台查询所有订单", notes = "后台查询所有订单")
    @ApiImplicitParam(value = "状态", name = "status")
    public Result getAllOrder(int status,@ApiIgnore TokenUser tokenUser) {
        return activitySignupService.getAllOrder(status,tokenUser);
    }




}