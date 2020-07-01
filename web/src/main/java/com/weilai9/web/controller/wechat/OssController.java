package com.weilai9.web.controller.wechat;

import com.weilai9.common.constant.Result;
import com.weilai9.common.utils.oss.OssUpload;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Api(tags = "阿里OSS测试")
@RequestMapping("/oss")
public class OssController {

    /**
     * 阿里OSS测试
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ApiOperation(value = "OSS上传文件")
    @ApiImplicitParam(name = "file", value = "文件", required = true, type = "MultipartFile")
    public String uploadBlog(MultipartFile file) {
        String url = OssUpload.uploadFile(file);
        return url;
    }
}