package com.weilai9.web.controller.wechat;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.weilai9.common.utils.obs.ObsUtil;
import com.weilai9.common.utils.wechat.ApiStatus;
import com.weilai9.common.utils.wechat.ReturnUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author xujinhao
 */
@RestController
@RequestMapping("/obs")
@Api(tags = "华为OBS测试")
public class ObsController {


    @PostMapping("/test")
    @ApiOperation(value = "上传文件，返回全部路径")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileName", value = "文件名,请保证文件名唯一性",required = true),
            @ApiImplicitParam(name = "file", value = "文件",required = true,type = "MultipartFile")
    })
    public Map<String, Object> uploadFile(String fileName, MultipartFile file) {
        if(StrUtil.isBlank(fileName)){
            fileName = file.getName()+"_"+ DateUtil.now()+".jpg";
        }
        String url = ObsUtil.uploadFile( fileName, file);
        Map<String,Object> data = new HashMap<>(16);
        data.put("url",url);
        return ReturnUtil.returnMap(ApiStatus.SUCCESS,data);
    }


    @PostMapping("/test2")
    @ApiOperation(value = "上传文件,返回文件名")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileName", value = "文件名,请保证文件名唯一性",required = true),
            @ApiImplicitParam(name = "file", value = "文件",required = true,type = "MultipartFile")
    })
    public Map<String, Object> uploadFile2(String fileName, MultipartFile file) {
        if(StrUtil.isBlank(fileName)){
            fileName = file.getName()+"_"+ DateUtil.now()+".jpg";
        }
        String url = ObsUtil.uploadFile( fileName, file);
        url = url.substring(69);
        Map<String,Object> data = new HashMap<>(16);
        data.put("url",url);
        return ReturnUtil.returnMap(ApiStatus.SUCCESS,data);
    }




}
