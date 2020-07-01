package com.weilai9.web.controller.active;


import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.vo.activity.GroupBuyingVO;
import com.weilai9.service.admin.GroupBuyingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;


@RestController
@Api(tags = "团购相关接口")
@RequestMapping("shippingAddress")
public class GroupBuyingController {

    @Resource
    private GroupBuyingService service;

    /**
     * 团购
     *
     * @return
     */
    @ApiOperation(value = "团购",notes = "发起/加入团购，parentId不为空，则表示为参团，parentId为空表示为开团")
    @RequestMapping(value = "addBuy",method = RequestMethod.POST)
    public Result addBuy(@RequestBody GroupBuyingVO vo,@ApiIgnore TokenUser tokenUser){

        return service.addBuy(vo,tokenUser.getCustomerId());
    }


    /**
     * 团购下单
     *
     * @return
     */
    @ApiOperation(value = "团购下单",notes = "发起/加入团购，parentId不为空，则表示为参团，parentId为空表示为开团")
    @RequestMapping(value = "addOrder",method = RequestMethod.POST)
    public Result addOrder(@RequestBody GroupBuyingVO vo,@ApiIgnore TokenUser tokenUser){

        return service.addOrder(vo,tokenUser.getCustomerId());
    }


    /**
     * 获取团购列表
     *
     * @return
     */
    @ApiOperation(value = "团购列表",notes = "获取团购列表")
    @RequestMapping(value = "getGroupList",method = RequestMethod.GET)
    //public Result addBuy(@RequestBody GroupBuyingVO vo,@ApiIgnore TokenUser tokenUser){
    public Result getGroupList(Integer page,Integer limit){

        return service.getGroupList(page, limit);
    }





}