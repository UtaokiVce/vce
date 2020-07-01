package com.weilai9.service.admin.impl;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weilai9.common.constant.Result;
import com.weilai9.common.utils.wechat.JobUtil;
import com.weilai9.common.utils.wechat.RedisHandle;
import com.weilai9.dao.entity.ActivityCollect;
import com.weilai9.dao.entity.ActivityMsg;
import com.weilai9.dao.entity.Paradise;
import com.weilai9.dao.entity.WxUser;
import com.weilai9.dao.mapper.ActivityCollectDao;
import com.weilai9.service.admin.ActivityCollectService;
import com.weilai9.service.admin.ActivityMsgService;
import com.weilai9.service.admin.ParadiseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * (ActivityCollect)表服务实现类
 *
 * @author makejava
 * @since 2020-04-26 17:55:31
 */
@Service("activityCollectService")
public class ActivityCollectServiceImpl extends ServiceImpl<ActivityCollectDao, ActivityCollect> implements ActivityCollectService {
    @Resource
    private ActivityCollectDao activityCollectDao;

    @Resource
    private ActivityMsgService activityMsgService;

    @Resource
    private ParadiseService paradiseService;

    @Resource
    private HttpServletRequest request;
    @Resource
    private RedisHandle redisHandle;



    @Override
    public Result getColl( int type) {

        String token = JobUtil.getAuthorizationToken(request);
        if(StrUtil.isBlank(token)){
            return Result.Error("token无效");
        }
        WxUser user = (WxUser) redisHandle.get(token);
        String customerId = user.getCustomerId();
        List<Map<String, Object>> coll = baseMapper.getColl(Integer.valueOf(customerId),type);
        return new Result(coll);
    }

    @Override
    public Result addColl(ActivityCollect activityCollect) {
        //int num = baseMapper.addColl(userId, actId);
        String token = JobUtil.getAuthorizationToken(request);
        if(StrUtil.isBlank(token)){
            return Result.Error("token无效");
        }
        WxUser user = (WxUser) redisHandle.get(token);
        if(user == null){
            return Result.Error("token无效");
        }
        String userId = user.getCustomerId();
        activityCollect.setUserId(Integer.parseInt(userId));
        //查询是否已经收藏
        List<ActivityCollect> list = list(new QueryWrapper<ActivityCollect>().eq("type", activityCollect.getType())
                .eq("user_id", activityCollect.getUserId()).eq("activity_id", activityCollect.getActivityId()));
        if(list.size()>0){
            return Result.Error(2,"已存在收藏");
        }
        activityCollect.setCreateTime(new Date());
        boolean b = save(activityCollect);
        if (b) {
            return Result.OK("添加成功");
        } else {
            return Result.Error("添加失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delColl(ActivityCollect obj) {

        String token = JobUtil.getAuthorizationToken(request);
        if(StrUtil.isBlank(token)){
            return Result.Error("token无效");
        }
        WxUser user = (WxUser) redisHandle.get(token);
        String customerId = user.getCustomerId();

        //删除这条收藏
        boolean remove = remove(new QueryWrapper<ActivityCollect>().eq("user_id", customerId)
                .eq("activity_id", obj.getActivityId()).eq("type",obj.getType()));
        if (remove) {
            return Result.Error("删除成功");
        } else {
            return Result.OK("删除失败");
        }

    }

    @Override
    public Result getOneColl(int id, Integer type) {
        //活动
        if(type ==1){
            ActivityMsg byId = activityMsgService.getById(id);
            return new Result(byId);
            //乐园
        }else if(type ==2){
            Paradise byId = paradiseService.getById(id);
            return new Result(byId);
        }else {
            return Result.Error("操作失败！");
        }

    }
}