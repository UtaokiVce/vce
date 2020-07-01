package com.weilai9.web.controller.activity;


import com.weilai9.service.admin.ActivityPayService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * (ActivityPay)表控制层
 *
 * @author makejava
 * @since 2020-05-29 11:06:43
 */
@RestController
//@Api(tags = "活动支付相关接口")
@RequestMapping("activityPay")
public class ActivityPayController {
    /**
     * 服务对象
     */
    @Resource
    private ActivityPayService activityPayService;




}