package com.weilai9.service.api;


import com.baomidou.mybatisplus.extension.service.IService;
import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.Goods;
import com.weilai9.dao.entity.Store;
import com.weilai9.dao.vo.store.AddGoodsVO;
import org.springframework.http.HttpRequest;

import java.awt.color.ICC_Profile;

/**
 * (User)表服务接口
 *
 * @author china.fuyao@outlook.com
 * @since 2020-03-25 18:20:47
 */
public interface ApiGoodsService extends IService<Goods> {

    /**
     *  首页推荐商品列表
     * @return
     */
    public Result getCommendGoodsList(Integer pageno,Integer pagesize);

    /**
     *  后台推荐商品列表
     * @return
     */
    Result getCommendGoodsList();


    /**
     * 后台推荐商品排序
     * @param id
     * @param operation
     * @return
     */
    Result commendGoodsSort(Integer id,Integer operation);

    /**
     *  门店商品列表
     * @param pageno
     * @param pagesize
     * @param
     * @param goodsName
     * @param state
     * @param oneType
     * @param twoType
     * @param priceSort
     * @return
     */
    public Result getStoreGoodsList(TokenUser tokenUser, Integer pageno, Integer pagesize, String goodsName, Integer state, Integer oneType, Integer twoType, Integer priceSort);

    /**
     *  管理后台商品列表
     * @param pageno
     * @param pagesize
     * @param goodsName
     * @param oneType
     * @param twoType
     * @return
     */
    public Result getAdminGoodsList(Integer pageno,Integer pagesize,String goodsName,Integer oneType,Integer twoType);

    /**
     * 管理后台商品审核列表
     * @param pageno
     * @param pagesize
     * @param goodsName
     * @param oneType
     * @param twoType
     * @return
     */
    public Result getAdminTryGoodsList(Integer pageno,Integer pagesize,String goodsName,Integer oneType,Integer twoType);

    /**
     *  门店端新增商品
     * @param addGoodsVO
     * @return
     */
    public Result saveGoods(TokenUser tokenUser,AddGoodsVO addGoodsVO);

    /**
     *  后台添加自营商品
     * @param addGoodsVO
     * @return
     */
    public Result saveAdminGoods(AddGoodsVO addGoodsVO);

    /**
     *  商品上下架
     * @param goodsSkuId
     * @param goodsId
     * @param enable
     * @return
     */
    public Result doGoodsEnable(Integer goodsSkuId,Integer goodsId,int enable);

    /**
     *  后台商品审核
     * @param goodsId
     * @param state
     * @return
     */
    public Result doGoodsAdminTry(Integer goodsId,Integer goodsSkuId,int state);

    /**
     *  门店商品出入库
     * @param goodsSkuId
     * @param
     * @param num
     * @param type
     * @param remark
     * @return
     */
    public Result doStoreStockInOrOut(TokenUser tokenUser,Integer goodsSkuId,Integer goodsId,Integer num,Integer type,String remark);

    /**
     *  用户端商品列表
     * @param pageno
     * @param pagesize
     * @param title
     * @param newtime
     * @param shopnum
     * @param inte
     * @param price
     * @return
     */
    public Result getShopGoodsList(Integer pageno,Integer pagesize,String title,Integer newtime,Integer shopnum,Integer inte,Integer price,Integer cateTwo,Integer cateOne);

    /**
     *  商品详情
     * @param goodsId
     * @return
     */
    public Result getGoodsInfo(Integer goodsId);

    /**
     *  团购商品详情
     * @param goodsId
     * @return
     */
    public Result getGroupsGoodsInfo(Integer goodsId,Integer goodsSkuId,Integer activeId,Long customerId);

    /**
     * 获取商品分类列表
     * @return
     */
    Result getGoodsSort();

    /**
     * 获取商品分类列表,根据父级id
     * @return
     */
    Result getGoodsSortByPid(Integer parentId);

    /**
     * 获取商城首页Banner
     * @return
     */
    Result getShoppingBanner();


    /**
     * 门店商品出入库记录
     * @return
     */
    Result storeStockHistory(TokenUser tokenUser, Integer pageno, Integer pagesize, Integer goodsId,Integer goodsSkuId);


    /**
     * 获取门店列表
     * @param pageno
     * @param pagesize
     * @return
     */
    Result storeList(Integer pageno, Integer pagesize,String addr,Integer storeType);

    /**
     * 获取门店详情
     * @param storeId
     * @return
     */
    Result storeInfo(Integer storeId);

    /**
     * 商品加入推荐
     * @param goodsId
     * @return
     */
    Result goods2Rem(Integer goodsId,Integer isRem);

    /**
     * 商品取消推荐
     * @param goodsId
     * @return
     */
    Result goods2NotRem(Integer goodsId);

}