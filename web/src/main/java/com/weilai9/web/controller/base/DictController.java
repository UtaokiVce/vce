package com.weilai9.web.controller.base;

import com.weilai9.common.config.annotation.MustLogin;
import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.vo.base.AddDictVO;
import com.weilai9.dao.vo.base.UpdateDictVO;
import com.weilai9.service.base.SysDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

@Api(tags = "字典相关接口")
@RestController
@RequestMapping("/dict")
public class DictController {
    @Resource
    SysDictService sysDictService;

    @MustLogin(isAdmin = true)
    @ApiOperation(value = "字典列表", notes = "不分页,字典列表")
    @GetMapping("/list")
    public Result dictList() {
        return new Result(sysDictService.list());
    }

    @MustLogin(isAdmin = true)
    @ApiOperation(value = "新增字典", notes = "新增字典")
    @PostMapping("/add")
    public Result addDict(@RequestBody AddDictVO addDictVO, @ApiIgnore TokenUser tokenUser) {
        return sysDictService.addDict(addDictVO, tokenUser.getCustomerId());
    }

    @MustLogin(isAdmin = true)
    @ApiOperation(value = "修改字典", notes = "修改字典")
    @PostMapping("/update")
    public Result updateDict(@RequestBody UpdateDictVO updateDictVO, @ApiIgnore TokenUser tokenUser) {
        return sysDictService.updateDict(updateDictVO, tokenUser.getCustomerId());
    }


    @MustLogin(isAdmin = true)
    @ApiOperation(value = "修改字典", notes = "修改字典")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "字典id", name = "dictId", required = true)
    })
    @PostMapping("/delete")
    public Result deleteDict(Integer dictId) {
        return sysDictService.deleteDict(dictId);
    }

    @ApiOperation(value = "根据父级字段code获取所有子级", notes = "根据父级字段code获取所有子级")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "字典code", name = "dictcode", required = true)
    })
    @PostMapping("/getDictListByparentCode")
    public Result getDictListByparentCode(String dictcode) {
        return sysDictService.getDictListByparentCode(dictcode);
    }
}
