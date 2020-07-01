package com.weilai9.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weilai9.dao.entity.ParadiseRefundApply;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 乐园退款审核表(ParadiseRefundApply)表数据库访问层
 *
 * @author makejava
 * @since 2020-05-08 10:09:44
 */
public interface ParadiseRefundApplyDao extends BaseMapper<ParadiseRefundApply> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ParadiseRefundApply queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ParadiseRefundApply> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param paradiseRefundApply 实例对象
     * @return 对象列表
     */
    List<ParadiseRefundApply> queryAll(ParadiseRefundApply paradiseRefundApply);

    /**
     * 新增数据
     *
     * @param paradiseRefundApply 实例对象
     * @return 影响行数
     */
    int insert(ParadiseRefundApply paradiseRefundApply);

    /**
     * 修改数据
     *
     * @param paradiseRefundApply 实例对象
     * @return 影响行数
     */
    int update(ParadiseRefundApply paradiseRefundApply);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    Map<String, Object> getInfo(@Param("id") Integer id);

    String getMarketCode(@Param("id") Integer id);
}