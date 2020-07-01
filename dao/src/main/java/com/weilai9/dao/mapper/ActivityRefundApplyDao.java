package com.weilai9.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weilai9.dao.entity.ActivityRefundApply;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * (RefundApply)表数据库访问层
 *
 * @author makejava
 * @since 2020-04-26 17:56:36
 */

//@Mapper
public interface ActivityRefundApplyDao extends BaseMapper<ActivityRefundApply> {


    int addRefundApply(ActivityRefundApply refundApply);

    Map<String, Object> getInfo(@Param(value = "id") Integer id);

    String getMarketCode(@Param(value = "id") Integer id);

    List<Map<String, Object>> getReasonDict();
}