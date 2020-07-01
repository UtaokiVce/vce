package com.weilai9.service.admin.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weilai9.common.config.auth.JwtTokenUtil;
import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.common.utils.wechat.JobUtil;
import com.weilai9.common.utils.wechat.RedisHandle;
import com.weilai9.dao.entity.*;
import com.weilai9.dao.mapper.*;
import com.weilai9.dao.vo.activity.AddActivityMsgVO;
import com.weilai9.dao.vo.activity.UpdateActivityMsgVO;
import com.weilai9.dao.vo.wechat.WxOrderVo;
import com.weilai9.service.admin.ActivityMsgService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * (ActivityMsg)表服务实现类
 *
 * @author makejava
 * @since 2020-04-26 17:56:00
 */
@Service("activityMsgService")
public class ActivityMsgServiceImpl extends ServiceImpl<ActivityMsgDao, ActivityMsg> implements ActivityMsgService {
    @Resource
    private ActivityMsgDao activityMsgDao;
    @Resource
    private ActivitySignupDao activitySignupDao;

    @Resource
    private SysDictMapper sysDictMapper;

    @Resource
    private ActivityPayDao activityPayDao;

    @Resource
    private StoreMapper storeMapper;
    @Resource
    private CustomerMapper customerMapper;

    @Resource
    private HttpServletRequest request;
    @Resource
    private RedisHandle redisHandle;
    @Resource
    JwtTokenUtil jwtTokenUtil;


    public static final BigDecimal STORE_GET_PROPORTION = new BigDecimal("0.2");


    @Override
    public Result getAll(int page, int size, String introductory) {

        IPage page1 = new Page(page, size);
        QueryWrapper<ActivityMsg> wrapper = new QueryWrapper<>();
        wrapper.like(!StrUtil.isEmpty(introductory), "activity_introduction", introductory);
        //按创建时间降序排序
        wrapper.orderBy(true,false,"create_time");
        IPage result = page(page1,wrapper);
        List<ActivityMsg> records = result.getRecords();
        if(records==null){
            return null;
        }
        records.forEach(activityMsg -> {
            //结束时间比当前时间小，设为已结束
            if(activityMsg.getActivityEndTime().getTime()<System.currentTimeMillis() &&
                    (activityMsg.getStatus()==1 || activityMsg.getStatus()==7)){
                activityMsg.setStatus(4);
                baseMapper.updateById(activityMsg);
            }else if(activityMsg.getActivityStartTime().getTime()>=System.currentTimeMillis() && activityMsg.getStatus()==4){
                activityMsg.setStatus(1);
                baseMapper.updateById(activityMsg);
            }
        });

        return new Result(result);
    }

    @Override
    public Result getActByType(String activityType) {

        QueryWrapper<ActivityMsg> wrapper = new QueryWrapper<>();
        wrapper.select("id", "activity_type", "activity_introduction", "pic_url", "activity_start_time", "site",
                "longitude", "latitude", "apply_num", "all_num", "status");
        if (!StringUtils.isEmpty(activityType)) {
            wrapper.eq("activity_type", activityType);
        }
        //排除待审核和审核失败的数据
        wrapper.ne("status", 6).ne("status", 0);
        List<Map<String, Object>> maps = listMaps(wrapper);
        maps.forEach(map->{
            String id = map.get("id").toString();
            int currNum = baseMapper.selectCurrNum(Integer.parseInt(id));
            map.put("currNum",currNum);
        });
        //List<Map<String,Object>> lsit = baseMapper.selectByType(activityType);

        return new Result(maps);
    }

    @Override
    public Result getActType() {
        List<Map<String, Object>> result = activityMsgDao.getActType();
        return new Result(result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addActivity(AddActivityMsgVO vo, Long userId) {

        Store store = storeMapper.selectOne(new QueryWrapper<Store>().eq("customerId", userId));
        if(store==null){
            return Result.Error("该用户下无商家！");
        }

        Customer customer = customerMapper.selectById(userId);
        if(customer.getCustomerType() != 2 ||customer.getCustomerType() != 1){
            return Result.Error("只有门店用户才能新增活动");
        }

        ActivityMsg activityMsg = new ActivityMsg();
        BeanUtil.copyProperties(vo, activityMsg);
        activityMsg.setStoreId(userId.intValue());
        activityMsg.setCreateTime(new Date());
        activityMsg.setUpdateTime(new Date());
        //状态默认为1
        activityMsg.setStatus(1);
        activityMsg.setStoreId(store.getStoreId());
        //设置亲钻奖励
        ActivityMsg activityMsg2 = count(activityMsg);
        //设置商家实得金额
        activityMsg2.setAdultActualMoney(activityMsg2.getAdultJs().multiply(STORE_GET_PROPORTION).setScale(2, BigDecimal.ROUND_DOWN));
        activityMsg2.setChildActualMoney(activityMsg2.getChildJs().multiply(STORE_GET_PROPORTION).setScale(2, BigDecimal.ROUND_DOWN));
        boolean save = save(activityMsg2);
        if (!save) {
            return Result.Error("发布失败");
        } else {
            return Result.OK("发布成功");
        }
    }

    /**
     * 亲钻转换金额=（报名费用-结算费用-一级经销奖励）/0.3
     *
     * @return
     */
    public ActivityMsg count(ActivityMsg obj) {

        BigDecimal adultQinZuan = (obj.getAdultCost().subtract(obj.getAdultJs()).subtract(obj.getAdultFirstSellAward())).divide(new BigDecimal("0.3"), 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal childQinZuan = (obj.getChildCost().subtract(obj.getChildJs()).subtract(obj.getChildFirstSellAward())).divide(new BigDecimal("0.3"), 2, BigDecimal.ROUND_HALF_UP);
//        double adultQinZuan = (obj.getAdultCost()-obj.getAdultJs()-obj.getAdultFirstSellAward())/0.3;
//        double childQinZuan = (obj.getChildCost()-obj.getChildJs()-obj.getChildFirstSellAward())/0.3;
        //设置亲钻奖励
        obj.setAdultDiamond(adultQinZuan);
        obj.setChildDiamond(childQinZuan);
        return obj;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delActivity(Integer activityId) {
        boolean b = removeById(activityId);
        if (b) {
            return Result.OK("删除成功");
        } else {
            return Result.Error("删除失败");
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateActivity(UpdateActivityMsgVO vo) {
        ActivityMsg activityMsg = new ActivityMsg();
        BeanUtil.copyProperties(vo, activityMsg);
        activityMsg.setUpdateTime(new Date());
        boolean b = updateById(activityMsg);
        if (b) {
            return Result.OK("修改成功");
        } else {
            return Result.Error("修改失败");
        }
    }

    @Override
    public Result getActByStore(int page,int size,Long userId) {

        Page<ActivityMsg> page1 = new Page(page,size);

        Store store = storeMapper.selectOne(new QueryWrapper<Store>().eq("customerId", userId).last(" limit 1"));
        if(store == null){
            return Result.Error("未查询到商家");
        }
        QueryWrapper<ActivityMsg> wrapper = new QueryWrapper();
        //指定查询字段
        wrapper.select("adult_cost", "activity_start_time", "child_cost", "all_num",
                "site", "activity_introduction", "id", "pic_url", "status").eq("store_id", store.getStoreId());
        IPage<Map<String, Object>> mapIPage = pageMaps(page1, wrapper);

        return new Result(mapIPage);
    }

    @Override
    public Result getActById(Integer actId) {

        QueryWrapper<ActivityMsg> wrapper = new QueryWrapper<>();
        //指定查询字段
        wrapper.select("id", "activity_introduction", "pic_url", "apply_num", "all_num", "adult_cost", "status", "child_cost", "adult_first_sell_award", "child_first_sell_award",
                "adult_diamond", "child_diamond", "sponsor", "activity_start_time", "activity_end_time", "site", "activity_details", "notice", "longitude", "latitude");
        Map<String, Object> map = getMap(wrapper.eq("id", actId));

        //查询已核销人数
        int count = activityMsgDao.getHeXiaoCount(actId);
        //查询已报名人数
        int currNum = baseMapper.selectCurrNum(actId);
        map.put("currNum", currNum);
        map.put("heXiaoNum", count);
        //查询报名信息下的头像（最多4个）
        List<String> headList = activityMsgDao.getHead(actId);
        map.put("headList", headList);
        return new Result(map);
    }

    @Override
    public Result getActRegistration(Integer current, Integer size, Integer actId) {
        IPage page = new Page(null == current ? 1 : current, null == size ? 10 : size);


        IPage<Map> customerIPage = baseMapper.getUserList(page, actId);
        /*List<Map<String,Object>> list = activityMsgDao.getUserList(actId);
        page.setRecords(list);*/

        return new Result(customerIPage);
    }

    @Override
    public Result again(Integer actId) {

      //  ActivityMsg result = getOne(new QueryWrapper<ActivityMsg>().select(ActivityMsg.class, obj -> !obj.getColumn().equals("id")));
        ActivityMsg result = getOne(new QueryWrapper<ActivityMsg>().eq("id", actId).last(" limit 1"));
        return new Result(result);
    }

    @Override
    public Result getRefusedReason(Integer id) {
        Map<String, Object> map = getMap(new QueryWrapper<ActivityMsg>().select("refused_reason").eq("id", id));
        return new Result(map);
    }

    @Override
    public Result getActByUser(Integer status) {

        String token = JobUtil.getAuthorizationToken(request);
        if (StrUtil.isBlank(token)) {
            return Result.Error("token无效");
        }
        WxUser user = (WxUser) redisHandle.get(token);
        String userId = user.getCustomerId();

        List<Map<String, Object>> result = baseMapper.getMyAct(userId, status);
        return new Result(result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addActType(SysDict obj) {
        obj.setDictPid(1);
        obj.setDictLevel(2);
        obj.setDictStatus(1);
        obj.setUpdateTime(new Date());
        obj.setAddTime(new Date());
        int insert = sysDictMapper.insert(obj);
        if (insert == 1) {
            return Result.OK("添加成功");
        } else {
            return Result.Error("添加失败");
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result actPay(Integer orderId) {

        //当前订单
        ActivitySignup activitySignup = activitySignupDao.selectById(orderId);
        WxOrderVo wxOrderVo = new WxOrderVo();
        //订单号
        wxOrderVo.setOutTradeNo(activitySignup.getId().toString() + "activity");
        //金额  分
        double v = activitySignup.getPracticalMoney().doubleValue() * 100;
        wxOrderVo.setTotalFee(new Double(v).intValue());
        //商品描述
        wxOrderVo.setBody(activitySignup.getName() + "的活动订单");
        //备注
        //商品（活动）id
        wxOrderVo.setProductId(activitySignup.getActivityId().toString());
        //添加信息到支付表
        ActivityPay activityPay = new ActivityPay();

        activityPay.setMoney(wxOrderVo.getTotalFee());
        activityPay.setDescribe(wxOrderVo.getBody());
        activityPay.setDescribe(wxOrderVo.getAttach());
        activityPay.setStatus(0);
        activityPay.setType(1);
        activityPay.setOrderId(wxOrderVo.getOutTradeNo());
        activityPay.setCreatetime(new Date());
        activityPay.setActid(activitySignup.getActivityId());
        activityPayDao.insert(activityPay);

        return new Result(wxOrderVo);
    }

    @Override
    public Result getActIndex() {
        List<Map<String, Object>> list = baseMapper.getActIndex();
        return new Result(list);
    }

    @Override
    public Result switchAct(Integer id, Integer status) {
        ActivityMsg activityMsg = getById(id);
        if (activityMsg == null) {
            return Result.Error("未查询到活动");
        }
        //报名中的活动不允许操作
        if(activityMsg.getActivityStartTime().getTime()<=System.currentTimeMillis()){
            return Result.Error("活动正在进行中，不允许操作！");
        }
        activityMsg.setStatus(status);
        updateById(activityMsg);
        return new Result(activityMsg);
    }


}