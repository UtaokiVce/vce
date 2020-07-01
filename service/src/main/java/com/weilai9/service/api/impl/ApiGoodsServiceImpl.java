package com.weilai9.service.api.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.common.utils.wechat.JobUtil;
import com.weilai9.common.utils.wechat.RedisHandle;
import com.weilai9.dao.entity.*;
import com.weilai9.dao.mapper.*;
import com.weilai9.dao.vo.store.AddGoodsVO;
import com.weilai9.dao.vo.store.AddGoodsskuVO;
import com.weilai9.service.api.ApiGoodsService;
import com.weilai9.service.api.GoodscommentService;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * (User)表服务实现类
 *
 * @author china.fuyao@outlook.com
 * @since 2020-03-25 18:20:48
 */
@Slf4j
@Service("apiGoodsService")
public class ApiGoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements ApiGoodsService {

    @Resource
    private GoodsMapper goodsMapper;
    @Resource
    private StoreMapper storeMapper;
    @Resource
    private GoodsskuMapper goodsskuMapper;
    @Resource
    private StoreStockMapper storeStockMapper;
    @Resource
    private StoreStockHistoryMapper storeStockHistoryMapper;
    @Resource
    private GoodscommentService goodscommentService;
    @Resource
    private ParentorderMapper parentorderMapper;
    @Resource
    private ActiveMapper activeMapper;
    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private WebBannerMapper webBannerMapper;
    @Resource
    private CustomerMapper customerMapper;

    @Resource
    private HttpServletRequest request;
    @Resource
    private RedisHandle redisHandle;

    @Override
    public Result getCommendGoodsList(Integer pageno, Integer pagesize) {
        IPage page = new Page(null == pageno ? 1 : pageno, null == pagesize ? 10 : pagesize);
        IPage<Map> iPage = goodsMapper.getCommendGoodsList(page);
        return new Result(iPage);
    }

    @Override
    public Result getCommendGoodsList() {
        List<Goods> isRemList = this.goodsMapper.AdminGetCommendGoodsList();
        return new Result(isRemList);
    }

    @Override
    public Result commendGoodsSort(Integer id, Integer operation) {
        //当前商品
        Goods goods = this.goodsMapper.selectById(id);
        //当前排序
        Integer currSort = goods.getRemNum();
        //上移 当前sort+1  将上一条数据sort-1
        if(operation == 1){
            if(currSort == 4){
                return Result.Error("操作失败，已经是第一位了！");
            }
            goods.setRemNum(goods.getRemNum()+1);
            Goods goods1 = this.goodsMapper.selectOne(new QueryWrapper<Goods>().eq("remNum", currSort + 1));

            if(goods1 != null){
                goods1.setRemNum(currSort);
                this.goodsMapper.updateById(goods1);
            }
            this.goodsMapper.updateById(goods);
            return Result.OK("操作成功！");

        }else if(operation == 2){
            //下移 当前sort-1  将下一条数据sort+1
            if(currSort == 1){
                return Result.Error("操作失败，已经是最后一位了！");
            }
            goods.setRemNum(goods.getRemNum()-1);
            Goods goods1 = this.goodsMapper.selectOne(new QueryWrapper<Goods>().eq("remNum", currSort - 1));

            if(goods1 != null){
                goods1.setRemNum(currSort);
                this.goodsMapper.updateById(goods1);
            }
            this.goodsMapper.updateById(goods);
            return Result.OK("操作成功！");
        }else if(operation == 3){
            //置顶 当前sort设为4，比他高的-1
            List<Goods> goodsList1 = this.goodsMapper.selectList(new QueryWrapper<Goods>().eq("isRem", 1));
            Integer size = goodsList1.size();
            if(currSort == size){
                return Result.Error("置顶失败，已经是第一位了！");
            }
            goods.setRemNum(size);
            List<Goods> goodsList = list(new QueryWrapper<Goods>().gt("remNum", currSort));
            if(goodsList.size()>0){
                goodsList.forEach(goods2->{
                    goods2.setRemNum(goods2.getRemNum()-1);
                    this.goodsMapper.updateById(goods2);
                });
            }
            this.goodsMapper.updateById(goods);
            return Result.OK("操作成功！");
        }

        return Result.Error("操作失败");
    }

    @Override
    public Result getStoreGoodsList(TokenUser tokenUser, Integer pageno, Integer pagesize, String goodsName, Integer state, Integer oneType, Integer twoType, Integer priceSort) {

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


        IPage page = new Page(null == pageno ? 1 : pageno, null == pagesize ? 10 : pagesize);
        IPage<Map> iPage = goodsMapper.getStoreGoodsList(page, storeId, goodsName, state, oneType, twoType, priceSort);
        return new Result(iPage);
    }

    @Override
    public Result getAdminGoodsList(Integer pageno, Integer pagesize, String goodsName, Integer oneType, Integer twoType) {
        IPage page = new Page(null == pageno ? 1 : pageno, null == pagesize ? 10 : pagesize);
        IPage<Map> iPage = goodsMapper.getAdminGoodsList(page, goodsName, oneType, twoType);
        return new Result(iPage);
    }

    @Override
    public Result getAdminTryGoodsList(Integer pageno, Integer pagesize, String goodsName, Integer oneType, Integer twoType) {
        IPage page = new Page(null == pageno ? 1 : pageno, null == pagesize ? 10 : pagesize);
        IPage<Map> iPage = goodsMapper.getAdminTryGoodsList(page, goodsName, oneType, twoType);
        return new Result(iPage);
    }

    @Override
    @Transactional
    public Result saveGoods(TokenUser tokenUser, AddGoodsVO addGoodsVO) {

        Integer customerId = tokenUser.getCustomerId().intValue();
        Store store1 = this.storeMapper.selectOne(new QueryWrapper<Store>().select("storeId")
                .eq("customerId", customerId)
                .groupBy("storeId"));
        if (store1 == null) {
            return Result.Error(201, "该用户无门店");
        }
        Integer storeId = store1.getStoreId();

        if (storeId == null) {
            return Result.Error("门店信息有误");
        }
        Store store = storeMapper.selectById(storeId);
        if (store == null) {
            return Result.Error("门店信息有误");
        }
        if (store.getState() == null || store.getState().intValue() == 0) {
            return Result.Error("门店已禁用");
        }

        //添加商品信息
        Goods goods = new Goods();
        goods.setCateOne(addGoodsVO.getCateOne());
        goods.setCateTwo(addGoodsVO.getCateTwo());
        goods.setTitle(addGoodsVO.getTitle());
        goods.setHeadImg(addGoodsVO.getHeadImg());
        goods.setDs(addGoodsVO.getDs());
        goods.setRetreat(addGoodsVO.getRetreat());
        goods.setBannerImg(addGoodsVO.getBannerImg());
        goods.setGoodsType(addGoodsVO.getGoodsType());
        goods.setDetail(addGoodsVO.getDetail());
        goods.setDetailImg(addGoodsVO.getDetailImg());

        if (addGoodsVO.getGoodsId() != null && addGoodsVO.getGoodsId().intValue() > 0) {
            goodsMapper.update(goods, new UpdateWrapper<Goods>().eq("goodsId", addGoodsVO.getGoodsId()));
        } else {
            //默认审核中
            goods.setState(2);
            goodsMapper.insert(goods);
        }

        Integer goodsId = goods.getGoodsId();
        //添加规格信息
        List<Integer> skuIds = new ArrayList<Integer>();

        Goodssku goodssku = new Goodssku();
        goodssku.setGoodsId(goodsId);
        goodssku.setSkuName(addGoodsVO.getSkuName());
        goodssku.setPrice(addGoodsVO.getPrice());
        goodssku.setMarkPrice(addGoodsVO.getMarkPrice());
        goodssku.setAccountPrice(addGoodsVO.getAccountPrice());
        goodssku.setOnePrice(addGoodsVO.getOnePrice());
        BigDecimal changePrice = (addGoodsVO.getPrice().subtract(addGoodsVO.getAccountPrice()).subtract(addGoodsVO.getOnePrice())).multiply(new BigDecimal(10)).divide(new BigDecimal(3),2);
        goodssku.setChangePrice(changePrice);
        if (addGoodsVO.getGoodsSkuId() != null && addGoodsVO.getGoodsSkuId().intValue() > 0) {
            goodsskuMapper.update(goodssku, new UpdateWrapper<Goodssku>().eq("goodsSkuId", addGoodsVO.getGoodsSkuId()));
        } else {
            goodsskuMapper.insert(goodssku);
            skuIds.add(goodssku.getGoodsSkuId());
        }


        //添加库存信息
        for (Integer goodsskuId : skuIds) {
            StoreStock storeStock = new StoreStock();
            storeStock.setStoreId(storeId);
            storeStock.setGoodsSkuId(goodsskuId);
            storeStock.setGoodsId(goodsId);
            storeStock.setNum(addGoodsVO.getGoodsSkuNum());
            storeStockMapper.insert(storeStock);
        }

        return Result.OK("操作成功");
    }

    @Override
    public Result saveAdminGoods(AddGoodsVO addGoodsVO) {
//        List<AddGoodsskuVO> skuList=addGoodsVO.getSkuList();
//        if(skuList==null || skuList.size()==0){
//            return Result.Error("规格信息有误");
//        }
//        //添加商品信息
//        Goods goods=new Goods();
//        goods.setCateOne(addGoodsVO.getCateOne());
//        goods.setCateTwo(addGoodsVO.getCateTwo());
//        goods.setTitle(addGoodsVO.getTitle());
//        goods.setSelf(1);
//        goods.setHeadImg(addGoodsVO.getHeadImg());
//        goods.setDs(addGoodsVO.getDs());
//        goods.setRetreat(addGoodsVO.getRetreat());
//        goods.setBannerImg(addGoodsVO.getBannerImg());
//        goods.setGoodsType(addGoodsVO.getGoodsType());
//        goods.setDetail(addGoodsVO.getDetail());
//        goods.setDetailImg(addGoodsVO.getDetailImg());
//
//        if(addGoodsVO.getGoodsId()!=null && addGoodsVO.getGoodsId().intValue()>0){
//            goodsMapper.update(goods,new UpdateWrapper<Goods>().eq("goodsId",addGoodsVO.getGoodsId()));
//        }else{
//            //默认审核通过
//            goods.setState(1);
//            goodsMapper.insert(goods);
//        }
//
//        Integer goodsId=goods.getGoodsId();
//        //添加规格信息
//        List<Integer> skuIds=new ArrayList<Integer>();
//        for(AddGoodsskuVO v:skuList){
//            Goodssku goodssku=new Goodssku();
//            goodssku.setGoodsId(goodsId);
//            goodssku.setSkuName(v.getSkuName());
//            goodssku.setPrice(v.getPrice());
//            goodssku.setMarkPrice(v.getMarkPrice());
//            goodssku.setAccountPrice(v.getAccountPrice());
//            goodssku.setOnePrice(v.getOnePrice());
//            goodssku.setChangePrice(v.getChangePrice());
//            if(v.getGoodsSkuId()!=null && v.getGoodsSkuId().intValue()>0){
//                goodsskuMapper.update(goodssku,new UpdateWrapper<Goodssku>().eq("goodsSkuId",v.getGoodsSkuId()));
//            }else{
//                goodsskuMapper.insert(goodssku);
//                skuIds.add(goodssku.getGoodsSkuId());
//            }
//        }
//        //添加库存信息
//        if(skuIds.size()>0){
//            List<Store> storeList=storeMapper.selectList(null);
//            if(storeList!=null && storeList.size()>0){
//                for(Store s:storeList){
//                    for(Integer goodsskuId:skuIds){
//                        StoreStock storeStock=new StoreStock();
//                        storeStock.setStoreId(s.getStoreId());
//                        storeStock.setGoodsSkuId(goodsskuId);
//                        storeStock.setGoodsId(goodsId);
//                        storeStock.setNum(0);
//                        storeStockMapper.insert(storeStock);
//                    }
//                }
//            }
//        }
        return Result.OK("操作成功");
    }

    @Override
    public Result doGoodsEnable(Integer goodsSkuId, Integer goodsId, int enable) {
        if (enable == 1) {
            //修改商品
            goodsMapper.update(null, new UpdateWrapper<Goods>().set("`enable`", 1).eq("goodsId", goodsId));
            goodsskuMapper.update(null, new UpdateWrapper<Goodssku>().set("`enable`", 1).eq("goodsSkuId", goodsSkuId));
        } else {
            goodsskuMapper.update(null, new UpdateWrapper<Goodssku>().set("`enable`", 0).eq("goodsSkuId", goodsSkuId));
            //校验商品下架
            checkGoodsEnable(goodsId);
        }
        return Result.OK("成功");
    }

    @Override
    public Result doGoodsAdminTry(Integer goodsId, Integer goodsSkuId, int state) {
        int count = goodsskuMapper.update(null, new UpdateWrapper<Goodssku>().set("enable", state).eq("goodsId", goodsId).eq("goodsSkuId", goodsSkuId));
        if (count <= 0) {
            return Result.Error("失败");
        }
        return Result.OK("成功");
    }

    /**
     * 校验商品是否应该下架
     *
     * @param goodsId
     */
    private void checkGoodsEnable(Integer goodsId) {
        int count = goodsskuMapper.selectCount(new QueryWrapper<Goodssku>().eq("goodsId", goodsId).eq("`enable`", 1));
        if (count == 0) {
            goodsMapper.update(null, new UpdateWrapper<Goods>().set("`enable`", 0).eq("goodsId", goodsId));
        }
    }

    @Override
    @Transactional
    public Result doStoreStockInOrOut(TokenUser tokenUser, Integer goodsSkuId, Integer goodsId, Integer num, Integer type, String remark) {
        if (type == null) {
            return Result.Error("类型错误");
        }

        Integer customerId = tokenUser.getCustomerId().intValue();
        Store store1 = this.storeMapper.selectOne(new QueryWrapper<Store>().select("storeId")
                .eq("customerId", customerId)
                .groupBy("storeId"));
        if (store1 == null) {
            return Result.Error(201, "该用户无门店");
        }
        Integer storeId = store1.getStoreId();

        //添加记录
        Storestockhistory storestockhistory = new Storestockhistory();
        if (type.intValue() == 1) {
            //入库
            int count = storeStockMapper.update(null, new UpdateWrapper<StoreStock>().eq("storeId", storeId).eq("goodsSkuId", goodsSkuId).setSql("num=num+" + num.intValue()));
            if (count <= 0) {
                return Result.Error("操作失败");
            }
            storestockhistory.setType(1);
        } else {
            //出库
            int count = storeStockMapper.update(null, new UpdateWrapper<StoreStock>().eq("storeId", storeId).eq("goodsSkuId", goodsSkuId).ge("num", num.intValue()).setSql("num=num-" + num.intValue()));
            if (count <= 0) {
                return Result.Error("操作失败");
            }
            storestockhistory.setType(2);
        }
        storestockhistory.setStoreId(storeId);
        storestockhistory.setGoodsSkuId(goodsSkuId);
        storestockhistory.setGoodsId(goodsId);
        storestockhistory.setNum(num);
        storestockhistory.setRemark(remark);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        storestockhistory.setAddTime(df.format(new Date()));
        storeStockHistoryMapper.insert(storestockhistory);
        return Result.OK("操作成功");
    }

    @Override
    public Result getShopGoodsList(Integer pageno, Integer pagesize, String title, Integer newtime, Integer shopnum, Integer inte, Integer price, Integer cateTwo, Integer cateOne) {
        IPage page = new Page(null == pageno ? 1 : pageno, null == pagesize ? 10 : pagesize);
        IPage<Map> iPage = goodsMapper.getShopGoodsList(page, title, newtime, shopnum, inte, price, cateTwo, cateOne);
        return new Result(iPage);
    }

    @Override
    public Result getGoodsInfo(Integer goodsId) {
        if (goodsId==null){
            return Result.Error(201,"参数有误");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        Goods goods = goodsMapper.selectById(goodsId);
        map.put("goods", goods);
        List<Goodssku> goodsskuList = goodsskuMapper.selectList(new QueryWrapper<Goodssku>().eq("goodsId", goodsId).eq("`enable`", 1).orderByAsc("price"));
        map.put("goodsskuList", goodsskuList);
        Result result = goodscommentService.getStoreGoodsCommentList(1, 1, null, null, goodsId);
        map.put("commentData", result.getData());
        map.put("goodNum", result.getMsg());
        IPage page = new Page(1, 4);
        IPage<Map> iPage = goodsMapper.getCommendGoodsList(page);
        map.put("yourLike", iPage.getRecords());
        map.put("goodNum", result.getMsg());
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select("storeId");
        queryWrapper.groupBy("storeId");
        queryWrapper.eq("goodsId", goods.getGoodsId());
        StoreStock storeStock = this.storeStockMapper.selectOne(queryWrapper);
        map.put("storeId", storeStock.getStoreId());
        Store store = this.storeMapper.selectById(storeStock.getStoreId());
        map.put("storeName", store.getStoreName());
        map.put("storeAddr", store.getAddr());
        return new Result(map);
    }

    @Override
    public Result getGroupsGoodsInfo(Integer goodsId, Integer goodsSkuId, Integer activeId, Long customerId) {
        Map<String, Object> map = new HashMap<String, Object>();
        Active active = activeMapper.selectById(activeId);
        if (active == null) {
            return Result.Error("活动不存在");
        }
        if (active.getGoodsskuid().intValue() != goodsSkuId.intValue()) {
            return Result.Error("活动信息有误");
        }
        Goods goods = goodsMapper.selectById(goodsId);
        map.put("goods", goods);

        List<Goodssku> goodsskuList = goodsskuMapper.selectList(new QueryWrapper<Goodssku>().eq("goodsSkuId", goodsSkuId).eq("`enable`", 1));
        Goodssku goodssku = goodsskuList.get(0);
        goodssku.setMarkPrice(goodssku.getPrice());
        goodssku.setPrice(active.getPrice());
        goodssku.setChangePrice(active.getChangeprice());

        map.put("goodsskuList", goodsskuList);
        Result result = goodscommentService.getStoreGoodsCommentList(1, 1, null, null, goodsId);
        //门店评价列表
        map.put("commentData", result.getData());
        //查询总评论条数，根据goodsid查询
        int count = goodscommentService.count(new QueryWrapper<Goodscomment>().eq("goodsId", goodsId));
        map.put("commentCount", count);

        //店铺分
        map.put("avgScore", result.getMsg());
        IPage page = new Page(1, 4);
        IPage<Map> iPage = goodsMapper.getCommendGoodsList(page);
        map.put("yourLike", iPage.getRecords());
        map.put("goodNum", result.getMsg());
        //小团订单
        List<Map<String, Object>> groupList = parentorderMapper.getCanJoinList(activeId);


        groupList.forEach(map1 -> {
            String ids = (String) map1.get("ids");
            //不能拼自己的单
            if (ids.contains("," + customerId) || ids.contains(customerId + ",")) {
                map1.put("status", "no");
            } else if (ids.contains(customerId + "") && !(ids.contains(","))) {
                map1.put("status", "no");
            } else {
                map1.put("status", "yes");
            }
            String imgs = (String) map1.get("imgs");
            if (imgs != null) {
                //头像集合
                String[] split = imgs.split(",");
                List<String> list = Arrays.asList(split);
                map1.put("imgs", list);
            }
        });

        map.put("groupList", groupList);
        map.put("activeId", activeId);
        return new Result(map);
    }

    @Override
    public Result getGoodsSort() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("parentId", 0);
        queryWrapper.orderByAsc("orderIndex");
        List<Category> categoryList = this.categoryMapper.selectList(queryWrapper);
        return new Result(categoryList);
    }

    @Override
    public Result getGoodsSortByPid(Integer parentId) {
        if (parentId != null) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("parentId", parentId);
            queryWrapper.orderByAsc("orderIndex");
            List<Category> categoryList = this.categoryMapper.selectList(queryWrapper);
            return new Result(categoryList);
        } else return Result.Error("id为空");

    }

    @Override
    public Result getShoppingBanner() {
        List<Webbanner> webbanners = this.webBannerMapper.selectList(new QueryWrapper<Webbanner>().eq("type", 3)
                .orderByAsc("orderindex"));
        return new Result(webbanners);
    }

    @Override
    public Result storeStockHistory(TokenUser tokenUser, Integer pageno, Integer pagesize, Integer goodsId, Integer goodsSkuId) {
        Integer customerId = tokenUser.getCustomerId().intValue();
        Store store1 = this.storeMapper.selectOne(new QueryWrapper<Store>().select("storeId")
                .eq("customerId", customerId)
                .groupBy("storeId"));
        if (store1 == null) {
            return Result.Error(201, "该用户无门店");
        }
        Integer storeId = store1.getStoreId();

        List<Storestockhistory> storestockhistories = this.storeStockHistoryMapper.selectList(new QueryWrapper<Storestockhistory>().eq("storeId", storeId).eq("goodsId", goodsId).eq("goodsSkuId", goodsSkuId));
        return new Result(storestockhistories);
    }

    @Override
    public Result storeList(Integer pageno, Integer pagesize, String addr, Integer storeType) {
        Page ipage = new Page(pageno, pagesize);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("state", 1);
        if (addr != null) {
            queryWrapper.like("addr", addr);
        }
        if (storeType != null && storeType == 1) {
            queryWrapper.eq("storeType", 1);
        }
        if (storeType != null && storeType == 2) {
            queryWrapper.eq("storeType", 2);
        }
        IPage iPage = this.storeMapper.selectPage(ipage, queryWrapper);
        return new Result(iPage);
    }

    @Override
    public Result storeInfo(Integer storeId) {
        Map map = new HashMap();
        Store store = this.storeMapper.selectById(storeId);

        map.put("store", store);
        List<Map> storeGoodsImg = this.goodsMapper.getStoreGoodsImg(storeId);
        map.put("goodsImg", storeGoodsImg);
        return new Result(map);
    }

    @Override
    public Result goods2Rem(Integer goodsId,Integer isRem) {
        if (goodsId==null||isRem==null){
            return Result.Error(201,"参数有误");
        }
        Integer maxRemNum = this.goodsMapper.getMaxRemNum();
        if (maxRemNum!=null&&maxRemNum>=4){
            return Result.Error(201,"已到最大推荐数");
        }
        if (maxRemNum==null){
            maxRemNum=0;
        }
        Goods goods = this.goodsMapper.selectById(goodsId);
        goods.setIsRem(isRem);
        goods.setRemNum(maxRemNum+1);
        this.goodsMapper.updateById(goods);
        return Result.OK();
    }

    @Override
    public Result goods2NotRem(Integer goodsId) {
        if (goodsId==null){
            return Result.Error(201,"参数有误");
        }
        Goods goods = this.goodsMapper.selectById(goodsId);



        List<Goods> goodsList = this.goodsMapper.selectList(new QueryWrapper<Goods>().ge("remNum", goods.getRemNum()));
        for (Goods g : goodsList) {
            g.setRemNum(g.getRemNum()-1);
            this.goodsMapper.updateById(g);
        }
        goods.setIsRem(0);
        goods.setRemNum(0);
        this.goodsMapper.updateById(goods);
        return Result.OK();
    }
}