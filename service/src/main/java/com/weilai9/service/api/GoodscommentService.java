package com.weilai9.service.api;

import com.baomidou.mybatisplus.extension.service.IService;
import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.Goodscomment;

/**
 * 评价表(Goodscomment)表服务接口
 *
 * @author makejava
 * @since 2020-04-26 18:24:32
 */
public interface GoodscommentService extends IService<Goodscomment>{

    /**
     *  门店评价列表
     * @param pageno
     * @param pagesize
     * @param storeId
     * @param goodsSkuId
     * @return
     */
    public Result getStoreGoodsCommentList(Integer pageno,Integer pagesize,Integer storeId,Integer goodsSkuId,Integer goodsId);

    /**
     *  评价回复
     * @param commentId
     * @param content
     * @return
     */
    public Result doCommentReply(Integer commentId,String content);


    /**
     * 未评价商品列表
     * @param tokenUser
     * @return
     */
    Result notCommentGoodsList(TokenUser tokenUser);
}