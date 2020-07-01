package com.weilai9.web.controller.active;


import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.common.utils.wechat.JobUtil;
import com.weilai9.common.utils.wechat.RedisHandle;
import com.weilai9.dao.entity.ShippingAddress;
import com.weilai9.dao.entity.WxUser;
import com.weilai9.dao.vo.activity.AddShippingAddressVO;
import com.weilai9.dao.vo.activity.UpdateShippingAddressVO;
import com.weilai9.service.admin.ShippingAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * (ShippingAddress)表控制层
 * 收货地址
 * @author makejava
 * @since 2020-05-13 11:35:25
 */
@RestController
@Api(tags = "用户配送地址相关接口")
@RequestMapping("shippingAddress")
public class ShippingAddressController {

    /**
     * 服务对象
     */
    @Resource
    private ShippingAddressService shippingAddressService;


    @ApiOperation(value = "查询单个",notes = "查询单个")
    @RequestMapping(value = "selectOne",method = RequestMethod.GET)
    public Result selectOne(Integer id) {

        return shippingAddressService.get(id);
    }


    /**
     * 查询我的配送地址列表
     * @param
     * @return
     */
    @ApiOperation(value = "查询配送地址列表",notes = "查询我的配送地址列表")
    @RequestMapping(value = "getMyAddress",method = RequestMethod.GET)
    public Result getMyAddress() {

        return shippingAddressService.getMyAddress();
    }

    /**
     * 查询我的默认地址
     * @param
     * @return
     */
    @ApiOperation(value = "查询我的默认地址",notes = "查询我的默认地址")
    @RequestMapping(value = "getAddDefault",method = RequestMethod.GET)
    @ApiImplicitParam(value = "id", name = "id")
    public Result getAddDefault(Integer id) {

        return shippingAddressService.getAddDefault(id);
    }



    /**
     * 添加配送地址
     * @return
     */
    @ApiOperation(value = "添加配送地址", notes = "添加配送地址")
    @RequestMapping(value = "addAddress",method = RequestMethod.POST)
    public Result addAddress(@RequestBody AddShippingAddressVO vo){

        return shippingAddressService.addAddress(vo);
    }

   /**
     * 修改配送地址
     * @return
     */
    @ApiOperation(value = "修改配送地址", notes = "修改配送地址")
    @RequestMapping(value = "updateAddress",method = RequestMethod.POST)
    public Result updateAddress(@RequestBody UpdateShippingAddressVO vo){

        return shippingAddressService.updateAddress(vo);
    }


    /**
     * 删除配送地址
     * @return
     */
    @ApiOperation(value = "删除配送地址", notes = "删除配送地址")
    @RequestMapping(value = "deleteAddress",method = RequestMethod.DELETE)
    public Result deleteAddress(Integer id){
        return shippingAddressService.deleteAddress(id);
    }




}