package com.weilai9.dao.mapper;

import com.weilai9.dao.entity.Groupons;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 小团参与表(Groupons)表数据库访问层
 *
 * @author makejava
 * @since 2020-04-27 19:55:21
 */
public interface GrouponsMapper  extends BaseMapper<Groupons> {
    List<String> selectHead(@Param("parentId") Integer parentId);
}