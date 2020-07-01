package com.weilai9.service.admin.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weilai9.common.config.auth.AccessInterceptor;
import com.weilai9.common.config.auth.JwtTokenUtil;
import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.common.utils.redis.RedisUtil;
import com.weilai9.common.utils.wechat.JobUtil;
import com.weilai9.common.utils.wechat.RedisHandle;
import com.weilai9.dao.entity.*;
import com.weilai9.dao.mapper.*;
import com.weilai9.dao.vo.activity.AddParadiseVO;
import com.weilai9.dao.vo.wechat.WxOrderVo;
import com.weilai9.service.admin.ParadiseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * 乐园信息表(Paradise)表服务实现类
 *
 * @author makejava
 * @since 2020-05-08 10:09:42
 */
@Service("paradiseService")
public class ParadiseServiceImpl extends ServiceImpl<ParadiseDao, Paradise> implements ParadiseService {
    @Resource
    private ParadiseDao paradiseDao;

    @Resource
    private ParadiseSignupDao paradiseSignupDao;

    @Resource
    private HttpServletRequest request;
    @Resource
    private RedisHandle redisHandle;
    @Resource
    private ActivityPayDao activityPayDao;
    @Resource
    private StoreMapper storeMapper;
    @Resource
    private CustomerMapper customerMapper;
    @Resource
    JwtTokenUtil jwtTokenUtil;

    public static final BigDecimal STORE_GET_PROPORTION = new BigDecimal("0.2");

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Paradise queryById(Integer id) {
        return this.paradiseDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<Paradise> queryAllByLimit(int offset, int limit) {
        return this.paradiseDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param paradise 实例对象
     * @return 实例对象
     */
    @Override
    public Paradise insert(Paradise paradise) {
        this.paradiseDao.insert(paradise);
        return paradise;
    }

    /**
     * 修改数据
     *
     * @param paradise 实例对象
     * @return 实例对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Paradise update(Paradise paradise) {
        this.paradiseDao.update(paradise);
        return this.queryById(paradise.getId());
    }


    @Override
    public Result getAll(Integer current, Integer size,String name) {

        IPage page = new Page(current,size);
        QueryWrapper<Paradise> wrapper = new QueryWrapper<>();
        wrapper.like(!StrUtil.isBlank(name),"name",name);
        IPage page1 = page(page,wrapper);
        return new Result(page1);
    }


    /**
     * 亲钻转换金额=（报名费用-结算费用-一级经销奖励）/0.3
     *
     * @return
     */
    public Paradise count(Paradise obj) {


        BigDecimal adultQinZuan = (obj.getAdultCost().subtract(obj.getAdultMj()).subtract(obj.getAdultFirstSellAward())).divide(new BigDecimal("0.3"), 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal childQinZuan = (obj.getChildCost().subtract(obj.getChildMj()).subtract(obj.getChildFirstSellAward())).divide(new BigDecimal("0.3"), 2, BigDecimal.ROUND_HALF_UP);
//        double adultQinZuan = (obj.getAdultCost()-obj.getAdultMj()-obj.getAdultFirstSellAward())/0.3;
//        double childQinZuan = (obj.getChildCost()-obj.getChildMj()-obj.getChildFirstSellAward())/0.3;
        //设置亲钻奖励
        obj.setAdultDiamond(adultQinZuan);
        obj.setChildDiamond(childQinZuan);
        return obj;
    }
/*

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addParadise(Paradise obj) {

        obj.setStatus(0);
        obj.setCreateTime(new Date());
        obj.setUpdateTime(new Date());

        //设置商家实得金额
        obj.setAdultActualMoney(obj.getAdultMj().multiply(STORE_GET_PROPORTION).setScale(2, BigDecimal.ROUND_DOWN));
        obj.setChildActualMoney(obj.getChildMj().multiply(STORE_GET_PROPORTION).setScale(2, BigDecimal.ROUND_DOWN));
        //设置亲钻
        Paradise obj2 = count(obj);
        System.out.println(obj2.toString());
        boolean b = save(obj2);
        if (!b) {
            return Result.Error("发布失败");
        } else {
            return Result.OK("发布成功");
        }
    }
*/

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addParadise(AddParadiseVO vo,Long customerId) {

        Customer customer = customerMapper.selectById(customerId);
        if(customer.getCustomerType() != 2 ||customer.getCustomerType() != 1){
            return Result.Error("只有门店用户才能新增活动");
        }


        Paradise obj = new Paradise();
        BeanUtil.copyProperties(vo,obj);
        obj.setStoreId(customerId.intValue());
        //设置商家实得金额
        obj.setAdultActualMoney(obj.getAdultMj().multiply(STORE_GET_PROPORTION).setScale(2, BigDecimal.ROUND_DOWN));
        obj.setChildActualMoney(obj.getChildMj().multiply(STORE_GET_PROPORTION).setScale(2, BigDecimal.ROUND_DOWN));
        obj.setStatus(1);
        obj.setCreateTime(new Date());
        obj.setUpdateTime(new Date());
       //设置亲钻
        obj = count(obj);
        int count = paradiseDao.insert(obj);

        if (count == 0) {
            return Result.Error("添加失败");
        } else {
            return Result.OK("添加成功");
        }
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
        return this.paradiseDao.deleteById(id) > 0;
    }


    @Override
    public Result getParadiseByStore(int page,int size,Long customerId) {

//        TokenUser tokenUser = JobUtil.getTokenUser(request, redisHandle);
//        if(tokenUser == null){
//            return Result.Error("登录过期！");
//        }
//

//        Long customerId = tokenUser.getCustomerId();
        Store store = storeMapper.selectOne(new QueryWrapper<Store>().eq("customerId", customerId).last(" limit 1"));

        List<Map<String, Object>> maps = baseMapper.getListByStore(store.getStoreId(),(page-1)*size,size);
        Result result = new Result(maps);
        int count = baseMapper.getListByStoreCount(store.getStoreId());
        result.setTotalCount(count);
        result.setCurPage(page);
        result.setPageSize(size);


        return result;
    }

    @Override
    public Result getDetail(Integer id) {
        QueryWrapper<Paradise> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        Map<String, Object> map = getMap(wrapper);
        //查询已核销人数 status=2
        int count = paradiseDao.getHeXiaoCount(id);
        map.put("heXiaoNum", count);
        //查询报名信息下的头像（最多4个）
        List<String> headList = paradiseDao.getHead(id);
        map.put("headList", headList);
        return new Result(map);
    }

    @Override
    public Result getParaRegistration(Integer current, Integer size, Integer id) {

        IPage page = new Page(null == current ? 1 : current, null == size ? 10 : size);
        IPage<Map> customerIPage = baseMapper.getUserList(page, id);
        return new Result(customerIPage);
    }


    @Override
    public Result getList() {
//        QueryWrapper<Paradise> wrapper = new QueryWrapper<>();
//        wrapper.select("id", "name", "cover", "opening_time", "closing_time", "site", "longitude", "latitude", "status", "available_daily_number");
//        //排除待审核和审核失败的数据
//        wrapper.ne("status", 0).ne("status", 6);
//        List<Map<String, Object>> list = listMaps(wrapper);
        List<Map<String, Object>> list = baseMapper.getList();
        return new Result(list);
    }

    /**
     * @param id 乐园id
     * @return
     */
    @Override
    public Result getDetail2(Integer id) {
        QueryWrapper<Paradise> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        Map<String, Object> map = getMap(wrapper);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //今天
        String today = sdf.format(new Date());
        //查询今日已报名人数
        int num = baseMapper.getNumByDate(id, today);
        map.put("registered_num", num);
        return new Result(map);

    }


    @Override
    public Result getMyParadise(Integer status) {

        String token = JobUtil.getAuthorizationToken(request);
        if(StrUtil.isBlank(token)){
            return Result.Error("token无效");
        }
        WxUser user = (WxUser) redisHandle.get(token);
        String userId = user.getCustomerId();

        List<Map<String, Object>> map = baseMapper.getMyParadise(Integer.parseInt(userId), status);

        return new Result(map);
    }

    @Override
    public Result getparById(int id) {
        /*QueryWrapper<Paradise> wrapper = new QueryWrapper<>();
        //指定查询字段
        wrapper.select("activity_introduction","pic_url","apply_num","adult_cost","child_cost","adult_first_sell_award","child_first_sell_award",
                "adult_diamond","child_diamond","sponsor","activity_start_time","site","activity_details","notice","longitude","latitude");
        Map<String, Object> map = getMap(wrapper.eq("id", id));
        //查询已核销人数
        int count = ParadiseDao.getHeXiaoCount(id);
        map.put("heXiaoNum",count);
        //查询报名信息下的头像（最多4个）
        List<String> headList = ParadiseDao.getHead(id);
        map.put("headList",headList);
        return new Result(map);*/
        return null;
    }

    @Override
    public Result parPay(Integer orderId) {
        //当前订单
        ParadiseSignup paradiseSignup = paradiseSignupDao.selectById(orderId);
        WxOrderVo wxOrderVo = new WxOrderVo();
        //订单号
        wxOrderVo.setOutTradeNo(paradiseSignup.getId().toString()+"paradise");
        //金额  分
        double v = paradiseSignup.getPracticalMoney().doubleValue() * 100;
        wxOrderVo.setTotalFee(new Double(v).intValue());
        //商品描述
        wxOrderVo.setBody(paradiseSignup.getName()+"的乐园订单");
        //备注
        //商品（活动）id
        wxOrderVo.setProductId(paradiseSignup.getParadiseId().toString());
        //添加信息到支付表
        ActivityPay activityPay = new ActivityPay();
        activityPay.setMoney(wxOrderVo.getTotalFee());
        activityPay.setDescribe(wxOrderVo.getBody());
        activityPay.setDescribe(wxOrderVo.getAttach());
        //新增的乐园状态为报名中
        activityPay.setStatus(1);
        activityPay.setType(2);
        activityPay.setOrderId(wxOrderVo.getOutTradeNo());
        activityPay.setCreatetime(new Date());
        activityPay.setActid(paradiseSignup.getParadiseId());
        activityPayDao.insert(activityPay);
        return new Result(wxOrderVo);
    }

    @Override
    public Result getParList() {
        List<Paradise> list = list(new QueryWrapper<Paradise>().last(" limit 2"));
        return new Result(list);
    }

    /**
     * 开园/闭园
     * @param id
     * @param status
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result switchPar(Integer id, Integer status) {

        Paradise paradise = getById(id);
        if(paradise == null ){
            return Result.Error("未查询到乐园！");
        }
        long millis = System.currentTimeMillis();
        if(millis>=paradise.getOpeningTime().getTime() && millis<paradise.getClosingTime().getTime()){
            return Result.Error("该乐园正在进行！");
        }
        paradise.setStatus(status);
        updateById(paradise);
        return new Result(paradise);
    }

    @Override
    public Result detele(Integer id) {

        Paradise paradise = getById(id);
        if(paradise.getStatus()==1){
            return Result.Error("该乐园正在进行,不能删除！");
        }
        boolean b = deleteById(id);
        if(b){
            return Result.OK("删除成功！");
        }else{
            return Result.Error("删除失败！");
        }
    }

}