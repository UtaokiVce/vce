package com.weilai9.service.api.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.db.sql.Order;
import com.aliyuncs.utils.MapUtils;
import com.aliyuncs.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.*;
import com.weilai9.dao.mapper.*;
import com.weilai9.dao.vo.ordersVo.OrderGoodsCommentVo;
import com.weilai9.service.api.GoodscommentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 评价表(Goodscomment)表服务实现类
 *
 * @author makejava
 * @since 2020-04-26 18:24:33
 */
@Service("goodscommentService")
public class GoodscommentServiceImpl extends ServiceImpl<GoodscommentMapper, Goodscomment> implements GoodscommentService {

    @Resource
    private GoodscommentMapper goodscommentMapper;

    @Resource
    private GoodsreplyMapper goodsreplyMapper;

    @Resource
    private OrdersMapper ordersMapper;

    @Resource
    private OrdergoodsMapper ordergoodsMapper;

    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    private GoodsskuMapper goodsskuMapper;

    @Override
    public Result getStoreGoodsCommentList(Integer pageno, Integer pagesize, Integer storeId, Integer goodsSkuId,Integer goodsId){
        IPage page = new Page(null == pageno ? 1 : pageno, null == pagesize ? 10 : pagesize);
        IPage<Map> iPage = goodscommentMapper.getStoreGoodsCommentList(page,storeId,goodsSkuId,goodsId);
        Result result=new Result(iPage);
        //查询中和评分
        String score="";
        Map<String,Object> map=goodscommentMapper.getAvgCommentScore(storeId,goodsSkuId,goodsId);
        if(map!=null){
            score= MapUtil.getStr(map,"score");
        }
        result.setMsg(score);
        return result;
    }

    @Override
    public Result doCommentReply(Integer commentId,String content){
        if(StringUtils.isEmpty(content)){
            return Result.Error("回复内容不能为空");
        }
        Goodsreply goodsreply=new Goodsreply();
        goodsreply.setSreplycontent(content);
        goodsreply.setSreplyaddtime(new Date());
        goodsreplyMapper.insert(goodsreply);
        //修改
        goodscommentMapper.update(null,new UpdateWrapper<Goodscomment>().set("replyId",goodsreply.getReplyid()).eq("commentId",commentId));
        return Result.OK("操作成功");
    }

    @Override
    public Result notCommentGoodsList(TokenUser tokenUser) {
        if (tokenUser == null){
            return Result.Error("用户未登录");
        }
        Integer customerId = tokenUser.getCustomerId().intValue();
        List<Orders> orderList = this.ordersMapper.selectList(new QueryWrapper<Orders>().eq("customerId", customerId).eq("state", 3));
        List<OrderGoodsCommentVo> orderGoodsCommentVoList = new ArrayList<>();
        for (Orders o:orderList) {
            List<Ordergoods> ordergoodsList = this.ordergoodsMapper.selectList(new QueryWrapper<Ordergoods>().eq("orderId", o.getOrderid()).eq("commentState", 0));
            for (Ordergoods os : ordergoodsList) {
                OrderGoodsCommentVo orderGoodsCommentVo = new OrderGoodsCommentVo();
                Goods goods = this.goodsMapper.selectById(os.getGoodsid());
                Goodssku goodssku = this.goodsskuMapper.selectById(os.getGoodsskuid());
                orderGoodsCommentVo.setGoodsId(goods.getGoodsId());
                orderGoodsCommentVo.setOrderGoodsSkuId(goodssku.getGoodsSkuId());
                orderGoodsCommentVo.setOrderGoodsSku(goodssku.getSkuName());
                orderGoodsCommentVo.setOrderGoodsHeadImg(goods.getHeadImg());
                orderGoodsCommentVo.setOrderGoodsName(goods.getTitle());
                orderGoodsCommentVo.setOrderId(os.getOrderid());
                orderGoodsCommentVo.setOrderGoodsNo(os.getOrdergoodsno());
                orderGoodsCommentVo.setOrderGoodsStoreId(os.getStoreid());
                orderGoodsCommentVo.setGoodsId(os.getGoodsid());

                orderGoodsCommentVoList.add(orderGoodsCommentVo);
            }
        }
        return new Result(orderGoodsCommentVoList);
    }
}