package com.weilai9.service.admin.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weilai9.common.constant.Result;
import com.weilai9.common.utils.order.OrderUtil;
import com.weilai9.dao.entity.*;
import com.weilai9.dao.mapper.*;
import com.weilai9.dao.vo.activity.GroupBuyingVO;
import com.weilai9.service.admin.GroupBuyingService;
import com.weilai9.service.api.ApiGoodsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GroupBuyingServiceImpl implements GroupBuyingService {

    @Resource
    private GroupBuyingDao dao;

    @Resource
    private ActiveMapper activeMapper;

    @Resource
    private StoreStockMapper storeStockMapper;

    @Resource
    private OrdersMapper ordersMapper;

    @Resource
    private ParentorderMapper parentorderMapper;

    @Resource
    private GrouponsMapper grouponsMapper;

    @Resource
    private OrdergoodsMapper ordergoodsMapper;

    @Resource
    private PaymentMapper paymentMapper;

    @Resource
    private GoodsFreightMapper goodsFreightMapper;

    @Resource
    private ShippingAddressDao shippingAddressDao;

    @Resource
    private GoodsMapper goodsMapper;

 @Resource
    private CustomerMapper customerMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addBuy(GroupBuyingVO vo, Long userId) {
        //当前活动
        Active active = activeMapper.selectById(vo.getActiveId());
        if (active == null) {
            return Result.Error("未找到该活动！");
        }

        Map<String, Object> result = new HashMap();
        int parentid = vo.getParentId();
        result.put("parentId",parentid);

        GoodsFreight GoodsFreight = goodsFreightMapper.selectOne(new QueryWrapper<GoodsFreight>().eq("storeId", active.getStoreid()));
        //运费（分）
        BigDecimal freight = GoodsFreight.getFreight();
        List<ShippingAddress> shippingAddresses = shippingAddressDao.selectList(new QueryWrapper<ShippingAddress>().eq("user_id", userId));
        //配送地址
        result.put("shippingAddresses", shippingAddresses);

        List<String> head = grouponsMapper.selectHead(parentid);
        //小团头像
        result.put("head", head);
        Map<String, Object> goodsInfo = goodsMapper.getGroupBuyInfo(active.getGoodsid());
        //商品商家信息
        result.put("goodsInfo", goodsInfo);

        //商品金额
        BigDecimal realmoney = active.getPrice();
        //总金额
        BigDecimal totalMoney = realmoney.add(freight);
        Map<String, Object> money = new HashMap();
        money.put("freight", freight);
        money.put("totalMoney", totalMoney);
        money.put("realmoney", realmoney);
        result.put("money", money);
        return new Result(result);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addOrder(GroupBuyingVO vo, Long userId) {

        //当前活动
        Active active = activeMapper.selectById(vo.getActiveId());
        if (active == null) {
            return Result.Error("未找到该活动！");
        }
        //验证是否限购
        if (active.getIsonly() != 0 && active.getOnlynum() < vo.getBuyNum()) {
            return Result.Error("团购失败，商品每人限购" + active.getOnlynum() + "件");
        }
        //查询库存  storeId goodsId goodsSkuId
        StoreStock storeStock = storeStockMapper.selectOne(new QueryWrapper<StoreStock>().eq("storeId", active.getStoreid())
                .eq("goodsId", active.getGoodsid()).eq("goodsSkuId", active.getGoodsskuid()));
        if (storeStock == null | storeStock.getNum() < vo.getBuyNum()) {
            //库存不足
            return Result.Error("团购失败，库存不足,仅剩" + storeStock.getNum() + "件");
        }

        Orders orders = new Orders();
        //小团
        Parentorder parentorder = new Parentorder();
        if (vo.getParentId() == null || vo.getParentId() == 0) {
            //开团 订单的parentId为空
            parentorder.setActiveid(vo.getActiveId());
            parentorder.setMinnum(active.getJoinnum());//成团人数
            parentorder.setAddtime(new Date());
            parentorder.setCurrentnum(1);//当前人数  开团为1
            parentorder.setIssucceed(0);//是否成功 1成功 2失败
            parentorder.setIsjoin(1); //是否可加入 1可以 0不可
            parentorder.setCapsid(Integer.parseInt(userId.toString()));
            parentorder.setState(0);//小团状态：-1 失效 0 待成团 1已成团
            parentorder.setUpdatetime(new Date());
            //添加小团表
            parentorderMapper.insert(parentorder);

        } else {
            //拼团
            //查询小团表
            parentorder = parentorderMapper.selectById(vo.getParentId());
            if (parentorder == null) {
                return Result.Error("小团不存在！");
            }
            //判断这个团是否允许加入
            if (parentorder.getIsjoin() == 0 | parentorder.getIssucceed() == 1) {
                return Result.Error("该团已满，不允许添加！");
            }
            parentorder.setAddtime(new Date());
            //当前人数+1
            parentorder.setCurrentnum(parentorder.getCurrentnum() + 1);
            //判断当前人数和成团人数是否相等
            if (parentorder.getCurrentnum().equals(parentorder.getMinnum())) {
                parentorder.setState(1);//状态改为已成团
                parentorder.setIsjoin(0);
                parentorder.setIssucceed(1);
            }
            //更新小团表
            parentorderMapper.updateById(parentorder);

            orders.setParentid(vo.getParentId());//团购id

        }

        //设置订单信息
        orders.setAddtime(new Date());//下单时间
        orders.setOrderno(OrderUtil.setOrderNo());
        orders.setOrdertype(vo.getBuyType());
        orders.setOrderattr(2);
        orders.setCustomerid(userId.intValue());
        orders.setStoreid(active.getStoreid());
        orders.setGoodsallmoney(active.getPrice().multiply(new BigDecimal(vo.getBuyNum())));//总金额
        //不传优惠金额 设为0
        if (vo.getCouponMoney() == null) {
            vo.setCouponMoney(new BigDecimal(0.00));
        }
        orders.setActivemoney(vo.getCouponMoney());//优惠总金额
        orders.setRealmoney(active.getPrice().multiply(new BigDecimal(vo.getBuyNum())).subtract(vo.getCouponMoney()));//实际支付金额
        orders.setState(1);//待支付状态
        orders.setMendtime(vo.getMendTime());
        orders.setSendtime(vo.getSendTime());
        ordersMapper.insert(orders);

        //往ordergoods 表写订单详细信息
        Ordergoods ordergoods = new Ordergoods(orders.getOrderid()
                , active.getGoodsskuid(), active.getGoodsid(), active.getPrice(), vo.getBuyNum(), 2, active.getActiveid());
        ordergoodsMapper.insert(ordergoods);

        //修改库存
        storeStock.setNum(storeStock.getNum() - vo.getBuyNum());
        storeStockMapper.updateById(storeStock);

        //添加支付信息
        Payment payment = new Payment();
        payment.setAddtime(new Date());
        payment.setCustomerid(userId.intValue());
        payment.setPayno(orders.getOrderno());
        payment.setPremoney(orders.getRealmoney());
        payment.setCate(1);
        payment.setState(2);
        paymentMapper.insert(payment);

        //小团参与者
        Groupons groupons = new Groupons(parentorder.getParentid(), userId.intValue());
        grouponsMapper.insert(groupons);

        Map<String, Object> result = new HashMap();

        GoodsFreight GoodsFreight = goodsFreightMapper.selectOne(new QueryWrapper<GoodsFreight>().eq("storeId", active.getStoreid()));
        //运费（分）
        BigDecimal freight = GoodsFreight.getFreight();

        //商品金额
        BigDecimal realmoney = orders.getRealmoney();
        //总金额
        BigDecimal totalMoney = realmoney.add(freight);
        result.put("totalFee", totalMoney);
        Customer customer = customerMapper.selectById(userId);
        result.put("body", customer.getCustomerName()+"的团购订单");
        result.put("outTradeNo", orders.getOrderno());

        return new Result(result);
    }

    /**
     * 获取团购列表
     *
     * @param page
     * @param limit
     * @return
     */
    @Override
    public Result getGroupList(Integer page, Integer limit) {
        IPage page1 = new Page(page, limit);
//        IPage iPage = activeMapper.selectPage(page1, new QueryWrapper<Active>().eq("activeType",2));
        List<Map<String, Object>> groupList = activeMapper.getGroupList();
        page1.setRecords(groupList);
        return new Result(page1);
    }


}
