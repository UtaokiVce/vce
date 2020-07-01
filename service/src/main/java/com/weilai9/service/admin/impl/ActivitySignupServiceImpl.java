package com.weilai9.service.admin.impl;


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.common.utils.obs.ObsUtil;
import com.weilai9.common.utils.qrCode.Path2FileUtil;
import com.weilai9.common.utils.qrCode.QRCodeUtil;
import com.weilai9.common.utils.wechat.JobUtil;
import com.weilai9.common.utils.wechat.QzUtil;
import com.weilai9.common.utils.wechat.RedisHandle;
import com.weilai9.dao.entity.*;
import com.weilai9.dao.mapper.*;
import com.weilai9.service.admin.ActivitySignupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * (ActivitySignup)表服务实现类
 *
 * @author makejava
 * @since 2020-04-26 17:56:24
 */
@Service("activitySignupService")
public class ActivitySignupServiceImpl extends ServiceImpl<ActivitySignupDao, ActivitySignup> implements ActivitySignupService {
    @Resource
    private ActivitySignupDao activitySignupDao;

    @Resource
    private ActivityMsgDao activityMsgDao;

    @Resource
    private StoreMapper storeMapper;

    @Resource
    private HttpServletRequest request;
    @Resource
    private RedisHandle redisHandle;

    @Resource
    private CustomerqinzuanMapper customerqinzuanMapper;
  @Resource
    private CustomerMapper customerMapper;


    @Override
    public List<Map<String, Object>> getMyAct(String status) {
        String token = JobUtil.getAuthorizationToken(request);
        if (StrUtil.isBlank(token)) {
            return null;
        }
        WxUser user = (WxUser) redisHandle.get(token);
        String userId = user.getCustomerId();


        return activitySignupDao.getMyAct(Integer.valueOf(userId), status);
    }

    @Override
    public Map<String, Object> getOne(String actSignId) {

        //查询单个订单对象
        ActivitySignup obj = activitySignupDao.getObject(actSignId);
        //订单状态
        Integer status = obj.getStatus();
        //待核销
        if (status == 1) {
            return activitySignupDao.getOne(actSignId);
            //待审核
        } else if (status == 3) {
            return activitySignupDao.getChecking(actSignId);
        } else {
            Map<String, Object> map = getMap(new QueryWrapper<ActivitySignup>().eq("id", actSignId));
            ActivityMsg act = activityMsgDao.selectById(map.get("activity_id").toString());
            map.put("activity_start_time",act.getActivityStartTime());
            map.put("activity_end_time",act.getActivityEndTime());
            map.put("site",act.getSite());
            map.put("totalNum",obj.getAdultNum()+obj.getChildNum());
            return map;

        }


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addOrder(ActivitySignup activitySignup) {
        String token = JobUtil.getAuthorizationToken(request);
        if (StrUtil.isBlank(token)) {
            return Result.Error("token无效");
        }
        WxUser user = (WxUser) redisHandle.get(token);
        String customerId = user.getCustomerId();

        activitySignup.setUpdateTime(new Date());
        activitySignup.setCreateTime(new Date());
        //随机生成一个12位的核销码
        activitySignup.setMarketCode(((long) ((Math.random() * 10) * Math.pow(10, 11))) + "");

        //添加成功，给个核销码
        String hexiaoCode = ((long) ((Math.random() * 10) * Math.pow(10, 11))) + "";

        activitySignup.setMarketCode(hexiaoCode);

        // 存放在二维码中的内容
        // 嵌入二维码的图片路径
//        String imgPath = "D:/JAVA/9a504fc2d5628535a675deb89bef76c6a6ef6395.png";
        // 生成的二维码的路径及名称
        String file = activitySignup.getMarketCode() + ".jpg";
        String destPath = "D:/111111111111111/" + file;
        //生成二维码
        try {
            QRCodeUtil.encode("01" + hexiaoCode, null, destPath, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //url 图片路径
        String url = ObsUtil.uploadFile(file, Path2FileUtil.getMulFileByPath(destPath));
        activitySignup.setMarketUrl(url);
        activitySignup.setStatus(1);
        activitySignup.setUserId(Integer.valueOf(customerId));
        boolean b = save(activitySignup);
        if (b) {
            //回调   修改订单状态，加总钻，亲钻类型，用户id
            //计算亲钻值
            ActivityMsg activityMsg = activityMsgDao.selectById(activitySignup.getActivityId());
            BigDecimal childQZ = activityMsg.getChildDiamond().multiply(new BigDecimal(activitySignup.getChildNum()));
            BigDecimal adultQZ = activityMsg.getAdultDiamond().multiply(new BigDecimal(activitySignup.getAdultNum()));
            BigDecimal qz = childQZ.add(adultQZ);
            QzUtil.addQz(customerqinzuanMapper, Integer.parseInt(customerId), qz, 2, activitySignup.getId(), 1);
            return new Result(activitySignup);
        } else {
            return Result.Error("预约失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateOrderStatus(Integer id, Integer status) {

        //查询订单
        ActivitySignup obj = getById(id);
        obj.setStatus(status);
        boolean b = updateById(obj);
        if (b) {
            //回显当前订单信息
            Map<String, Object> map = baseMapper.getInfo(obj.getId());
            return new Result(map);
        } else {
            return Result.Error("操作失败");
        }
    }

    /**
     * 预约
     *
     * @param activityId
     * @return
     */
    @Override
    public Result appointment(Integer activityId) {
        Map<String, Object> map = baseMapper.appointment(activityId);
        return new Result(map);
    }

    /**
     * 核销
     *
     * @param orderId
     * @param adultNum
     * @param childNum
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result writeOff(Integer orderId, Integer adultNum, Integer childNum) {
        ActivitySignup obj = getById(orderId);
        if (obj == null) {
            return Result.Error("该活动不存在！");
        }

        Integer activityId = obj.getActivityId();
        ActivityMsg activityMsg = activityMsgDao.selectById(activityId);

        //判断订单状态
        if (obj.getStatus() == 2) {
            return Result.Error("该订单已核销！");
        }

        BigDecimal adultQZ;
        BigDecimal childQZ;
        BigDecimal allQZ;

        //全部核销
        if (adultNum == null && childNum == null) {
            obj.setStatus(2);
            obj.setAdultMarketNum(obj.getAdultNum());
            obj.setChildMarketNum(obj.getChildNum());
            updateById(obj);
            //亲钻值
            adultQZ = activityMsg.getAdultDiamond().multiply(new BigDecimal(obj.getAdultNum()));
            childQZ = activityMsg.getChildDiamond().multiply(new BigDecimal(obj.getChildNum()));
            allQZ = adultQZ.add(childQZ);
            QzUtil.addQz(customerqinzuanMapper, obj.getUserId(), allQZ, 2, obj.getId(), 1);
            return Result.OK("核销成功");
        } else {

            if (adultNum == null) {
                adultNum = 0;
            }
            if (childNum == null) {
                childNum = 0;
            }
            //成人、儿童已核销人数
            int adNum = obj.getAdultMarketNum() == null ? 0 : obj.getAdultMarketNum();
            int chNum = obj.getChildMarketNum() == null ? 0 : obj.getChildMarketNum();
            obj.setAdultMarketNum(adNum + adultNum);
            obj.setChildMarketNum(chNum + childNum);
            //已经全部核销
            if (obj.getAdultNum() == obj.getAdultMarketNum() && obj.getChildNum() == obj.getChildMarketNum()) {
                obj.setStatus(2);
            } else {
                //部分核销
                obj.setStatus(5);
            }
            obj.setMarketTime(new Date());
            updateById(obj);

            //亲钻值
            adultQZ = activityMsg.getAdultDiamond().multiply(new BigDecimal(adultNum));
            childQZ = activityMsg.getChildDiamond().multiply(new BigDecimal(childNum));
            allQZ = adultQZ.add(childQZ);
            QzUtil.addQz(customerqinzuanMapper, obj.getUserId(), allQZ, 2, obj.getId(), 1);
            return Result.OK("核销成功");
        }

    }

    @Override
    public Result hexiao(Integer orderId) {
        ActivitySignup obj = getById(orderId);
        //核销码
        String text = obj.getMarketCode();
        // 生成的二维码的路径及名称
        String destPath = "D:/market/" + text + ".png";
        //生成二维码
        try {
            QRCodeUtil.encode(text, null, destPath, true);
            return Result.OK("生成成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Result getEWM(Integer orderId) {
        ActivitySignup obj = getById(orderId);
        String marketUrl = obj.getMarketUrl();
        return new Result(marketUrl);
    }

    @Override
    public Result analysisHXM(String hxm, Long customerId) {
        ActivitySignup activitySignup = getOne(new QueryWrapper<ActivitySignup>().eq("market_code", hxm).last(" limit 1"));
        if (activitySignup == null) {
            return Result.Error("没有查询到核销码！");
        }
        //当前商家
        Store store = storeMapper.selectOne(new QueryWrapper<Store>().eq("customerId", customerId).last(" limit 1"));
        //活动id
        Integer activityId = activitySignup.getActivityId();
        ActivityMsg activityMsg = activityMsgDao.selectById(activityId);
        //判断核销码是否是该商家下的
        if (store.getStoreId() != activityMsg.getStoreId()) {
            return Result.Error("不是该商家的订单！");
        }

        Map<String, Object> map = new HashMap(16);
        //根据订单id查询基本信息
        Map<String, Object> info = baseMapper.getInfo(activitySignup.getId());
        Result result = new Result(info);
        result.setMsg("huodong");
        return result;
    }

    @Override
    public Result getAllOrder(int status,TokenUser tokenUser) {
        Long customerId;
        List<Integer> idlist = null;
        //管理员
        if(tokenUser != null){
            customerId = tokenUser.getCustomerId();
            Store store = storeMapper.selectOne(new QueryWrapper<Store>().eq("customerId", customerId).last(" limit 1"));
            if(store != null){
                idlist = activityMsgDao.selectIdList(store.getStoreId());
            }

        }

        List<Map<String, Object>> allAct = activitySignupDao.getAllAct(status,idlist);

        return new Result(allAct);
    }


}