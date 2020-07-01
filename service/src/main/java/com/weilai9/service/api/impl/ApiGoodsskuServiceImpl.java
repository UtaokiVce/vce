package com.weilai9.service.api.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weilai9.dao.entity.Goods;
import com.weilai9.dao.entity.Goodssku;
import com.weilai9.dao.mapper.GoodsMapper;
import com.weilai9.dao.mapper.GoodsskuMapper;
import com.weilai9.service.api.ApiGoodsService;
import com.weilai9.service.api.ApiGoodsskuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * (User)表服务实现类
 *
 * @author china.fuyao@outlook.com
 * @since 2020-03-25 18:20:48
 */
@Slf4j
@Service("apiGoodsskuService")
public class ApiGoodsskuServiceImpl extends ServiceImpl<GoodsskuMapper, Goodssku> implements ApiGoodsskuService{


}