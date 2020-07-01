package com.weilai9.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.ActivityRefundApply;

/**
 * (RefundApply)表服务接口
 *
 * @author makejava
 * @since 2020-04-26 17:56:36
 */
public interface ActivityRefundApplyService extends IService<ActivityRefundApply> {


    Result addRefundApply(ActivityRefundApply refundApply);

    Result updateRefundApply(ActivityRefundApply refundApply);

    Result getDeclinedReason(Integer id);

    Result getMarketCode(Integer id);

    Result getReasonDict();
}