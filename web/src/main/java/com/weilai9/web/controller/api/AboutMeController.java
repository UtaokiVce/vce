package com.weilai9.web.controller.api;

import com.weilai9.common.constant.Result;
import com.weilai9.service.api.AboutMeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户端“我的”
 */
@RestController
@RequestMapping("aboutMe")
@Api(tags = "我的首页相关接口")
public class AboutMeController {

    @Autowired
    private AboutMeService service;

    /**
     * 获取我的页面信息
     * @return
     */
    @ApiOperation(value = "获取我的页面信息", notes = "获取我的页面信息")
    @RequestMapping(value = "getMyInfo",method = RequestMethod.GET)
    public Result aboutMe(Integer userId){
        Result result = service.getInfo(userId);
        return result;
    }

}
