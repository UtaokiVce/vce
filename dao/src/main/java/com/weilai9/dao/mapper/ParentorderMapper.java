package com.weilai9.dao.mapper;

import com.weilai9.dao.entity.Parentorder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 小团表(Parentorder)表数据库访问层
 *
 * @author makejava
 * @since 2020-04-27 19:55:21
 */
public interface ParentorderMapper  extends BaseMapper<Parentorder> {

    List<Map<String,Object>> getCanJoinList(@Param("activeId") Integer activeId);
}