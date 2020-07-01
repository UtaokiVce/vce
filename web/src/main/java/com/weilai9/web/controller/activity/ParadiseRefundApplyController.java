package com.weilai9.web.controller.activity;

import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.ParadiseRefundApply;
import com.weilai9.service.admin.ParadiseRefundApplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 乐园退款审核表(ParadiseRefundApply)表控制层
 *
 * @author makejava
 * @since 2020-05-08 10:09:44
 */
@RestController
@RequestMapping("paradiseRefundApply")
@Api(tags = "乐园退款审核相关接口")
public class ParadiseRefundApplyController {
    /**
     * 服务对象
     */
    @Resource
    private ParadiseRefundApplyService paradiseRefundApplyService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    /*@GetMapping("selectOne")
    public ParadiseRefundApply selectOne(Integer id) {
        return this.paradiseRefundApplyService.queryById(id);
    }*/

    /**
     * 添加退款审核
     *
     * @param obj
     * @return
     */
    @ApiOperation(value = "添加退款审核", notes = "添加退款审核")
    @RequestMapping(value = "addRefund", method = RequestMethod.POST)
    public Result addRefund(@RequestBody ParadiseRefundApply obj) {
        return paradiseRefundApplyService.addRefund(obj);
    }

    /**
     * 退款审核修改
     *
     * @param obj
     * @return
     */
    @ApiOperation(value = "退款审核修改", notes = "退款审核修改")
    @RequestMapping(value = "updateRefund", method = RequestMethod.PUT)
    public Result updateRefund(@RequestBody ParadiseRefundApply obj) {
        return paradiseRefundApplyService.updateRefund(obj);
    }


    /**
     * 查看核销码
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "查看核销码", notes = "查看核销码")
    @ApiImplicitParam(value = "退款申请id", name = "id", required = true)
    @RequestMapping(value = "getMarketCode", method = RequestMethod.GET)
    public Result getMarketCode(Integer id) {

        return paradiseRefundApplyService.getMarketCode(id);

    }


}