package com.weilai9.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weilai9.dao.entity.ActivitySignup;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * (ActivitySignup)表数据库访问层
 *
 * @author makejava
 * @since 2020-04-26 17:56:24
 */
//@Mapper
public interface ActivitySignupDao extends BaseMapper<ActivitySignup> {


    List<Map<String, Object>> getMyAct(@Param("userId") long userId, @Param("status") String status);

    List<Map<String, Object>> getAllAct(@Param("status") Integer status,@Param("idlist") List<Integer> idlist);

    Map<String, Object> getOne(@Param("actSignId") String actSignId);

    ActivitySignup getObject(@Param("actSignId") String actSignId);

    Map<String, Object> getChecking(@Param("actSignId") String actSignId);

    int addOrder(ActivitySignup activitySignup);

    Map<String, Object> getInfo(@Param("id") Integer id);

    Map<String, Object> appointment(@Param("id") Integer activityId);

    Map<String, Object> getOne1(String actSignId);
}