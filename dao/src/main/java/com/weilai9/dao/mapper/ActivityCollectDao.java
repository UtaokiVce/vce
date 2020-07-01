package com.weilai9.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weilai9.dao.entity.ActivityCollect;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * (ActivityCollect)表数据库访问层
 *
 * @author makejava
 * @since 2020-04-26 17:55:30
 */
//@Mapper
public interface ActivityCollectDao  extends BaseMapper<ActivityCollect> {

    //IPage<Map> getManagerCustomerInfo(IPage page, @Param("customerName") String customerName);
    List<Map<String,Object>> getColl(@Param("userId") long userId, @Param("type") int type);

    int addColl(@Param("userId") int userId, @Param("actId") int actId);
}