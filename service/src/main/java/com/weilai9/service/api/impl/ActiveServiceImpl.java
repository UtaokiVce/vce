package com.weilai9.service.api.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.*;
import com.weilai9.dao.mapper.*;
import com.weilai9.dao.vo.store.AddActiveVO;
import com.weilai9.service.api.ActiveService;
import org.springframework.stereotype.Service;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 活动表(Active)表服务实现类
 *
 * @author makejava
 * @since 2020-04-27 14:22:39
 */
@Service("activeService")
public class ActiveServiceImpl extends ServiceImpl<ActiveMapper, Active> implements ActiveService {

    @Resource
    private ActiveMapper activeMapper;
    @Resource
    private GoodsFreightMapper goodsFreightMapper;
    @Resource
    private StoreMapper storeMapper;
    @Resource
    private SecondTimeMapper secondTimeMapper;
    @Resource
    private StoreMoneyOffMapper storeMoneyOffMapper;

    @Override
    public Result getStoreActiveGoodsList(Integer pageno, Integer pagesize, Integer activeType,Integer state,Integer priceSort,TokenUser tokenUser){
        Integer customerId = tokenUser.getCustomerId().intValue();
        Store store1 = this.storeMapper.selectOne(new QueryWrapper<Store>().select("storeId")
                .eq("customerId", customerId)
                .groupBy("storeId"));
        if (store1==null){
            return Result.Error(201,"该用户无门店");
        }
        Integer storeId = store1.getStoreId();
        IPage page = new Page(null == pageno ? 1 : pageno, null == pagesize ? 10 : pagesize);
        IPage<Map> iPage = activeMapper.getStoreActiveGoodsList(page,storeId,activeType,state,priceSort);
        return new Result(iPage);
    }

    @Override
    public Result doGoodsEnable(Integer activeId,Integer enable){
        int count=activeMapper.update(null,new UpdateWrapper<Active>().set("`enable`",enable).eq("activeId",activeId));
        if(count<=0){
            return Result.Error("失败");
        }
        return Result.OK("成功");
    }

    @Override
    public Result saveActive(AddActiveVO addActiveVO, TokenUser tokenUser){
        if(addActiveVO==null){
            return Result.Error("参数有误");
        }


        Integer customerId = tokenUser.getCustomerId().intValue();
        Store store1 = this.storeMapper.selectOne(new QueryWrapper<Store>().select("storeId")
                .eq("customerId", customerId)
                .groupBy("storeId"));
        if (store1==null){
            return Result.Error(201,"该用户无门店");
        }
        Integer storeId = store1.getStoreId();

        int num=activeMapper.getActiveCount(storeId,addActiveVO.getGoodsskuid(),addActiveVO.getBegintime(),addActiveVO.getEndtime());
        if(num>0){
            return Result.Error("此时间段内已有活动");
        }

        List<Active> actives = this.activeMapper.selectList(new QueryWrapper<Active>().eq("goodsId", addActiveVO.getGoodsid()).eq("goodsSkuId", addActiveVO.getGoodsskuid()).eq("enable", 1));
        if (actives.size()>0){
            return Result.Error(201,"该产品已参加其他活动");
        }


        Active active=new Active();
        active.setActivetype(addActiveVO.getActivetype());
        active.setPrice(addActiveVO.getPrice());
        active.setAccountPrice(addActiveVO.getAccountPrice());
        active.setGoodsid(addActiveVO.getGoodsid());
        active.setGoodsskuid(addActiveVO.getGoodsskuid());
        active.setStoreid(storeId);
        active.setOneprice(addActiveVO.getOneprice());
        BigDecimal changePrice = (addActiveVO.getPrice().subtract(addActiveVO.getAccountPrice()).subtract(addActiveVO.getOneprice())).multiply(new BigDecimal(10)).divide(new BigDecimal(3),2);
        active.setChangeprice(changePrice);
        active.setJoinnum(addActiveVO.getJoinnum());
        active.setIsonly(addActiveVO.getIsonly());
        active.setOnlynum(addActiveVO.getOnlynum());
        DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            active.setBegintime(df.parse(addActiveVO.getBegintime()));
            active.setEndtime(df.parse(addActiveVO.getEndtime()));
        }catch (Exception e){}
        active.setEnable(0);
        int count=activeMapper.insert(active);
        if(count<=0){
            return Result.Error("失败");
        }
        return Result.OK("成功");
    }

    @Override
    public Result getSecendTimeList(){
        List<Map<String,Object>> dataList=this.secondTimeMapper.getSecendTimeList();
        if(dataList==null){
            dataList=new ArrayList<>();
        }
        return new Result(dataList);
    }

    //秒杀商品结算页
    @Override
    public Result SecendGoodsAccount(Integer storeId, String storeName, String addr, Integer goodsId, Integer goodsSkuId, String title, String skuName, BigDecimal price, String headImg, BigDecimal changePrice,Integer num) {
        Map map = new HashMap();
        if (num==null){
            num=1;
        }
        storeName = storeName.replace("\"", "");
        addr = addr.replace("\"", "");
        title = title.replace("\"", "");
        skuName = skuName.replace("\"", "");
        headImg = headImg.replace("\"", "");
        map.put("storeId",storeId);
        map.put("storeName",storeName);
        map.put("addr",addr);
        map.put("goodsId",goodsId);
        map.put("goodsSkuId",goodsSkuId);
        map.put("title",title);
        map.put("skuName",skuName);
        map.put("price",price);
        map.put("headImg",headImg);
        map.put("changePrice",changePrice);
        map.put("num",num);
        GoodsFreight freight = this.goodsFreightMapper.selectOne(new QueryWrapper<GoodsFreight>().eq("storeId", storeId));
        map.put("goodsFreight",freight.getFreight());
        BigDecimal multiply = price.multiply(new BigDecimal(num));
        map.put("realMoney",multiply);
        map.put("realMoneyByFreight",multiply.add(freight.getFreight()));


        return new Result(map);

    }

    @Override
    public Result getSecendGoodsList(Integer pageno,Integer pagesize,String timename){
        IPage page = new Page(null == pageno ? 1 : pageno, null == pagesize ? 10 : pagesize);
        IPage<Map> iPage = activeMapper.getSecendGoodsList(page,timename);
        return new Result(iPage);
    }


    @Override
    public Result getGroupsGoodsList(Integer pageno,Integer pagesize,String title,Integer newtime,Integer shopnum,Integer inte,Integer price){
        IPage page = new Page(null == pageno ? 1 : pageno, null == pagesize ? 10 : pagesize);
        IPage<Map> iPage = activeMapper.getGroupsGoodsList(page,title,newtime,shopnum,inte,price);
        return new Result(iPage);
    }

    @Override
    public Result addMoneyOff(StoreMoneyOff storeMoneyOff, TokenUser tokenUser) {
        if(storeMoneyOff==null){
            return Result.Error("参数有误");
        }


        Integer customerId = tokenUser.getCustomerId().intValue();
        Store store1 = this.storeMapper.selectOne(new QueryWrapper<Store>().select("storeId")
                .eq("customerId", customerId)
                .groupBy("storeId"));
        if (store1==null){
            return Result.Error(201,"该用户无门店");
        }
        Integer storeId = store1.getStoreId();

        storeMoneyOff.setStoreId(storeId);
        int insert = this.storeMoneyOffMapper.insert(storeMoneyOff);
        if (insert>0){
            return Result.OK();
        }
        else return new Result(201,"添加失败");
    }

    @Override
    public Result addMoneyOffChangeState(Integer moneyOffId, Integer state, TokenUser tokenUser) {
        if(moneyOffId==null||state==null){
            return Result.Error("参数有误");
        }


        Integer customerId = tokenUser.getCustomerId().intValue();
        Store store1 = this.storeMapper.selectOne(new QueryWrapper<Store>().select("storeId")
                .eq("customerId", customerId)
                .groupBy("storeId"));
        if (store1==null){
            return Result.Error(201,"该用户无门店");
        }

        StoreMoneyOff storeMoneyOff = this.storeMoneyOffMapper.selectById(moneyOffId);
        storeMoneyOff.setState(state);
        this.storeMoneyOffMapper.updateById(storeMoneyOff);

        return Result.OK("更改成功");

    }

    @Override
    public Result storeMoneyOffList(TokenUser tokenUser,Integer pageno,Integer pagesize,Integer state) {

        Integer customerId = tokenUser.getCustomerId().intValue();
        Store store1 = this.storeMapper.selectOne(new QueryWrapper<Store>().select("storeId")
                .eq("customerId", customerId)
                .groupBy("storeId"));
        if (store1==null){
            return Result.Error(201,"该用户无门店");
        }
        Integer storeId = store1.getStoreId();
        Page page = new Page(pageno,pagesize);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("storeId",storeId);
        if (state!=null&&state==1){
            queryWrapper.eq("state",1);
        }
        else if (state!=null&&state==0){
            queryWrapper.eq("state",0);
        }
        IPage iPage = this.storeMoneyOffMapper.selectPage(page,queryWrapper);
        return new Result(iPage);
    }

    @Override
    public Result activeChangeState(Integer activeId,Integer state, TokenUser tokenUser) {
        if(activeId==null||state==null){
            return Result.Error("参数有误");
        }


        Integer customerId = tokenUser.getCustomerId().intValue();
        Store store1 = this.storeMapper.selectOne(new QueryWrapper<Store>().select("storeId")
                .eq("customerId", customerId)
                .groupBy("storeId"));
        if (store1==null){
            return Result.Error(201,"该用户无门店");
        }

        Active active = this.activeMapper.selectById(activeId);
        active.setEnable(state);
        this.activeMapper.updateById(active);

        return Result.OK("更改成功");

    }

    @Override
    public Result addSecondTime(SecondTime secondTime) {
        if (secondTime==null){
            return Result.Error(201,"填写信息为空");
        }
        secondTime.setEnable(1);
        this.secondTimeMapper.insert(secondTime);
        return Result.OK();
    }

    @Override
    public Result getSecondTime(Integer pageno,Integer pagesize,Integer enable) {
        Page page = new Page(pageno,pagesize);
        QueryWrapper queryWrapper = new QueryWrapper();
        if (enable!=null){
            queryWrapper.eq("enable",enable);
        }
        IPage iPage = this.secondTimeMapper.selectPage(page, queryWrapper);
        return new Result(iPage);
    }

    @Override
    public Result changeSecondTimeState(Integer secondTimeId, Integer enable) {
        SecondTime secondTime = this.secondTimeMapper.selectById(secondTimeId);
        secondTime.setEnable(enable);
        this.secondTimeMapper.updateById(secondTime);
        return Result.OK();
    }
}