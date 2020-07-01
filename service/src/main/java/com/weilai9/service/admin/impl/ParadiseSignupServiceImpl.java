package com.weilai9.service.admin.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
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
import com.weilai9.service.admin.ParadiseSignupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 乐园报名信息表(ParadiseSignup)表服务实现类
 *
 * @author makejava
 * @since 2020-05-08 10:09:44
 */
@Service("paradiseSignupService")
public class ParadiseSignupServiceImpl extends ServiceImpl<ParadiseSignupDao, ParadiseSignup> implements ParadiseSignupService {
    @Resource
    private ParadiseSignupDao paradiseSignupDao;

    @Resource
    private ParadiseDao paradiseDao;

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


    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public ParadiseSignup queryById(Integer id) {
        return this.paradiseSignupDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<ParadiseSignup> queryAllByLimit(int offset, int limit) {
        return this.paradiseSignupDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param paradiseSignup 实例对象
     * @return 实例对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ParadiseSignup insert(ParadiseSignup paradiseSignup) {
        this.paradiseSignupDao.insert(paradiseSignup);
        return paradiseSignup;
    }

    /**
     * 修改数据
     *
     * @param paradiseSignup 实例对象
     * @return 实例对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ParadiseSignup update(ParadiseSignup paradiseSignup) {
        this.paradiseSignupDao.update(paradiseSignup);
        return this.queryById(paradiseSignup.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(Integer id) {
        return this.paradiseSignupDao.deleteById(id) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addSignup(ParadiseSignup obj) {

        String token = JobUtil.getAuthorizationToken(request);
        if (StrUtil.isBlank(token)) {
            return Result.Error("token无效");
        }
        WxUser user = (WxUser) redisHandle.get(token);
        if (user == null) {
            return Result.Error("token无效");
        }
        String customerId = user.getCustomerId();

        obj.setUserId(Integer.parseInt(customerId));
        obj.setStatus(1);
        obj.setCreateTime(new Date());
        obj.setUpdateTime(new Date());

        //添加成功，给个核销码
        String hexiaoCode = ((long) ((Math.random() * 10) * Math.pow(10, 11))) + "";
        obj.setMarketCode(hexiaoCode);

        // 存放在二维码中的内容
        // 嵌入二维码的图片路径
        // 生成的二维码的路径及名称
        String file = hexiaoCode + ".jpg";
        String destPath = "D:/111111111111111/" + file;
        //生成二维码
        try {
            QRCodeUtil.encode("02" + hexiaoCode, null, destPath, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //url 图片路径
        String url = ObsUtil.uploadFile(file, Path2FileUtil.getMulFileByPath(destPath));
        obj.setMarketUrl(url);
        //save(obj);
        baseMapper.insert(obj);
        //返回订单基本信息
        Map<String, Object> map = baseMapper.getSignInfo(obj.getId());
        return new Result(map);


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result writeOff(Integer orderId, Integer adultNum, Integer childNum) {

        ParadiseSignup obj = baseMapper.queryById(orderId);
        //判断订单状态
        if (obj.getStatus() == 2) {
            return Result.Error("该订单已核销！");
        }
        Paradise paradise = paradiseDao.selectById(obj.getParadiseId());

        BigDecimal adultQZ;
        BigDecimal childQZ;
        BigDecimal allQZ;

        //全部核销
        if (adultNum == null && childNum == null) {
            obj.setStatus(2);
            obj.setAdultMarketNum(obj.getAdultNum());
            obj.setChildMarketNum(obj.getChildNum());
            obj.setMarketTime(new Date());
            updateById(obj);
            //加钻
            adultQZ = paradise.getAdultDiamond().multiply(new BigDecimal(obj.getAdultNum()));
            childQZ = paradise.getChildDiamond().multiply(new BigDecimal(obj.getChildNum()));
            allQZ = adultQZ.add(childQZ);
            QzUtil.addQz(customerqinzuanMapper, obj.getUserId(), allQZ, 2, obj.getId(), 1);
            return Result.OK("核销成功");
        } else {
            //部分核销
            obj.setStatus(5);
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
            }
            obj.setMarketTime(new Date());
            updateById(obj);
            //加钻
            adultQZ = paradise.getAdultDiamond().multiply(new BigDecimal(adultNum));
            childQZ = paradise.getChildDiamond().multiply(new BigDecimal(childNum));
            allQZ = adultQZ.add(childQZ);
            QzUtil.addQz(customerqinzuanMapper, obj.getUserId(), allQZ, 2, obj.getId(), 1);
            return Result.OK("核销成功");
        }

    }

    @Override
    public Result getAvailNumber(Date date, Integer paradiseId) {
        //查询当天已预约人数
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date1 = sdf.format(date);
        int num = baseMapper.getNum(date1, paradiseId);
        //查询可预约人数
        Paradise paradise = paradiseDao.queryById(paradiseId);
        Object availNum = paradise.getAvailableDailyNumber() - num;
        return new Result(availNum);
    }

    @Override
    public Result appointment(Integer paradiseId) {

        Map<String, Object> map = baseMapper.appointment(paradiseId);
        return new Result(map);
    }

    @Override
    public Map<String, Object> getDetail(Integer actSignId) {
        //查询单个订单对象
        //ParadiseSignup obj = baseMapper.queryById(actSignId);

        Map<String, Object> map = baseMapper.getDetails(actSignId);
        return map;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result ticketChanging(Integer orderId, String newDate) {

        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sd.parse(newDate);
            getById(orderId).setForwardDate(date);
            return Result.OK("改签成功");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return Result.Error("改签失败");
    }

    @Override
    public Result hexiao(Integer orderId) {
        ParadiseSignup obj = getById(orderId);
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
        ParadiseSignup obj = getById(orderId);
        String marketUrl = obj.getMarketUrl();
        return new Result(marketUrl);
    }

    @Override
    public Result analysisHXM(String hxm, Long customerId) {
        ParadiseSignup obj = getOne(new QueryWrapper<ParadiseSignup>().eq("market_code", hxm).last(" limit 1"));
        if (obj == null) {
            return Result.Error("没有查询到核销码！");
        }

        //当前商家
        Store store = storeMapper.selectOne(new QueryWrapper<Store>().eq("customerId", customerId).last(" limit 1"));
        //乐园id
        Integer paraId = obj.getParadiseId();
        Paradise paradise = paradiseDao.selectById(paraId);
        //判断核销码是否是该商家下的
        if (store.getStoreId() != paradise.getStoreId()) {
            return Result.Error("不是该商家的订单！");
        }
        //活动id
        //Integer activityId = activitySignup.getActivityId();
        //ActivityMsg activityMsg = activityMsgDao.selectById(activityId);
        Map<String, Object> map = new HashMap(16);
        //根据订单id查询基本信息
        Map<String, Object> info = baseMapper.getDetails(obj.getId());

        Result result = new Result(info);
        result.setMsg("leyuan");
        return result;
    }

    @Override
    public Result getAll(int status, TokenUser tokenUser) {
        Long customerId;
        List<Integer> idlist = null;
        //管理员
        if (tokenUser != null) {
            customerId = tokenUser.getCustomerId();
            Store store = storeMapper.selectOne(new QueryWrapper<Store>().eq("customerId", customerId).last(" limit 1"));
            if (store != null) {
                idlist = paradiseDao.selectIdList(store.getStoreId());
            }

        }

        List<Map<String, Object>> all = paradiseSignupDao.getAllAct(status, idlist);

        return new Result(all);

    }


}