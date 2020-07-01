package com.weilai9.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weilai9.dao.entity.Banner;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * banner表(Banner)表数据库访问层
 *
 * @author makejava
 * @since 2020-06-10 14:19:34
 */
public interface BannerDao extends BaseMapper<Banner> {

    /**
     * 通过ID查询单条数据
     *
     * @param bannerId 主键
     * @return 实例对象
     */
    Banner queryById(Integer bannerId);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Banner> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 新增数据
     *
     * @param banner 实例对象
     * @return 影响行数
     */
    int insert(Banner banner);

    /**
     * 修改数据
     *
     * @param banner 实例对象
     * @return 影响行数
     */
    int update(Banner banner);

    /**
     * 通过主键删除数据
     *
     * @param bannerId 主键
     * @return 影响行数
     */
    int deleteById(Integer bannerId);

    List<Integer> getSortList();
}