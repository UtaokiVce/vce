package com.weilai9.service.api.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weilai9.common.constant.Result;
import com.weilai9.common.utils.wechat.JobUtil;
import com.weilai9.common.utils.wechat.RedisHandle;
import com.weilai9.dao.entity.*;
import com.weilai9.dao.mapper.*;
import com.weilai9.dao.vo.shoppingCart.CartGoodsVo;
import com.weilai9.dao.vo.shoppingCart.CartStoreVo;
import com.weilai9.service.api.OrdersService;
import com.weilai9.service.api.ShoppingcartService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * 购物车表(Shoppingcart)表服务实现类
 *
 * @author makejava
 * @since 2020-04-26 18:19:43
 */
@Service("shoppingcartService")
public class ShoppingcartServiceImpl extends ServiceImpl<ShoppingcartMapper, Shoppingcart> implements ShoppingcartService {
    @Resource
    private ShoppingcartMapper shoppingcartMapper;
    @Resource
    private StoreStockMapper storeStockMapper;
    @Resource
    private StoreMapper storeMapper;
    @Resource
    private OrdersService ordersService;
    @Resource
    private OrdergoodsMapper ordergoodsMapper;
    @Resource
    private GoodsFreightMapper goodsFreightMapper;
    @Resource
    private StoreMoneyOffMapper storeMoneyOffMapper;
    @Resource
    HttpServletRequest request;
    @Resource
    RedisHandle redisHandle;

    /**
     * 添加商品进购物车
     *
     * @param shoppingcart
     * @return
     */
    @Override
    public Result addGoods2Cart(Shoppingcart shoppingcart) {
        String token = getAuthorizationToken();
        if (StrUtil.isBlank(token)) {
            return Result.Error("用户未登录");
        }
        if (shoppingcart==null){
            return  Result.Error("商品不存在");
        }
        WxUser user = (WxUser) redisHandle.get(token);
        Integer customerId = Integer.parseInt(user.getCustomerId());
        shoppingcart.setCustomerid(customerId);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("customerId", shoppingcart.getCustomerid());
        queryWrapper.eq("goodsId", shoppingcart.getGoodsid());
        queryWrapper.eq("goodsSkuId", shoppingcart.getGoodsskuid());
        queryWrapper.eq("storeId",shoppingcart.getStoreid());

        List<Shoppingcart> selectList = shoppingcartMapper.selectList(queryWrapper);
        if (selectList.size() > 0) {
            selectList.forEach(e -> {
                UpdateWrapper updateWrapper = new UpdateWrapper();
                updateWrapper.eq("customerId", shoppingcart.getCustomerid());
                updateWrapper.eq("goodsId", shoppingcart.getGoodsid());
                updateWrapper.eq("goodsSkuId", shoppingcart.getGoodsskuid());
                int newNum = shoppingcart.getNum() + e.getNum();
                e.setNum(newNum);

                shoppingcartMapper.update(e, updateWrapper);
            });
            return Result.OK();
        }

        shoppingcartMapper.insert(shoppingcart);
        return Result.OK();
    }

    /**
     * 购物车商品数量增加
     *
     * @param shopid
     * @return
     */
    @Override
    public Result addNum(Integer shopid) {
        String token = getAuthorizationToken();
        if (StrUtil.isBlank(token)) {
            return Result.Error("用户未登录");
        }
        if (shopid==null){
            return Result.Error("购物车ID为空");
        }
        Shoppingcart shoppingcart = shoppingcartMapper.selectById(shopid);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("storeId",shoppingcart.getStoreid());
        queryWrapper.eq("goodsSkuId",shoppingcart.getGoodsskuid());
        queryWrapper.eq("goodsId",shoppingcart.getGoodsid());
        StoreStock storeStock = storeStockMapper.selectOne(queryWrapper);
        if (storeStock.getNum()>shoppingcart.getNum()){
            shoppingcart.setNum(shoppingcart.getNum() + 1);
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("shopid", shopid);
            shoppingcartMapper.update(shoppingcart, updateWrapper);
            return Result.OK("增加成功");
        }
        else if (shoppingcart.getNum()>=99){
            return Result.Error(201,"单次购买数量最高99");
        }
        else {

            return Result.Error(201,"已到最大库存，添加失败");
        }

    }

    /**
     * 购物车商品数量减少
     *
     * @param shopid
     * @return
     */
    @Override
    public Result reduceNum(Integer shopid) {
        String token = getAuthorizationToken();
        if (StrUtil.isBlank(token)) {
            return Result.Error("用户未登录");
        }
        Shoppingcart shoppingcart = shoppingcartMapper.selectById(shopid);
        if (shoppingcart.getNum()>0){
            shoppingcart.setNum(shoppingcart.getNum()-1);
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("shopid", shopid);
            shoppingcartMapper.update(shoppingcart, updateWrapper);
            return Result.OK("减少成功");
        }
        return Result.Error(201,"已到最小数量");
    }

    @Override
    public Result deleteGoods(Integer shopid) {
        String token = getAuthorizationToken();
        if (StrUtil.isBlank(token)) {
            return Result.Error("用户未登录");
        }
        shoppingcartMapper.deleteById(shopid);
        return Result.OK("删除成功");
    }

    @Override
    public Result queryGoodsByCustomerId() {
        String token = getAuthorizationToken();
        if (StrUtil.isBlank(token)) {
            return Result.Error("用户未登录");
        }
        WxUser user = (WxUser) redisHandle.get(token);
        Integer customerId = Integer.parseInt(user.getCustomerId());
        //查询该用户下商品所属商家
        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.select("storeId").eq("customerId",customerId);
        queryWrapper1.groupBy("storeId");

        List<Shoppingcart> storeIdList = shoppingcartMapper.selectList(queryWrapper1);

        List<CartStoreVo> cartStoreVoList = new ArrayList<>();

        //遍历商家获取商家信息
        for (Shoppingcart shoppingcart : storeIdList) {
            Integer storeId = shoppingcart.getStoreid();
            CartStoreVo cartStoreVo = new CartStoreVo();
            cartStoreVo.setCartStoreId(storeId);

            Store store = this.storeMapper.selectById(storeId);
            cartStoreVo.setCartStoreName(store.getStoreName());
            cartStoreVo.setCartAddr(store.getAddr());

            //查询该用户该商家下的购物车商品
            QueryWrapper queryWrapper2 = new QueryWrapper();
            queryWrapper2.eq("customerid",customerId);
            queryWrapper2.eq("storeId",storeId);
            List<Shoppingcart> cartList = this.shoppingcartMapper.selectList(queryWrapper2);


            List<CartGoodsVo> cartGoodsVoList = new ArrayList<>();
            for (Shoppingcart s : cartList) {
                CartGoodsVo cartGoodsVo = this.shoppingcartMapper.queryGoodsByGoodsId(s.getGoodsid(), s.getGoodsskuid(), s.getCustomerid(),s.getStoreid());
                //检查商品库存
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("storeId",s.getStoreid());
                queryWrapper.eq("goodsSkuId",s.getGoodsskuid());
                queryWrapper.eq("goodsId",s.getGoodsid());
                StoreStock storeStock = storeStockMapper.selectOne(queryWrapper);
                if (cartGoodsVo.getCartGoodsNum()>storeStock.getNum()){
                    cartGoodsVo.setCartGoodsNum(storeStock.getNum());

                    s.setNum(storeStock.getNum());
                    UpdateWrapper updateWrapper = new UpdateWrapper();
                    updateWrapper.eq("shopId",s.getShopid());
                    this.shoppingcartMapper.update(s,updateWrapper);

                    cartGoodsVo.setCartGoodsMaxNum(storeStock.getNum());
                    cartGoodsVoList.add(cartGoodsVo);
                }else cartGoodsVo.setCartGoodsMaxNum(storeStock.getNum());
                cartGoodsVoList.add(cartGoodsVo);
            }
            cartStoreVo.setCartGoodsVoList(cartGoodsVoList);
            cartStoreVoList.add(cartStoreVo);
        }
        return new Result(cartStoreVoList);
    }


    @Override
    public CartGoodsVo queryGoodsByGoodsId(Integer goodsId,Integer goodsSkuId,Integer storeId) {
        String token = getAuthorizationToken();

        WxUser user = (WxUser) redisHandle.get(token);
        Integer customerId = Integer.parseInt(user.getCustomerId());
        return shoppingcartMapper.queryGoodsByGoodsId(goodsId,goodsSkuId,customerId,storeId);
    }

    @Override
    public Result getAccount(String shopIds) {

        //获取用户id
        String token = getAuthorizationToken();
        if (StrUtil.isBlank(token)) {
            return Result.Error("用户未登录");
        }
        WxUser user = (WxUser) redisHandle.get(token);
        Integer customerId = Integer.parseInt(user.getCustomerId());

        //获取商品idList
        List<String> idsStringList = Arrays.asList(shopIds.split(","));
        List<Integer> shopIdList = new ArrayList<>();
        CollectionUtils.collect(idsStringList, new Transformer() {
            @Override
            public Object transform(Object o) {
                return Integer.valueOf(o.toString());
            }
        }, shopIdList);

        List<Shoppingcart> shops = this.shoppingcartMapper.selectList(new QueryWrapper<Shoppingcart>()
                .select("storeId")
                .eq("customerid",customerId)
                .in("shopId", shopIdList)
                .groupBy("storeId"));


        List<CartStoreVo> cartStoreVoList = new ArrayList<>();

        //遍历商家获取商家信息
        for (Shoppingcart shoppingcart : shops) {
            Integer storeId = shoppingcart.getStoreid();
            CartStoreVo cartStoreVo = new CartStoreVo();
            cartStoreVo.setCartStoreId(storeId);

            Store store = this.storeMapper.selectById(storeId);
            cartStoreVo.setCartStoreName(store.getStoreName());
            cartStoreVo.setCartAddr(store.getAddr());

            //查询该商家运费

            GoodsFreight freight = this.goodsFreightMapper.selectOne(new QueryWrapper<GoodsFreight>().eq("storeId", storeId));
            if (freight==null){
                cartStoreVo.setGoodsFreight(new BigDecimal(0.00));
            }
            else {
                cartStoreVo.setGoodsFreight(freight.getFreight());
            }


            //查询该商家满减规则
            StoreMoneyOff moneyOff = this.storeMoneyOffMapper.selectOne(new QueryWrapper<StoreMoneyOff>().eq("storeId", storeId).eq("state",1));

            BigDecimal goodsAccount = new BigDecimal(0.00);

            //查询该用户该商家下的商品
            QueryWrapper queryWrapper2 = new QueryWrapper();
            queryWrapper2.eq("customerid",customerId);
            queryWrapper2.eq("storeId",storeId);
            queryWrapper2.in("shopId",shopIdList);

            List<Shoppingcart> cartList = this.shoppingcartMapper.selectList(queryWrapper2);


            List<CartGoodsVo> cartGoodsVoList = new ArrayList<>();
            for (Shoppingcart s : cartList) {
                CartGoodsVo cartGoodsVo = this.shoppingcartMapper.queryGoodsByGoodsId(s.getGoodsid(), s.getGoodsskuid(), s.getCustomerid(),s.getStoreid());
                goodsAccount = (new BigDecimal(cartGoodsVo.getCartGoodsNum()).multiply(cartGoodsVo.getGoodsPrice())).add(goodsAccount);
                cartGoodsVoList.add(cartGoodsVo);
            }
            if (moneyOff!=null&&goodsAccount.compareTo(moneyOff.getSatisfyPrice())>-1&&moneyOff.getState()==1){
                cartStoreVo.setStoreMoneyOff(moneyOff.getDecreasePrice());
            }else cartStoreVo.setStoreMoneyOff(new BigDecimal(0.00));

            cartStoreVo.setGoodsAccount(goodsAccount);
            cartStoreVo.setAccount(cartStoreVo.getGoodsAccount().subtract(cartStoreVo.getStoreMoneyOff()).add(cartStoreVo.getGoodsFreight()));

            cartStoreVo.setCartGoodsVoList(cartGoodsVoList);


            cartStoreVoList.add(cartStoreVo);
        }
        return new Result(cartStoreVoList);
    }


    private String getAuthorizationToken() {
        return JobUtil.getAuthorizationToken(request);
    }
}