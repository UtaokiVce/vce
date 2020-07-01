package com.weilai9.service.admin.impl;


import cn.hutool.core.date.DateTime;
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
import com.weilai9.service.admin.CustomerrelationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xjh
 * @since 2020-05-20
 */
@Service
public class CustomerrelationServiceImpl extends ServiceImpl<CustomerrelationMapper, Customerrelation> implements CustomerrelationService {
    @Resource
    CustomerqinzuanMapper customerqinzuanMapper;
    @Resource
    RedisHandle redisHandle;
    @Resource
    HttpServletRequest request;
    @Resource
    QinzuanrateMapper qinzuanrateMapper;
    @Resource
    QzmonthlogMapper qzmonthlogMapper;
    @Resource
    QzlogMapper qzlogMapper;
    @Resource
    ParadiseSignupDao  paradiseSignupMapper;
    @Resource
    ParadiseDao paradiseMapper;
    @Resource
    ActivitySignupDao activitySignupMapper;
    @Resource
    ActivityMsgDao  activityMsgDao;
    @Override
    public Map<String, Object> monthList(Integer pageno, Integer pagesize) {

        WxUser user = getWxUser();
        if (user == null) {
            return ReturnUtil.returnMap(ApiStatus.ACCOUNT_ERROR);
        }
        String customerId = user.getCustomerId();
        Map<String,Object> data = new HashMap<>(16);
        QueryWrapper<Qzmonthlog> wrapper = new QueryWrapper<>();
        wrapper.eq("customerid",customerId)
                .orderByDesc("year")
                .orderByDesc("month")
                .last("limit "+pageno*pagesize+","+pagesize);
        List<Qzmonthlog> qzmonthlogs = qzmonthlogMapper.selectList(wrapper);
        data.put("qzmonthlogList",qzmonthlogs);

        return ReturnUtil.returnMap(ApiStatus.SUCCESS,data);
    }

    @Override
    public Map<String, Object> homeInfo() {
        WxUser user = getWxUser();
        if (user == null) {
            return ReturnUtil.returnMap(ApiStatus.ACCOUNT_ERROR);
        }
        String cid = user.getCustomerId();
        QueryWrapper<Customerqinzuan> qw = new QueryWrapper<>();
        qw.eq("customerid",cid);
        Customerqinzuan cqz =customerqinzuanMapper.selectOne(qw);
        if(cqz==null){
            return ReturnUtil.returnMap("未找到该用户。");
        }
        Map<String,Object> data = new HashMap<>(16);
        //基础用户信息
        data.put("base",cqz);
        //亲钻收益信息， 个人预计收益
        QzMonthVo qzMonthVo = customerqinzuanMapper.pIncome(cid);
        if (qzMonthVo==null){
            data.put("pIncome",0);
        }else {
            data.put("pIncome",qzMonthVo.getRealmoney());
        }
        //亲钻收益信息，团队预计收益
        QzMonthVo teamqzVo = customerqinzuanMapper.tIncome(cid);
        if (teamqzVo==null){
            data.put("tIncome",0);
        }else {
            data.put("tIncome",teamqzVo.getRealmoney());
        }
        BigDecimal i = new BigDecimal(0.01);
        //todo 个人累计收益
        BigDecimal persontotalvalue = cqz.getPersontotalvalue();
        BigDecimal teamtotalvalue = cqz.getTeamtotalvalue();
        //下属团队亲钻
        BigDecimal subtract = teamtotalvalue.subtract(persontotalvalue);
        QueryWrapper<Qinzuanrate> subtractWrapper = new QueryWrapper<>();
        subtractWrapper.ge("endvalue",subtract).le("startvalue",subtract);
        BigDecimal rate = new BigDecimal(0);
        Qinzuanrate qinzuanrate1 = qinzuanrateMapper.selectOne(subtractWrapper);
        if (qinzuanrate1!=null){
            rate = qinzuanrate1.getRate();
        }
        //下级团队累计收益
        BigDecimal multiply = rate.multiply(subtract).multiply(i);

        QueryWrapper<Qinzuanrate> teamWrapper = new QueryWrapper<>();
        teamWrapper.ge("endvalue",teamtotalvalue).le("startvalue",teamtotalvalue);
        BigDecimal teamrate = new BigDecimal(0);
        Qinzuanrate qinzuanrate2 = qinzuanrateMapper.selectOne(teamWrapper);
        if (qinzuanrateMapper.selectOne(teamWrapper)!=null){
            teamrate=qinzuanrate2.getRate();
        }
        BigDecimal teammultiply = teamrate.multiply(teamtotalvalue).multiply(i);
        //个人累计收益
        BigDecimal personTotalMoney = teammultiply.subtract(multiply);
        data.put("personTotalMoney",personTotalMoney.doubleValue());
        //距离下一等级
        QueryWrapper<Qinzuanrate> wrapper = new QueryWrapper<>();
        wrapper.ge("endvalue",cqz.getTeamcheckvalue()).le("startvalue",cqz.getTeamcheckvalue());
        Qinzuanrate qinzuanrate = qinzuanrateMapper.selectOne(wrapper);
        if (qinzuanrate==null){
            qinzuanrate = qinzuanrateMapper.selectList(null).get(0);
        }
        data.put("nextLevel",qinzuanrate);
        //距离最高等级
        QueryWrapper<Qinzuanrate> wrapper2 = new QueryWrapper<>();
        wrapper2.orderByDesc("endvalue").last("limit 1");
        Qinzuanrate highLevel = qinzuanrateMapper.selectOne(wrapper2);
        data.put("highLevel",highLevel);
        return ReturnUtil.returnMap(ApiStatus.SUCCESS,data);
    }

    @Override
    public Map<String, Object> monthInfo(Integer year, Integer month, Integer pageno, Integer pagesize) {
        WxUser wxUser = getWxUser();
        if (wxUser==null){
            return ReturnUtil.returnMap(ApiStatus.TOKEN_ERROR);
        }
        String cid = wxUser.getCustomerId();
        Map<String,Object> data = new HashMap<>(16);
        QueryWrapper<Qzlog> wrapper = new QueryWrapper<>();
        wrapper.eq("customerid",cid)
                .eq("MONTH (subdate)",month)
                .eq("YEAR ( subdate )",year)
                .orderByDesc("subdate").last("limit "+pageno*pagesize+","+pagesize);;
        List<Qzlog> qzlogList = qzlogMapper.selectList(wrapper);
        data.put("qzlogList",qzlogList);
        return ReturnUtil.returnMap(ApiStatus.SUCCESS,data);
    }

    @Override
    public Map<String, Object> teamList(Integer pageno, Integer pagesize) {
        WxUser wxUser = getWxUser();
        if (wxUser==null){
            return ReturnUtil.returnMap(ApiStatus.TOKEN_ERROR);
        }
        String cid = wxUser.getCustomerId();
        Map<String,Object> data = new HashMap<>(16);
        List<Customerqinzuan> customerqinzuanList = customerqinzuanMapper.teamList(cid, pageno*pagesize, pagesize);
        data.put("customerqinzuanList",customerqinzuanList);
        return ReturnUtil.returnMap(ApiStatus.SUCCESS,data);
    }

    @Override
    public Map<String, Object> todyaInfo(Integer type) {
        // todo 订单类型后续更新
        type =2;
        WxUser user = getWxUser();
        if (user == null) {
            return ReturnUtil.returnMap(ApiStatus.ACCOUNT_ERROR);
        }
        String cid = user.getCustomerId();
        Map<String,Object> data = new HashMap<>(16);
        QueryWrapper<Qzlog> wrapper = new QueryWrapper<>();
        DateTime date = DateUtil.date();
        DateTime beginOfDay = DateUtil.beginOfDay(date);
        DateTime dateTime = DateUtil.endOfDay(date);
        wrapper.eq("customerid",cid).ge("subdate",beginOfDay).le("subdate",dateTime);
        List<Qzlog> qzmonthlogs = qzlogMapper.selectList(wrapper);
        //乐园订单
        if (type==1){
            for (Qzlog qzmonthlog : qzmonthlogs) {
                Integer ordersn = qzmonthlog.getOrdersn();
                ParadiseSignup paradiseSignup = paradiseSignupMapper.selectById(ordersn);
                Integer paradiseId = paradiseSignup.getParadiseId();
                Paradise paradise = paradiseMapper.selectById(paradiseId);
                String name = paradise.getName();
                qzmonthlog.setOrderName(name).setOrderTime(paradiseSignup.getCreateTime());
            }
        }
        //活动订单
        if (type==2){
            for (Qzlog qzmonthlog : qzmonthlogs) {
                Integer ordersn = qzmonthlog.getOrdersn();
                ActivitySignup activitySignup = activitySignupMapper.selectById(ordersn);
                Integer activityId = activitySignup.getActivityId();
                ActivityMsg activityMsg = activityMsgDao.selectById(activityId);
                String activityIntroduction = activityMsg.getActivityIntroduction();
                qzmonthlog.setOrderName(activityIntroduction);
                qzmonthlog.setOrderTime(activitySignup.getCreateTime());
            }
        }
        if (type==3){

        }
        //亲钻收益信息， 个人预计收益
        QzMonthVo qzMonthVo = customerqinzuanMapper.pIncome(cid);
        if (qzMonthVo==null){
            data.put("pIncome","");
        }else {
            data.put("pIncome",qzMonthVo.getRealmoney());
        }
        //亲钻收益信息，团队预计收益
        QzMonthVo teamqzVo = customerqinzuanMapper.tIncome(cid);
        if (teamqzVo==null){
            data.put("tIncome","");
        }else {
            data.put("tIncome",teamqzVo.getRealmoney());
        }
        data.put("qzmonthlogs",qzmonthlogs);

        return ReturnUtil.returnMap(ApiStatus.SUCCESS,data);
    }

    /**
     * 个人本月亲钻
     * @param type
     * @return
     */
    @Override
    public Map<String, Object> monthInfo(Integer type) {
        // todo 订单类型后续更新
        type =2;
        WxUser user = getWxUser();
        if (user == null) {
            return ReturnUtil.returnMap(ApiStatus.ACCOUNT_ERROR);
        }
        String cid = user.getCustomerId();
        Map<String,Object> data = new HashMap<>(16);
        QueryWrapper<Qzlog> wrapper = new QueryWrapper<>();
        DateTime date = DateUtil.date();
        DateTime beginOfDay = DateUtil.beginOfMonth(date);
        DateTime endOfDay = DateUtil.date();
        wrapper.eq("customerid",cid).ge("subdate",beginOfDay).le("subdate",endOfDay);
        List<Qzlog> qzmonthlogs = qzlogMapper.selectList(wrapper);
        //乐园订单
        if (type==1){
            for (Qzlog qzmonthlog : qzmonthlogs) {
                Integer ordersn = qzmonthlog.getOrdersn();
                ParadiseSignup paradiseSignup = paradiseSignupMapper.selectById(ordersn);
                Integer paradiseId = paradiseSignup.getParadiseId();
                Paradise paradise = paradiseMapper.selectById(paradiseId);
                String name = paradise.getName();
                qzmonthlog.setOrderName(name).setOrderTime(paradiseSignup.getCreateTime());
            }
        }
        //活动订单
        if (type==2){
            for (Qzlog qzmonthlog : qzmonthlogs) {
                Integer ordersn = qzmonthlog.getOrdersn();
                ActivitySignup activitySignup = activitySignupMapper.selectById(ordersn);
                Integer activityId = activitySignup.getActivityId();
                ActivityMsg activityMsg = activityMsgDao.selectById(activityId);
                String activityIntroduction = activityMsg.getActivityIntroduction();
                qzmonthlog.setOrderName(activityIntroduction);
                qzmonthlog.setOrderTime(activitySignup.getCreateTime());
            }
        }
        if (type==3){

        }
        //亲钻收益信息， 个人预计收益
        QzMonthVo qzMonthVo = customerqinzuanMapper.pIncome(cid);
        if (qzMonthVo==null){
            data.put("pIncome","");
        }else {
            data.put("pIncome",qzMonthVo.getRealmoney());
        }
        //亲钻收益信息，团队预计收益
        QzMonthVo teamqzVo = customerqinzuanMapper.tIncome(cid);
        if (teamqzVo==null){
            data.put("tIncome","");
        }else {
            data.put("tIncome",teamqzVo.getRealmoney());
        }
        data.put("qzmonthlogs",qzmonthlogs);

        return ReturnUtil.returnMap(ApiStatus.SUCCESS,data);
    }

    /**
     * 个人当年累计亲钻
     * @param year
     * @return
     */
    @Override
    public Map<String, Object> totalInfo(Integer year) {
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
            BigDecimal personqz = qzmonthlog.getPersonqz();
            i=personqz.add(i);
        }
        Map<String,Object> data = new HashMap<>(16);
        data.put("qzmonthlogs",qzmonthlogs);
        data.put("totleQz",i);
        return ReturnUtil.returnMap(ApiStatus.SUCCESS,data);
    }

    private WxUser getWxUser() {
        String token = JobUtil.getAuthorizationToken(request);
        return (WxUser) redisHandle.get(token);
    }

}
