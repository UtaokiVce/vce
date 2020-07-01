package com.weilai9.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.weilai9.dao.entity.ActivityMsg;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * (ActivityMsg)表数据库访问层
 *
 * @author makejava
 * @since 2020-04-26 17:56:00
 */

public interface ActivityMsgDao extends BaseMapper<ActivityMsg> {

    List<ActivityMsg> getAll();

    List<Map<String,Object>> getActByType(@Param(value = "activityType") String activityType);

    List<Map<String,Object>> getActType();

    int insertAct(ActivityMsg activityMsg);

    int getHeXiaoCount(@Param(value = "actId") Integer actId);

    List<String> getHead(@Param(value = "actId") Integer actId);

    //List<Map<String, Object>> getUserList(@Param(value = "actId")Integer actId);
    IPage<Map> getUserList(IPage page, @Param(value = "actId") Integer actId);

    List<Map<String, Object>> getMyAct(@Param(value = "userId") String userId, @Param(value = "status") Integer status);

    Map<String, Object> queryById(@Param(value = "actId")Integer actId);

    List<Map<String, Object>> getActIndex();

    List<Map<String, Object>> getBanner();

    List<Map<String, Object>> selectByType(String activityType);

    int selectCurrNum(Integer actId);

    List<Integer> selectIdList(Integer storeId);
}