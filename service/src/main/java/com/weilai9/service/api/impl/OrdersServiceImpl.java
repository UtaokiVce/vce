package com.weilai9.service.api.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.common.utils.obs.ObsUtil;
import com.weilai9.common.utils.order.OrderUtil;
import com.weilai9.common.utils.qrCode.Path2FileUtil;
import com.weilai9.common.utils.qrCode.QRCodeUtil;
import com.weilai9.common.utils.wechat.JobUtil;
import com.weilai9.common.utils.wechat.QzUtil;
import com.weilai9.common.utils.wechat.RedisHandle;
import com.weilai9.dao.entity.*;
import com.weilai9.dao.mapper.*;
import com.weilai9.dao.vo.ordersVo.*;
import com.weilai9.dao.vo.wechat.WxOrderVo;
import com.weilai9.service.api.OrdersService;
import org.springframework.stereotype.Service;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 订单表(Orders)表服务实现类
 *
 * @author makejava
 * @since 2020-04-28 09:14:29
 */
@Service("ordersService")
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Resource
    private OrdersMapper ordersMapper;
    @Resource
    private ShoppingcartMapper shoppingcartMapper;
    @Resource
    private GoodsskuMapper goodsskuMapper;
    @Resource
    private OrdergoodsMapper ordergoodsMapper;
    @Resource
    private StoreMapper storeMapper;
    @Resource
    private GoodsFreightMapper goodsFreightMapper;
    @Resource
    private CustomerMapper customerMapper;
    @Resource
    private StoreStockMapper storeStockMapper;
    @Resource
    private HttpServletRequest request;
    @Resource
    private RedisHandle redisHandle;
    @Resource
    private StoreMoneyOffMapper storeMoneyOffMapper;
    @Resource
    private GoodscommentMapper goodscommentMapper;
    @Resource
    private ShippingAddressDao shippingAddressDao;
    @Resource
    private ActiveMapper activeMapper;
    @Resource
    CustomerqinzuanMapper customerqinzuanMapper;


    @Override
    public Result addOrder(AddOrderVo addOrderVo) {
        String token = getAuthorizationToken();
        if (StrUtil.isBlank(token)) {
            return Result.Error("用户未登录");
        }
        WxUser user = (WxUser) redisHandle.get(token);
        Integer customerId = Integer.parseInt(user.getCustomerId());
        String orderNo = OrderUtil.setOrderNo();

        List<AddOrderStoreVo> addOrderStoreVoList = addOrderVo.getAddOrderStoreVoList();
        for (AddOrderStoreVo a : addOrderStoreVoList) {
            Orders orders = new Orders();
            orders.setCustomerid(customerId);
            orders.setOrderno(orderNo);
            orders.setAddtime(new Date());
            orders.setOrdertype(a.getOrderType());
            orders.setOrderattr(0);
            orders.setStoreid(a.getOrderStoreId());
            orders.setGoodsallmoney(a.getStoreGoodsPrice());
            orders.setRealmoney(a.getStoreAccountPrice());
            orders.setState(1);

            ordersMapper.insert(orders);
            Integer orderId = orders.getOrderid();
            List<AddOrderGoodsVo> addOrderGoodsVoList = a.getAddOrderGoodsVoList();
            for (AddOrderGoodsVo aog: addOrderGoodsVoList ) {
                Ordergoods ordergoods = new Ordergoods();
                ordergoods.setOrderid(orderId);
                ordergoods.setGoodsskuid(aog.getOrderGoodsSkuId());
                ordergoods.setGoodsid(aog.getOrderGoodsId());
                ordergoods.setOrdergoodsno(orderNo);
                ordergoods.setPrice(aog.getOrderGoodsPrice());
                ordergoods.setNum(aog.getOrderGoodsNum());
                if (a.getOrderType()==1){
                    // 存放在二维码中的内容

                    String text = ((long) ((Math.random() * 10) * Math.pow(10, 11)))+"";
                    // 生成的二维码的路径及名称
                    String file = "03"+text + ".jpg";
                    String destPath = "D:/JAVA/" + file;
                    //生成二维码
                    try {
                        QRCodeUtil.encode("03"+text, null, destPath, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String url = ObsUtil.uploadFile(file, Path2FileUtil.getMulFileByPath(destPath));

                    ordergoods.setQrCodeNo(text);
                    ordergoods.setQrCodeUrl(url);
                }
                ordergoods.setStoreid(a.getOrderStoreId());
                this.ordergoodsMapper.insert(ordergoods);

                //测试用
                BigDecimal changePrice = aog.getOrderGoodsChangePrice();
                QzUtil.addQz(customerqinzuanMapper,customerId,changePrice,1,ordergoods.getOrdergoodsid(),3);


                StoreStock storeStock = this.storeStockMapper.selectOne(new QueryWrapper<StoreStock>().eq("storeId", a.getOrderStoreId())
                        .eq("goodsId", aog.getOrderGoodsId())
                        .eq("goodsSkuId", aog.getOrderGoodsSkuId()));
                storeStock.setNum(storeStock.getNum()-aog.getOrderGoodsNum());
                this.storeStockMapper.updateById(storeStock);

                //清除购物车
                shoppingcartMapper.delete(new QueryWrapper<Shoppingcart>().eq("customerId",customerId).eq("storeId",a.getOrderStoreId()).eq("goodsId",aog.getOrderGoodsId()).eq("goodsSkuId",aog.getOrderGoodsSkuId()));

            }
        }


        WxOrderVo wxOrderVo = new WxOrderVo();
        wxOrderVo.setBody("id为"+customerId+"的商品总订单");
        wxOrderVo.setOutTradeNo(orderNo);
        int a =addOrderVo.getOrdersMoney().multiply(new BigDecimal(100)).intValue();
        wxOrderVo.setTotalFee(a);
        return new Result(wxOrderVo);
    }

    @Override
    public Result addOrderGoods(Integer orderId, Integer shopId,Integer orderType) {
        String token = getAuthorizationToken();
        if (StrUtil.isBlank(token)) {
            return Result.Error("用户未登录");
        }

        Shoppingcart shoppingcart = shoppingcartMapper.selectById(shopId);
        Ordergoods ordergoods = new Ordergoods();
        if(orderType==1) {
            // 存放在二维码中的内容

            String text = ((long) ((Math.random() * 10) * Math.pow(10, 11)))+"";
            // 生成的二维码的路径及名称
            String file = "03"+text + ".jpg";
            String destPath = "D:/JAVA/" + file;
            //生成二维码
            try {
                QRCodeUtil.encode("03"+text, null, destPath, true);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String url = ObsUtil.uploadFile(file, Path2FileUtil.getMulFileByPath(destPath));

            ordergoods.setQrCodeNo(text);
            ordergoods.setQrCodeUrl(url);
        }
        ordergoods.setGoodsid(shoppingcart.getGoodsid());
        ordergoods.setGoodsskuid(shoppingcart.getGoodsskuid());
        ordergoods.setOrderid(orderId);
        ordergoods.setStoreid(shoppingcart.getStoreid());
        Goodssku goodssku = goodsskuMapper.selectById(shoppingcart.getGoodsskuid());
        ordergoods.setNum(shoppingcart.getNum());
        ordergoods.setPrice((goodssku.getPrice()).multiply((new BigDecimal(shoppingcart.getNum()))));
        ordergoods.setOrdergoodsno(OrderUtil.setOrderNo());

        ordergoodsMapper.insert(ordergoods);


        return Result.OK();
    }

    @Override
    public Result cancelOrder(Integer orderId) {
        String token = getAuthorizationToken();
        if (StrUtil.isBlank(token)) {
            return Result.Error("用户未登录");
        }
        Orders orders = this.ordersMapper.selectById(orderId);
        orders.setState(0);
        this.ordersMapper.updateById(orders);
        return Result.OK();
    }

    @Override
    public Result getOrderSList(TokenUser tokenUser,Integer state) {
        if (tokenUser==null) {
            return Result.Error("用户未登录");
        }
        Integer customerId = tokenUser.getCustomerId().intValue();

        List<Orders> orderList2 = this.ordersMapper.selectList(new QueryWrapper<Orders>().eq("customerId", customerId));
        for (Orders orders : orderList2) {
            if (!DateUtil.isIn(DateUtil.date(), orders.getAddtime(), DateUtil.date(orders.getAddtime().getTime()))){
                orders.setState(0);
                this.ordersMapper.updateById(orders);
            }
        }

        List<OrderVo> orderVoList = new ArrayList<>();
        //查询总订单列表
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("customerId",customerId);
        if (state==-2){
            state=null;
        }
        if (state!=null){
            queryWrapper.eq("state",state);
        }

        queryWrapper.select("orderId,orderNo,storeId,orderType,customerId,orderAttr,goodsAllMoney,activeMoney,couponId,couponMoney,realMoney,state,payType,addTime");
        List<Orders> ordersList = this.ordersMapper.selectList(queryWrapper);
        for (Orders order:ordersList) {

            //创建OrderVo
            OrderVo orderVo = new OrderVo();
            orderVo.setOrders(order);
            Date addtime = order.getAddtime();
            orderVo.setEndtime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(addtime.getTime()+900000)) ;
            //根据orderId查询orderGoods
            Integer orderid = order.getOrderid();
            QueryWrapper queryWrapper1 = new QueryWrapper();
            queryWrapper1.select("storeId").eq("orderId",orderid);
            queryWrapper1.groupBy("storeId");
            List<Ordergoods> ordergoodsList = this.ordergoodsMapper.selectList(queryWrapper1);
            List<OrderStoreVo> orderStoreVoList = getOrderStoreVo(ordergoodsList,orderid);
            orderVo.setOrderStoreVoList(orderStoreVoList);
            orderVoList.add(orderVo);
        }

        return new Result(orderVoList);
    }

    @Override
    public Result changeOrderState(TokenUser tokenUser, Integer orderId, Integer state) {
        if (tokenUser==null) {
            return Result.Error("用户未登录");
        }
        Integer customerId = tokenUser.getCustomerId().intValue();
        Orders orders = this.ordersMapper.selectOne(new QueryWrapper<Orders>().eq("customerId", customerId).eq("orderId",orderId));
        orders.setState(state);
        this.ordersMapper.updateById(orders);
        return Result.OK();
    }

    private List<OrderStoreVo> getOrderStoreVo(List<Ordergoods> ordergoodsList,Integer orderid){
        List<OrderStoreVo> orderStoreVoList = new ArrayList<>();
        for (Ordergoods orderGoods :ordergoodsList) {
            OrderStoreVo orderStoreVo = new OrderStoreVo();

            Integer storeid = orderGoods.getStoreid();
            Store store = this.storeMapper.selectById(storeid);
            orderStoreVo.setOrderStoreId(storeid);
            orderStoreVo.setOrderStoreName(store.getStoreName());
            orderStoreVo.setOrderAddr(store.getAddr());

            QueryWrapper queryWrapper2 = new QueryWrapper();
            queryWrapper2.eq("storeId",storeid);
            queryWrapper2.eq("orderId",orderid);
            List<Ordergoods> ordergoodsList1 = this.ordergoodsMapper.selectList(queryWrapper2);
            List<OrderGoodsVo> orderGoodsVoList = new ArrayList<>();


            BigDecimal storePrice = new BigDecimal(0.00);
            for (Ordergoods o : ordergoodsList1) {
                OrderGoodsVo orderGoodsVo = this.ordersMapper.getOrderGoodsVoByCondition(o.getGoodsid(), o.getGoodsskuid(),o.getOrderid());
                storePrice = orderGoodsVo.getOrderGoodsPrice().multiply(new BigDecimal(orderGoodsVo.getOrderGoodsNum())).add(storePrice);
                orderGoodsVoList.add(orderGoodsVo);
            }

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("storeId",storeid);
            queryWrapper.eq("state",1);

            StoreMoneyOff storeMoneyOff = this.storeMoneyOffMapper.selectOne(queryWrapper);
            if (storeMoneyOff==null){
                orderStoreVo.setStorePrice(storePrice);
            }
            else if (storeMoneyOff.getState()==1&&
                    storePrice.compareTo(storeMoneyOff.getSatisfyPrice())>-1 &&
                    DateUtil.isIn(DateUtil.date(),DateUtil.dateNew(storeMoneyOff.getBeginTime()),DateUtil.dateNew(storeMoneyOff.getEndTime()))
                   ){

                orderStoreVo.setStorePrice(storePrice.subtract(storeMoneyOff.getDecreasePrice()));
            }
            else orderStoreVo.setStorePrice(storePrice);
            orderStoreVo.setOrderGoodsVoList(orderGoodsVoList);

            orderStoreVoList.add(orderStoreVo);
        }
        return orderStoreVoList;
    }

    @Override
    public Result reduceGoodsStock(Integer goodsId, Integer goodsSkuId,Integer num) {
        String token = getAuthorizationToken();
        if (StrUtil.isBlank(token)) {
            return Result.Error("用户未登录");
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("goodsId",goodsId);
        queryWrapper.eq("goodsSkuId",goodsSkuId);
        StoreStock storeStock = this.storeStockMapper.selectOne(queryWrapper);
        storeStock.setNum(storeStock.getNum() - num);
        if (storeStock.getNum()>0){
            this.storeStockMapper.updateById(storeStock);
            return Result.OK();
        }
        return Result.Error("库存不足");



    }

    @Override
    public Result rollBackGoodsStock(Integer goodsId, Integer goodsSkuId,Integer num) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("goodsId",goodsId);
        queryWrapper.eq("goodsSkuId",goodsSkuId);
        StoreStock storeStock = this.storeStockMapper.selectOne(queryWrapper);
        storeStock.setNum(storeStock.getNum() + num);

        return Result.OK("回滚成功");
    }

    @Override
    public Result addOrderRate(Goodscomment goodscomment) {
        String token = getAuthorizationToken();
        if (goodscomment==null){
            return Result.Error("信息不存在");
        }
        else if (StrUtil.isBlank(token)) {
            return Result.Error("用户未登录");
        }
        else {WxUser user = (WxUser) redisHandle.get(token);
            Integer customerId = Integer.parseInt(user.getCustomerId());
            goodscomment.setCustomerid(customerId);
            goodscomment.setAddtime(DateUtil.date());
            this.goodscommentMapper.insert(goodscomment);

            Ordergoods ordergoods = this.ordergoodsMapper.selectOne(new QueryWrapper<Ordergoods>().eq("orderGoodsNo", goodscomment.getOrderGoodsNo()));
            ordergoods.setCommentState(1);
            this.ordergoodsMapper.updateById(ordergoods);


            return Result.OK("添加成功");}
    }

    //订单结算页
    @Override
    public Result goodsSettlement(String shopIds) {
        Map<String,Object> map = new HashMap<>();

        //获取用户Id
        String token = getAuthorizationToken();
        if (StrUtil.isBlank(token)) {
            return Result.Error("用户未登录");
        }
        WxUser user = (WxUser) redisHandle.get(token);
        Integer customerId = Integer.parseInt(user.getCustomerId());

        //获取用户默认收货地址
        ShippingAddress shippingAddress = shippingAddressDao.selectOne(new QueryWrapper<ShippingAddress>().eq("user_id", customerId).eq("is_default", 1));
        map.put("address",shippingAddress);

        //获取购物车id
        String[] split = shopIds.split(",");
        for (int i = 0; i < split.length; i++) {

        }
        return Result.OK();
    }

    @Override
    public Integer getGoodsStockNum(Integer goodsId, Integer goodsSkuId, Integer storeId) {
        StoreStock storeStock = this.storeStockMapper.selectOne(new QueryWrapper<StoreStock>().eq("goodsId", goodsId)
                .eq("goodsSkuId", goodsSkuId)
                .eq("storeId", storeId));
        return storeStock.getNum();
    }

    @Override
    public Result secKillGoodsCheckout(Integer ordertype, BigDecimal goodsallmoney, BigDecimal realmoney, Integer storeId, Integer goodsId, Integer goodsSkuId,Integer num) {
        if (num==null){
            num=1;
        }
        String token = getAuthorizationToken();
        if (StrUtil.isBlank(token)) {
            return Result.Error("用户未登录");
        }
        WxUser user = (WxUser) redisHandle.get(token);
        Integer customerId = Integer.parseInt(user.getCustomerId());

        //检查商品是否还在秒杀活动
//        this.activeMapper.selectOne(new QueryWrapper<Active>());

        //检查库存
        if (getGoodsStockNum(goodsId,goodsSkuId,storeId)==null||getGoodsStockNum(goodsId,goodsSkuId,storeId)<1){
            return Result.Error(201,"下单失败，库存不足");
        }
        else reduceGoodsStock(goodsId,goodsSkuId,1);

        //添加总订单
        Orders orders = new Orders();
        String s = OrderUtil.setOrderNo();
        orders.setOrderno(s);
        orders.setOrdertype(ordertype);
        orders.setOrderattr(1);
        orders.setCustomerid(customerId);
        orders.setStoreid(storeId);
        orders.setGoodsallmoney(goodsallmoney);
        orders.setRealmoney(realmoney);
        orders.setState(1);
        orders.setAddtime(new Date());
        if(orders.getOrdertype()==1){
            // 存放在二维码中的内容

            String text = ((long) ((Math.random() * 10) * Math.pow(10, 11)))+"";
            // 生成的二维码的路径及名称
            String file = "03"+text+".jpg";
            String destPath = "D:/JAVA/"+file;
            //生成二维码
            try {
                QRCodeUtil.encode("03"+text, null, destPath, true);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String url = ObsUtil.uploadFile(file, Path2FileUtil.getMulFileByPath(destPath));

            orders.setQrCodeNo(text);
            orders.setQrCodeUrl(url);

        }
        this.ordersMapper.insert(orders);

        //添加商品订单
        Ordergoods ordergoods = new Ordergoods();
        if(orders.getOrdertype()==1) {
            // 存放在二维码中的内容

            String text = ((long) ((Math.random() * 10) * Math.pow(10, 11)))+"";
            // 生成的二维码的路径及名称
            String file = "03"+text + ".jpg";
            String destPath = "D:/JAVA/" + file;
            //生成二维码
            try {
                QRCodeUtil.encode("03"+text, null, destPath, true);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String url = ObsUtil.uploadFile(file, Path2FileUtil.getMulFileByPath(destPath));

            ordergoods.setQrCodeNo(text);
            ordergoods.setQrCodeUrl(url);
        }
        ordergoods.setOrderid(orders.getOrderid());
        ordergoods.setGoodsskuid(goodsSkuId);
        ordergoods.setGoodsid(goodsId);
        ordergoods.setStoreid(storeId);
        ordergoods.setPrice(goodsallmoney);
        ordergoods.setNum(num);
        ordergoods.setItemtype(1);
        ordergoods.setOrdergoodsno(s);
        this.ordergoodsMapper.insert(ordergoods);
        Integer ordergoodsid = ordergoods.getOrdergoodsid();




        //测试用
        Goodssku goodssku = this.goodsskuMapper.selectById(goodsSkuId);
        BigDecimal changePrice = goodssku.getChangePrice();
        QzUtil.addQz(customerqinzuanMapper,customerId,changePrice,1,ordergoodsid,3);



        WxOrderVo wxOrderVo = new WxOrderVo();
        wxOrderVo.setBody("id为"+customerId+"的商品总订单");
        wxOrderVo.setOutTradeNo(orders.getOrderno());
        int a = orders.getRealmoney().multiply(new BigDecimal(100)).intValue();
        wxOrderVo.setTotalFee(a);
        wxOrderVo.setProductId(ordergoods.getGoodsid()+"");

        StoreStock storeStock = this.storeStockMapper.selectOne(new QueryWrapper<StoreStock>().eq("storeId", storeId)
                .eq("goodsId", goodsId)
                .eq("goodsSkuId", goodsSkuId));
        storeStock.setNum(storeStock.getNum()-1);
        this.storeStockMapper.updateById(storeStock);



        return new Result(wxOrderVo);


    }

    @Override
    public Result goodsQr(Integer qrState, TokenUser tokenUser) {
        return null;
    }

    @Override
    public Result orderPay(TokenUser tokenUser, String orderNo, BigDecimal realmoney) {
        String token = getAuthorizationToken();
        if (StrUtil.isBlank(token)) {
            return Result.Error("用户未登录");
        }
        WxUser user = (WxUser) redisHandle.get(token);
        String nickname = user.getNickname();
        WxOrderVo wxOrderVo = new WxOrderVo();
        wxOrderVo.setBody(nickname+"的商品订单");
        wxOrderVo.setOutTradeNo(orderNo);
        int a = realmoney.multiply(new BigDecimal(100)).intValue();
        wxOrderVo.setTotalFee(a);
        return new Result(wxOrderVo);
    }

    @Override
    public Result directGoodsCheckout(Integer ordertype, BigDecimal goodsallmoney, BigDecimal realmoney, Integer storeId, Integer goodsId, Integer goodsSkuId, Integer num) {
        String token = getAuthorizationToken();
        if (StrUtil.isBlank(token)) {
            return Result.Error("用户未登录");
        }
        WxUser user = (WxUser) redisHandle.get(token);
        Integer customerId = Integer.parseInt(user.getCustomerId());


        //检查库存
        if (getGoodsStockNum(goodsId,goodsSkuId,storeId)==null||getGoodsStockNum(goodsId,goodsSkuId,storeId)<num){
            return Result.Error(201,"下单失败，库存不足");
        }
        else reduceGoodsStock(goodsId,goodsSkuId,num);

        //添加总订单
        Orders orders = new Orders();
        String s = OrderUtil.setOrderNo();
        orders.setOrderno(s);
        orders.setOrdertype(ordertype);
        orders.setOrderattr(0);
        orders.setCustomerid(customerId);
        orders.setStoreid(storeId);
        orders.setGoodsallmoney(goodsallmoney);
        orders.setRealmoney(realmoney);
        orders.setState(1);
        orders.setAddtime(new Date());
        if(orders.getOrdertype()==1){
            // 存放在二维码中的内容

            String text = ((long) ((Math.random() * 10) * Math.pow(10, 11)))+"";
            // 生成的二维码的路径及名称
            String file = "03"+text+".jpg";
            String destPath = "D:/JAVA/"+file;
            //生成二维码
            try {
                QRCodeUtil.encode("03"+text, null, destPath, true);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String url = ObsUtil.uploadFile(file, Path2FileUtil.getMulFileByPath(destPath));

            orders.setQrCodeNo(text);
            orders.setQrCodeUrl(url);

        }
        this.ordersMapper.insert(orders);

        //添加商品订单
        Ordergoods ordergoods = new Ordergoods();
        if(orders.getOrdertype()==1) {
            // 存放在二维码中的内容

            String text = ((long) ((Math.random() * 10) * Math.pow(10, 11)))+"";
            // 生成的二维码的路径及名称
            String file = "03"+text + ".jpg";
            String destPath = "D:/JAVA/" + file;
            //生成二维码
            try {
                QRCodeUtil.encode("03"+text, null, destPath, true);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String url = ObsUtil.uploadFile(file, Path2FileUtil.getMulFileByPath(destPath));

            ordergoods.setQrCodeNo(text);
            ordergoods.setQrCodeUrl(url);
        }
        ordergoods.setOrderid(orders.getOrderid());
        ordergoods.setGoodsskuid(goodsSkuId);
        ordergoods.setGoodsid(goodsId);
        ordergoods.setStoreid(storeId);
        Goodssku goodssku1 = this.goodsskuMapper.selectById(goodsSkuId);
        ordergoods.setPrice(goodssku1.getPrice());
        ordergoods.setNum(num);
        ordergoods.setItemtype(0);
        ordergoods.setOrdergoodsno(s);
        this.ordergoodsMapper.insert(ordergoods);
        Integer ordergoodsid = ordergoods.getOrdergoodsid();




        //测试用
        Goodssku goodssku = this.goodsskuMapper.selectById(goodsSkuId);
        BigDecimal changePrice = goodssku.getChangePrice();
        QzUtil.addQz(customerqinzuanMapper,customerId,changePrice,1,ordergoodsid,3);



        WxOrderVo wxOrderVo = new WxOrderVo();
        wxOrderVo.setBody("id为"+customerId+"的商品总订单");
        wxOrderVo.setOutTradeNo(orders.getOrderno());
        int a = orders.getRealmoney().multiply(new BigDecimal(100)).intValue();
        wxOrderVo.setTotalFee(a);
        wxOrderVo.setProductId(ordergoods.getGoodsid()+"");

        StoreStock storeStock = this.storeStockMapper.selectOne(new QueryWrapper<StoreStock>().eq("storeId", storeId)
                .eq("goodsId", goodsId)
                .eq("goodsSkuId", goodsSkuId));
        storeStock.setNum(storeStock.getNum()-1);
        this.storeStockMapper.updateById(storeStock);



        return new Result(wxOrderVo);

    }

    @Override
    public Result getStoreOrderList(Integer pageno,Integer pagesize,Integer state,TokenUser tokenUser) {
        if (tokenUser == null) {
            return Result.Error("登录过期！");
        }
        Integer customerId = tokenUser.getCustomerId().intValue();
        Customer customer = this.customerMapper.selectOne(new QueryWrapper<Customer>().select("customer_type").eq("customer_id", customerId));
        Integer storeId = 0;

        if (customer != null && customer.getCustomerType() == 2) {
            Store store = this.storeMapper.selectOne(new QueryWrapper<Store>().select("storeId")
                    .eq("customerId", customerId)
                    .groupBy("storeId"));
            if (store == null) {
                return Result.Error(201, "该用户无门店");
            }
            storeId = store.getStoreId();
        } else if (customer != null && customer.getCustomerType() == 1) {
            storeId = null;
        } else {
            return Result.Error(201, "用户无权限");
        }


        QueryWrapper q = new QueryWrapper();
        q.eq("storeId",storeId);
        if (state!=null&&state!=-2){
            q.eq("state",state);
        }

        Page page = new Page(pageno,pagesize);


        IPage iPage = this.ordersMapper.selectPage(page, q);
        return new Result(iPage);
    }

    @Override
    public Result getOrderInfo(Integer orderId, TokenUser tokenUser) {
        if (tokenUser == null) {
            return Result.Error("登录过期！");
        }
        Integer customerId = tokenUser.getCustomerId().intValue();
        Customer customer = this.customerMapper.selectOne(new QueryWrapper<Customer>().select("customer_type").eq("customer_id", customerId));


        Store store = this.storeMapper.selectOne(new QueryWrapper<Store>().select("storeId")
                .eq("customerId", customerId)
                .groupBy("storeId"));
        if (store == null) {
            return Result.Error(201, "该用户无门店");
        }
            Integer storeId = store.getStoreId();


            Map<String, Object> map = new HashMap<>();
            Orders orders = this.ordersMapper.selectById(orderId);
            map.put("orderNo", orders.getOrderno());
            map.put("addTime", orders.getAddtime());
            if (orders.getOrdertype() == 2) {
                GoodsFreight gf = this.goodsFreightMapper.selectOne(new QueryWrapper<GoodsFreight>().eq("storeId", storeId));
                map.put("goodsFreight", gf.getFreight());
            } else {
                map.put("goodsFreight", 0.00);
            }
            map.put("state",orders.getState());
            map.put("realMoney", orders.getRealmoney());
            map.put("orderType", orders.getOrdertype());
            Store store1 = this.storeMapper.selectById(storeId);
            map.put("storeAddr",store1.getAddr());



        List<Ordergoods> ordergoodsList1 = this.ordergoodsMapper.selectList(new QueryWrapper<Ordergoods>().eq("orderId",orderId));
        List<OrderGoodsVo> orderGoodsVoList = new ArrayList<>();

        for (Ordergoods o : ordergoodsList1) {
            OrderGoodsVo orderGoodsVo = this.ordersMapper.getOrderGoodsVoByCondition(o.getGoodsid(), o.getGoodsskuid(),o.getOrderid());
            orderGoodsVoList.add(orderGoodsVo);
        }
        map.put("orderGoodsVoList",orderGoodsVoList);
        return new Result(map);
        }

    @Override
    public Result trackingNo(Integer orderId, String trackingNo, TokenUser tokenUser) {
        if (tokenUser == null) {
            return Result.Error("登录过期！");
        }
        Integer customerId = tokenUser.getCustomerId().intValue();
        Customer customer = this.customerMapper.selectOne(new QueryWrapper<Customer>().select("customer_type").eq("customer_id", customerId));


        Store store = this.storeMapper.selectOne(new QueryWrapper<Store>().select("storeId")
                .eq("customerId", customerId)
                .groupBy("storeId"));
        if (store == null) {
            return Result.Error(201, "该用户无门店");
        }

        Orders orders = this.ordersMapper.selectById(orderId);
        orders.setTrackingNo(trackingNo);
        orders.setState(4);
        this.ordersMapper.updateById(orders);
        return Result.OK();
    }


    private OrderGoodsVo getOrderGoodsVoByCondition(Integer goodsId,Integer goodsSku,Integer orderId){
        OrderGoodsVo orderGoodsVo = this.ordersMapper.getOrderGoodsVoByCondition(goodsId, goodsSku,orderId);
        return orderGoodsVo;
    }

    private String getAuthorizationToken() {
        return JobUtil.getAuthorizationToken(request);
    }
}