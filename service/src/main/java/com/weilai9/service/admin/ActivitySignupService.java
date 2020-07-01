package com.weilai9.service.admin;



import com.baomidou.mybatisplus.extension.service.IService;
import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.ActivitySignup;

import java.util.List;
import java.util.Map;

/**
 * (ActivitySignup)表服务接口
 *
 * @author makejava
 * @since 2020-04-26 17:56:24
 */
public interface ActivitySignupService extends IService<ActivitySignup> {


    List<Map<String, Object>> getMyAct(String status);

    Map<String, Object> getOne(String actSignId);

    Result addOrder(ActivitySignup activitySignup);

    Result updateOrderStatus(Integer id,Integer status);

    Result appointment(Integer activityId);

    Result writeOff(Integer orderId, Integer adultNum, Integer childNum);

    Result hexiao(Integer orderId);

    Result getEWM(Integer orderId);

    Result analysisHXM(String hxm,Long customerId);

    Result getAllOrder(int status,TokenUser tokenUser);
}