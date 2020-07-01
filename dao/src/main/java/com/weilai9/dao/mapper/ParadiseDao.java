package com.weilai9.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.weilai9.dao.entity.Paradise;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 乐园信息表(Paradise)表数据库访问层
 *
 * @author makejava
 * @since 2020-05-08 10:09:40
 */
public interface ParadiseDao  extends BaseMapper<Paradise> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Paradise queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<Paradise> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param paradise 实例对象
     * @return 对象列表
     */
    List<Paradise> queryAll(Paradise paradise);

    /**
     * 新增数据
     *
     * @param paradise 实例对象
     * @return 影响行数
     */
    int insert(Paradise paradise);

    /**
     * 修改数据
     *
     * @param paradise 实例对象
     * @return 影响行数
     */
    int update(Paradise paradise);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    /**
     * 查询已核销人数
        乐园id
     * @return
     */
    int getHeXiaoCount(@Param(value = "paradiseId") Integer paradiseId);

    List<String> getHead(@Param(value = "paradiseId") Integer paradiseId);

    IPage<Map> getUserList(IPage page, @Param(value = "paradiseId") Integer paradiseId);

    List<Map<String, Object>> getMyParadise(@Param(value = "userId") long userId, @Param(value = "status") Integer status);

    int getNumByDate(@Param(value = "id") Integer id, @Param(value = "today") String today);


    List<Map<String, Object>> getList();

    List<Map<String, Object>> getListByStore(Integer storeId,Integer page,Integer size);

    int getListByStoreCount(Integer storeId);

    List<Integer> selectIdList(Integer storeId);
}