package com.weilai9.service.api.impl;

import com.weilai9.common.constant.Result;
import com.weilai9.dao.mapper.CustomerMapper;
import com.weilai9.service.api.AboutMeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class AboutMeServiceImpl implements AboutMeService {

    @Resource
    private CustomerMapper customerMapper;




    @Override
    public Result getInfo(Integer userId) {

        //查询用户名和头像
//        Map<String,Object> map1 = customerMapper.getInfo(userId);
//        //查询订单数量
//        Map<String,Object> map2 = customerMapper.getOrderNum(userId);

        Map<String,Object> map = new HashMap();
//        map.putAll(map1);
//        map.putAll(map2);

        return new Result(map);
    }
}
