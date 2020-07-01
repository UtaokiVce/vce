package com.weilai9.service.api.impl;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weilai9.common.utils.wechat.ApiStatus;
import com.weilai9.common.utils.wechat.JobUtil;
import com.weilai9.common.utils.wechat.RedisHandle;
import com.weilai9.common.utils.wechat.ReturnUtil;
import com.weilai9.dao.entity.*;
import com.weilai9.dao.mapper.*;
import com.weilai9.dao.vo.wechat.QzMonthVo;
import com.weilai9.service.api.CustomerqinzuanService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xjh
 * @since 2020-06-03
 */
@Service
public class CustomerqinzuanServiceImpl extends ServiceImpl<CustomerqinzuanMapper, Customerqinzuan> implements CustomerqinzuanService {
    @Resource
    CustomerqinzuanMapper customerqinzuanMapper;
    @Resource
    RedisHandle redisHandle;
    @Resource
    HttpServletRequest request;
    @Resource
    QzmonthlogMapper qzmonthlogMapper;
    @Resource
    QzlogMapper qzlogMapper;
    @Resource
    ActivitySignupDao activitySignupDao;
    @Resource
    ActivityMsgDao activityMsgDao;
    /**
     * 团队今日亲钻
     * @return
     */
    @Override
    public Map<String, Object> teamTodayInfo() {
        WxUser user = getWxUser();
        if (user == null) {
            return ReturnUtil.returnMap(ApiStatus.ACCOUNT_ERROR);
        }
        String cid = user.getCustomerId();
        List<Customerqinzuan> customerqinzuanList = customerqinzuanMapper.teamList(cid, 0, 999999);
        Map<String,Object> data = new HashMap<>(16);
        data.put("team_today_qinzuan_List",customerqinzuanList);
        return ReturnUtil.returnMap(ApiStatus.SUCCESS,data);
    }

    @Override
    public Map<String, Object> teamMonthInfo() {
        WxUser user = getWxUser();
        if (user == null) {
            return ReturnUtil.returnMap(ApiStatus.ACCOUNT_ERROR);
        }
        String cid = user.getCustomerId();
        List<Customerqinzuan> customerqinzuanList = customerqinzuanMapper.teamList(cid, 0, 999999);
        Map<String,Object> data = new HashMap<>(16);
        data.put("team_month_qinzuan_List",customerqinzuanList);
        return ReturnUtil.returnMap(ApiStatus.SUCCESS,data);
    }

    /**
     * 团队年度总亲钻
     * @param year
     * @return
     */
    @Override
    public Map<String, Object> teamTotalInfo(Integer year) {
        WxUser user = getWxUser();
        if (user == null) {
            return ReturnUtil.returnMap(ApiStatus.ACCOUNT_ERROR);
        }
        String cid = user.getCustomerId();
        QueryWrapper<Qzmonthlog> wrapper = new QueryWrapper<>();
        wrapper.eq("customerid",cid).eq("year",year);
        List<Qzmonthlog> qzmonthlogs = qzmonthlogMapper.selectList(wrapper);
        BigDecimal i = new BigDecimal(0.00);
        for (Qzmonthlog qzmonthlog : qzmonthlogs) {
            BigDecimal personqz = qzmonthlog.getTeamqz();
            i=personqz.add(i);
        }
        Map<String,Object> data = new HashMap<>(16);
        data.put("qzmonthlogs",qzmonthlogs);
        data.put("totleQz",i);
        return ReturnUtil.returnMap(ApiStatus.SUCCESS,data);
    }

    /**
     * 亲钻关系列表
     * @return
     */
    @Override
    public Map<String, Object> qzList() {
        Map<String,Object> data = new HashMap<>(16);
        List<Customerqinzuan> customerqinzuanList = customerqinzuanMapper.selectList(null);
        data.put("customerqinzuanList",customerqinzuanList);
        return ReturnUtil.returnMap(ApiStatus.SUCCESS,data);
    }

    /**
     * 个人本月待结算亲钻
     * @return
     */
    @Override
    public Map<String, Object> monthWaitInfo() {
        WxUser user = getWxUser();
        if (user == null) {
            return ReturnUtil.returnMap(ApiStatus.ACCOUNT_ERROR);
        }
        String cid = user.getCustomerId();
        DateTime now = DateUtil.date();
        DateTime beginOfMonth = DateUtil.beginOfMonth(now);
        QueryWrapper<Qzlog> wrapper = new QueryWrapper();
        wrapper.eq("customerid",cid).between("subdate",beginOfMonth,now).orderByDesc("subdate");
        List<Qzlog> qzlogList = qzlogMapper.selectList(wrapper);
        BigDecimal perWaitQz=new BigDecimal(0.00);
        if (qzlogList.size()>0){
            Qzlog qzlog = qzlogList.get(0);
            perWaitQz = qzlog.getBeforepersonwait();
        }
        Map<String,Object> data = new HashMap<>(16);
        data.put("monthWaitValue",perWaitQz);
        List<QzMonthVo> list  = new ArrayList<>();
        for (Qzlog qzlog : qzlogList) {
            Integer ordersn = qzlog.getOrdersn();
            // todo 根据订单类型查询对应订单，这先写死
            if (true){
                ActivitySignup activitySignup = activitySignupDao.selectById(ordersn);
                Integer activityId = activitySignup.getActivityId();
                ActivityMsg activityMsg = activityMsgDao.selectById(activityId);
                String activityIntroduction = activityMsg.getActivityIntroduction();
                qzlog.setOrderName(activityIntroduction);
                qzlog.setOrderTime(activitySignup.getCreateTime());
            }
        }
        data.put("qzlogList",qzlogList);
        return ReturnUtil.returnMap(ApiStatus.SUCCESS,data);
    }


    private WxUser getWxUser() {
        String token = JobUtil.getAuthorizationToken(request);
        return (WxUser) redisHandle.get(token);
    }
}

