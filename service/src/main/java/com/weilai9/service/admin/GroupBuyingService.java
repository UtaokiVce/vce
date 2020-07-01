package com.weilai9.service.admin;


import com.weilai9.common.constant.Result;
import com.weilai9.dao.vo.activity.GroupBuyingVO;

public interface GroupBuyingService{


    Result addBuy(GroupBuyingVO vo,Long userId);

    Result getGroupList(Integer page, Integer limit);

    Result addOrder(GroupBuyingVO vo, Long customerId);
}