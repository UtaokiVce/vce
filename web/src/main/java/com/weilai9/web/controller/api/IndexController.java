package com.weilai9.web.controller.api;

import com.weilai9.common.constant.Result;
import com.weilai9.service.admin.IndexService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("index")
@Api(tags = "首页相关接口")
public class IndexController {

    @Resource
    private IndexService indexService;

    @ApiOperation(value = "首页banner", notes = "首页banner")
    @RequestMapping(value = "getBanner",method = RequestMethod.GET)
    public Result getBanner(){
        return indexService.getBanner();
    }

}
