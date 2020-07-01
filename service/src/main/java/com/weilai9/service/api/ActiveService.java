package com.weilai9.service.api;

import com.baomidou.mybatisplus.extension.service.IService;
import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.Active;
import com.weilai9.dao.entity.SecondTime;
import com.weilai9.dao.entity.StoreMoneyOff;
import com.weilai9.dao.vo.store.AddActiveVO;
import springfox.documentation.annotations.ApiIgnore;

import java.math.BigDecimal;

/**
 * 活动表(Active)表服务接口
 *
 * @author makejava
 * @since 2020-04-27 14:22:36
 */
public interface ActiveService extends IService<Active>{

    /**
     *  门店活动商品列表
     * @param pageno
     * @param pagesize
     * @param activeType
     * @return
     */
    public Result getStoreActiveGoodsList(Integer pageno, Integer pagesize, Integer activeType,Integer state,Integer priceSort,TokenUser tokenUser);

    /**
     *  活动上下架
     * @param activeId
     * @param enable
     * @return
     */
    public Result doGoodsEnable(Integer activeId,Integer enable);

    /**
     * 新增活动
     * @param addActiveVO
     * @return
     */
    public Result saveActive(AddActiveVO addActiveVO, TokenUser tokenUser);

    /**
     *  秒杀时间段列表
     * @return
     */
    public Result getSecendTimeList();

    /**
     * 秒杀商品结算页
     * @param storeId
     * @param storeName
     * @param addr
     * @param goodsId
     * @param goodsSkuId
     * @param title
     * @param skuName
     * @param price
     * @param headImg
     * @param changePrice
     * @return
     */
    Result SecendGoodsAccount(Integer storeId, String storeName, String addr, Integer goodsId, Integer goodsSkuId, String title, String skuName, BigDecimal price, String headImg, BigDecimal changePrice, Integer num);

    /**
     *  秒杀商品列表
     * @param pageno
     * @param pagesize
     * @param timename
     * @return
     */
    public Result getSecendGoodsList(Integer pageno,Integer pagesize,String timename);

    /**
     * 团购商品列表
     * @param pageno
     * @param pagesize
     * @param title
     * @param newtime
     * @param shopnum
     * @param inte
     * @param price
     * @return
     */
    public Result getGroupsGoodsList(Integer pageno,Integer pagesize,String title,Integer newtime,Integer shopnum,Integer inte,Integer price);


    /**
     * 添加满减规则
     * @param storeMoneyOff
     * @param tokenUser
     * @return
     */
    Result addMoneyOff(StoreMoneyOff storeMoneyOff,TokenUser tokenUser);


    /**
     * 更改满减活动状态
     * @param moneyOffId
     * @param state
     * @param tokenUser
     * @return
     */
    Result addMoneyOffChangeState(Integer moneyOffId,Integer state,TokenUser tokenUser);

    /**
     * 门店商品满减活动列表
     * @param tokenUser
     * @return
     */
    Result storeMoneyOffList(TokenUser tokenUser,Integer pageno,Integer pagesize,Integer state);

    /**
     * 门店团购秒杀活动启用禁用
     * @param activeId
     * @return
     */
    Result activeChangeState(Integer activeId,Integer state,TokenUser tokenUser);


    /**
     * 后台添加秒杀时间段
     * @param secondTime
     * @return
     */
    Result addSecondTime(SecondTime secondTime);


    /**
     * 后台获取秒杀时间段
     * @param
     * @return
     */
    Result getSecondTime(Integer pageno,Integer pagesize,Integer enable);

    /**
     * 后台启用/禁用秒杀时间段
     * @param
     * @return
     */
    Result changeSecondTimeState(Integer secondTimeId,Integer enable);
}
