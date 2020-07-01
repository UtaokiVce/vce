package com.weilai9.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.ActivityCollect;

/**
 * (ActivityCollect)表服务接口
 *
 * @author makejava
 * @since 2020-04-26 17:55:31
 */
public interface ActivityCollectService extends IService<ActivityCollect> {


    Result getColl(int type);

    Result addColl(ActivityCollect activityCollect);

    Result delColl(ActivityCollect obj);

    Result getOneColl(int acttvityId, Integer type);
}