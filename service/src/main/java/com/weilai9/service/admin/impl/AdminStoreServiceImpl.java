package com.weilai9.service.admin.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.*;
import com.weilai9.dao.mapper.*;
import com.weilai9.service.admin.AdminStoreService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminStoreServiceImpl implements AdminStoreService {

    @Resource
    StoreMapper storeMapper;
    @Resource
    GoodsMapper goodsMapper;
    @Resource
    GoodsskuMapper goodsskuMapper;
    @Resource
    OrdersMapper ordersMapper;
    @Resource
    StoreStockMapper storeStockMapper;

    @Resource
    OrdergoodsMapper ordergoodsMapper;

    @Override
    public Result addStore(Store store, TokenUser tokenUser) {
        if (store==null){
            return Result.Error(201,"请填写门店信息");
        }
        Integer customerId = tokenUser.getCustomerId().intValue();
        store.setCustomerId(customerId);

        if (store!=null&&store.getStoreId()!=null){
            this.storeMapper.updateById(store);
            return Result.OK();
        }
        else {
            List<Store> storeList = this.storeMapper.selectList(new QueryWrapper<Store>().eq("customerId", customerId));
            if (storeList.size()>0){
                return Result.Error(201,"该用户已存在所属门店");
            }
            this.storeMapper.insert(store);
            return Result.OK();
        }
    }

    @Override
    public Result changeStoreState(Integer state, Integer storeId, TokenUser tokenUser) {
        if (storeId==null){
            Integer customerId = tokenUser.getCustomerId().intValue();
            Store store1 = this.storeMapper.selectOne(new QueryWrapper<Store>().select("storeId")
                    .eq("customerId", customerId)
                    .groupBy("storeId"));
            if (store1==null){
                return Result.Error(201,"该用户无门店");
            }
            storeId = store1.getStoreId();
        }
        Store store = this.storeMapper.selectById(storeId);
        store.setState(state);
        this.storeMapper.updateById(store);
        return Result.OK();
    }

    @Override
    public Result goodsQr(String QrCode, TokenUser tokenUser) {
        Integer customerId = tokenUser.getCustomerId().intValue();
        Store store1 = this.storeMapper.selectOne(new QueryWrapper<Store>().select("storeId")
                .eq("customerId", customerId)
                .groupBy("storeId"));
        if (store1==null){
            return Result.Error(201,"该用户无门店");
        }
        Integer storeId = store1.getStoreId();

        Ordergoods ordergoods = this.ordergoodsMapper.selectOne(new QueryWrapper<Ordergoods>().eq("qr_code_no", QrCode));
        Orders orders = this.ordersMapper.selectById(ordergoods.getOrderid());
        if (null!=orders&&orders.getStoreid()!=storeId){
            return Result.Error(201,"该商品不属于该商家");
        }
        if (orders.getState()!=2){
            return Result.Error(201,"该订单未支付");
        }
        if (ordergoods.getQrState()==1){
            return Result.Error(201,"该商品已被核销");
        }
        ordergoods.setQrState(1);
        this.ordergoodsMapper.updateById(ordergoods);

        return Result.OK("核销成功");
    }

    @Override
    public Result goodsQrInfo(String QrCode, TokenUser tokenUser) {
        Ordergoods ordergoods = this.ordergoodsMapper.selectOne(new QueryWrapper<Ordergoods>().eq("qr_code_no", QrCode));
        Orders orders = this.ordersMapper.selectById(ordergoods.getOrderid());

        Integer customerId = tokenUser.getCustomerId().intValue();
        Store store1 = this.storeMapper.selectOne(new QueryWrapper<Store>().select("storeId")
                .eq("customerId", customerId)
                .groupBy("storeId"));
        if (store1==null){
            return Result.Error(201,"该用户无门店");
        }
        Integer storeId = store1.getStoreId();

        if (orders.getStoreid()!=storeId){
            return Result.Error(201,"该商品不属于该商家");
        }

        if (ordergoods!=null&&ordergoods.getQrState()==1){
            return Result.Error(201,"该商品已被核销");
        }
        Map map = new HashMap();
        map.put("ordergoods",ordergoods);
        Goods goods = this.goodsMapper.selectById(ordergoods.getGoodsid());
        map.put("goods",goods);
        Goodssku goodssku = this.goodsskuMapper.selectById(ordergoods.getGoodsskuid());
        map.put("goodssku",goodssku);
        map.put("qrCode",QrCode);

        return new Result(map);
    }

    @Override
    public Result getStoreList(Integer pageno, Integer pagesize, String storeName) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.ne("state",5);
        if (storeName!=null){
            queryWrapper.like("storeName",storeName);
        }
        Page page = new Page(pageno,pagesize);
        IPage iPage = this.storeMapper.selectPage(page, queryWrapper);
        return new Result(iPage);
    }

    @Override
    public Result changeRem(Integer isRem, Integer storeId) {
        if (isRem==null||storeId==null){
            return Result.Error(201,"参数有误");
        }
        Store store = this.storeMapper.selectById(storeId);
        store.setIsRem(1);
        Integer remNum = this.storeMapper.getMaxRemNum();
        store.setRemNum(remNum);
        this.storeMapper.updateById(store);
        return Result.OK();
    }

}
