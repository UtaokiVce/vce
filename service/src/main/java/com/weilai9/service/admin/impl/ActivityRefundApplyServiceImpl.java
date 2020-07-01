package com.weilai9.service.admin.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.ActivityRefundApply;
import com.weilai9.dao.mapper.ActivityRefundApplyDao;
import com.weilai9.service.admin.ActivityRefundApplyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (RefundApply)表服务实现类
 *
 * @author makejava
 * @since 2020-04-26 17:56:36
 */
@Service("refundApplyService")
public class ActivityRefundApplyServiceImpl extends ServiceImpl<ActivityRefundApplyDao, ActivityRefundApply> implements ActivityRefundApplyService {
    @Resource
    private ActivityRefundApplyDao refundApplyDao;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addRefundApply(ActivityRefundApply obj) {
        obj.setStatus(1);

        //int num = baseMapper.addRefundApply(refundApply);
        boolean b = save(obj);
        if (b) {
            //回显当前申请
            //查询活动信息，报名信息，订单信息
            Map<String,Object> result = baseMapper.getInfo(obj.getId());
            return new Result(result);
            //return Result.OK("申请成功");
        } else {
            return Result.Error("申请失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateRefundApply(ActivityRefundApply refundApply) {
        boolean b = updateById(refundApply);
        if (b) {
            //回显当前申请
            //查询活动信息，报名信息，订单信息
            Map<String,Object> result = baseMapper.getInfo(refundApply.getId());
            return new Result(result);
        } else {
            return Result.Error("操作失败");
        }
    }

    @Override
    public Result getDeclinedReason(Integer id) {
        //订单相关信息
        Map<String,Object> map = baseMapper.getInfo(id);
        Map<String, Object> map1 = getMap(new QueryWrapper<ActivityRefundApply>().select("declined_reason").eq("id", id));
        Map<String, Object> result = new HashMap();
        if(map1 == null || map1.size()==0){
            return Result.Error("操作失败");
        }
        result.putAll(map);
        result.putAll(map1);
        return new Result(result);
    }

    @Override
    public Result getMarketCode(Integer id) {
        String marketCode = baseMapper.getMarketCode(id);
        return new Result(marketCode);
    }

    @Override
    public Result getReasonDict() {

        List<Map<String,Object>> list = baseMapper.getReasonDict();

        return new Result(list);
    }
}