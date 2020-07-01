package com.weilai9.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weilai9.dao.entity.ShippingAddress;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * (ShippingAddress)表数据库访问层
 *
 * @author makejava
 * @since 2020-05-13 11:35:24
 */
public interface ShippingAddressDao extends BaseMapper<ShippingAddress> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ShippingAddress queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ShippingAddress> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param shippingAddress 实例对象
     * @return 对象列表
     */
    List<ShippingAddress> queryAll(ShippingAddress shippingAddress);

    /**
     * 新增数据
     *
     * @param shippingAddress 实例对象
     * @return 影响行数
     */
    @Override
    int insert(ShippingAddress shippingAddress);

    /**
     * 修改数据
     *
     * @param shippingAddress 实例对象
     * @return 影响行数
     */
    int update(ShippingAddress shippingAddress);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);


}