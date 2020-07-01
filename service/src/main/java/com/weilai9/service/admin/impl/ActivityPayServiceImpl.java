package com.weilai9.service.admin.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weilai9.dao.entity.ActivityPay;
import com.weilai9.dao.mapper.ActivityPayDao;
import com.weilai9.service.admin.ActivityPayService;
import org.springframework.stereotype.Service;

/**
 * (ActivityPay)表服务实现类
 *
 * @author makejava
 * @since 2020-05-29 11:06:43
 */
@Service("activityPayService")
public class ActivityPayServiceImpl extends ServiceImpl<ActivityPayDao, ActivityPay> implements ActivityPayService {

}