package com.weilai9.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.weilai9.dao.entity.Category;
import com.weilai9.dao.entity.Goods;
import org.apache.ibatis.annotations.Param;

import java.awt.color.ICC_Profile;
import java.util.List;
import java.util.Map;

public interface GoodsMapper extends BaseMapper<Goods> {

    /**
     *  推荐商品列表
     * @return
     */
    IPage<Map> getCommendGoodsList(IPage page);


    /**
     * 后台获取推荐商品
     * @return
     */
    List<Goods> AdminGetCommendGoodsList();

    /**
     * 获取最大推荐值
     * @return
     */
    Integer getMaxRemNum();

    /**
     *  门店商品列表
     * @param page
     * @param storeId
     * @param goodsName
     * @param state
     * @param oneType
     * @param twoType
     * @param priceSort
     * @return
     */
    IPage<Map> getStoreGoodsList(IPage page,@Param("storeId") Integer storeId,@Param("goodsName")String goodsName,@Param("state")Integer state,@Param("oneType")Integer oneType,@Param("twoType")Integer twoType,@Param("priceSort")Integer priceSort);

    IPage<Map> getAdminGoodsList(IPage page,@Param("goodsName")String goodsName,@Param("oneType")Integer oneType,@Param("twoType")Integer twoType);

    IPage<Map> getAdminTryGoodsList(IPage page,@Param("goodsName")String goodsName,@Param("oneType")Integer oneType,@Param("twoType")Integer twoType);

    /**
     *  用户端商品列表
     * @param page
     * @param title
     * @param newtime
     * @param shopnum
     * @param inte
     * @param price
     * @return
     */
    IPage<Map> getShopGoodsList(IPage page,@Param("title")String title,@Param("newtime")Integer newtime,@Param("shopnum")Integer shopnum,@Param("inte")Integer inte,@Param("price")Integer price,@Param("cateTwo")Integer cateTwo,@Param("cateOne")Integer cateOne);

    List<Map> getStoreGoodsImg(Integer storeId);

    Map<String,Object> getGroupBuyInfo(@Param("goodsid")Integer goodsid);
}
