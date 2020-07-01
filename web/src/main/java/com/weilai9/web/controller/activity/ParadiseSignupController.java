package com.weilai9.web.controller.activity;

import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.ParadiseSignup;
import com.weilai9.service.admin.ParadiseSignupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * 乐园报名信息表(ParadiseSignup)表控制层
 *
 * @author makejava
 * @since 2020-05-08 10:09:44
 */
@RestController
@RequestMapping("paradiseSignup")
@Api(tags = "乐园报名信息相关接口")
public class ParadiseSignupController {
    /**
     * 服务对象
     */
    @Resource
    private ParadiseSignupService paradiseSignupService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
   /* @GetMapping("selectOne")
    public ParadiseSignup selectOne(Integer id) {
        return this.paradiseSignupService.queryById(id);
    }
*/

    /**
     * 添加预约信息
     *
     * @param obj
     * @return
     */
    @RequestMapping(value = "addSignup", method = RequestMethod.POST)
    @ApiOperation(value = "添加预约信息", notes = "添加预约信息")
    public Result addSignup(@RequestBody ParadiseSignup obj) {
        return this.paradiseSignupService.addSignup(obj);
    }


    /**
     * 核销成功
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
        return paradiseSignupService.writeOff(orderId,adultNum1,childNum1);
    }


    /**
     * 根据日期获取当前可预约人数
     * 日期 乐园id
     *
     * @return
     */
    @RequestMapping(value = "getAvailNumber", method = RequestMethod.GET)
    @ApiOperation(value = "根据日期获取当前可预约人数", notes = "根据日期获取当前可预约人数")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "日期，格式为“yyyy-MM-dd", name = "date", required = true),
            @ApiImplicitParam(value = "乐园id", name = "paradiseId", required = true)
    })
    public Result getAvailNumber(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date, Integer paradiseId) {
        Result result = paradiseSignupService.getAvailNumber(date, paradiseId);
        return result;
    }


    /**
     * 我要预约
     * 乐园id
     *
     * @return
     */
    @RequestMapping(value = "appointment", method = RequestMethod.GET)
    @ApiOperation(value = "我要预约", notes = "我要预约")
    @ApiImplicitParam(value = "乐园id", name = "paradiseId", required = true)
    public Result appointment( Integer paradiseId) {
        return paradiseSignupService.appointment( paradiseId);
    }


    /**
     * 查询单个订单
     * @return
     */
    @ApiOperation(value = "查询单个订单", notes = "查询单个订单")
    @ApiImplicitParam(value = "订单id", name = "signId", required = true)
    @RequestMapping(value = "/getOne", method = RequestMethod.GET)
    public Result getOne(Integer signId) {

        Map<String, Object> result = paradiseSignupService.getDetail(signId);

        return new Result(result);
    }


    //改签
    @RequestMapping(value = "gaiqian", method = RequestMethod.GET)
    @ApiOperation(value = "改签", notes = "改签")
    @ApiImplicitParam(value = "报名信息id", name = "orderId", required = true)
    public Result ticketChanging(Integer orderId,String newDate) {
        return paradiseSignupService.ticketChanging(orderId,newDate);
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
        return paradiseSignupService.hexiao(orderId);
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
        return paradiseSignupService.getEWM(orderId);
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
        return paradiseSignupService.analysisHXM(hxm,tokenUser.getCustomerId());
    }

 /**
     * 后台查询全部
     * @param
     * @return
     */
    @RequestMapping(value = "getAll", method = RequestMethod.GET)
    @ApiOperation(value = "后台查询全部", notes = "后台查询全部")
    @ApiImplicitParam(value = "状态", name = "status", required = true)
    public Result getAll(int status,@ApiIgnore TokenUser tokenUser) {
        return paradiseSignupService.getAll(status,tokenUser);
    }



}