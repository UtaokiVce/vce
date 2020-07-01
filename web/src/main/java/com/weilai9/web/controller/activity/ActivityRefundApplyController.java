package com.weilai9.web.controller.activity;

import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.ActivityRefundApply;
import com.weilai9.service.admin.ActivityRefundApplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * (RefundApply)表控制层
 *
 * @author makejava
 * @since 2020-04-26 17:56:36
 */
@RestController
@RequestMapping("refundApply")
@Api(tags = "活动退款审核相关接口")
public class ActivityRefundApplyController {
    /**
     * 服务对象
     */
    @Resource
    private ActivityRefundApplyService refundApplyService;

    /**
     * 添加退款申请
     *
     * @param refundApply
     * @return
     */
    @ApiOperation(value = "添加退款申请", notes = "添加退款申请")
    @RequestMapping(value = "addRefundApply", method = RequestMethod.POST)
    public Result addRefundApply(@RequestBody ActivityRefundApply refundApply) {

        return refundApplyService.addRefundApply(refundApply);

    }


    /**
     * 申请状态修改
     *
     * @param refundApply
     * @return
     */
    @ApiOperation(value = "修改申请状态", notes = "修改申请状态")
    @RequestMapping(value = "updateRefundApply", method = RequestMethod.PUT)
    public Result updateRefundApply(@RequestBody ActivityRefundApply refundApply) {

        return refundApplyService.updateRefundApply(refundApply);

    }

    /**
     * 审核驳回原因
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "审核驳回原因", notes = "审核驳回原因")
    @ApiImplicitParam(value = "申请id",name = "id",required = true)
    @RequestMapping(value = "getDeclinedReason", method = RequestMethod.GET)
    public Result getDeclinedReason(Integer id) {

        return refundApplyService.getDeclinedReason(id);

    }

    /**
     * 查看核销码
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "查看核销码", notes = "查看核销码")
    @ApiImplicitParam(value = "退款申请id",name = "id",required = true)
    @RequestMapping(value = "getMarketCode", method = RequestMethod.GET)
    public Result getMarketCode(Integer id) {

        return refundApplyService.getMarketCode(id);

    }

    /**
     * 查询所有取消原因
     *
     * @return
     */
    @ApiOperation(value = "查询所有取消原因", notes = "查询所有取消原因")
    @RequestMapping(value = "getReasonDict", method = RequestMethod.GET)
    public Result getReasonDict() {

        return refundApplyService.getReasonDict();

    }




}