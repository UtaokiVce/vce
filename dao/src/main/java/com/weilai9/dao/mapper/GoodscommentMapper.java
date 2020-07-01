package com.weilai9.dao.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.weilai9.dao.entity.Goodscomment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 评价表(Goodscomment)表数据库访问层
 *
 * @author makejava
 * @since 2020-04-26 18:22:05
 */
public interface GoodscommentMapper  extends BaseMapper<Goodscomment> {

    IPage<Map> getStoreGoodsCommentList(IPage page,@Param("storeId")Integer storeId,@Param("goodsSkuId")Integer goodsSkuId,@Param("goodsId")Integer goodsId);

    Map<String,Object> getAvgCommentScore(@Param("storeId")Integer storeId,@Param("goodsSkuId")Integer goodsSkuId,@Param("goodsId")Integer goodsId);


}