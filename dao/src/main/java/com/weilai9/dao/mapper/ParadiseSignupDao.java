package com.weilai9.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weilai9.dao.entity.ParadiseSignup;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 乐园报名信息表(ParadiseSignup)表数据库访问层
 *
 * @author makejava
 * @since 2020-05-08 10:09:44
 */
public interface ParadiseSignupDao  extends BaseMapper<ParadiseSignup> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ParadiseSignup queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ParadiseSignup> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param paradiseSignup 实例对象
     * @return 对象列表
     */
    List<ParadiseSignup> queryAll(ParadiseSignup paradiseSignup);

    /**
     * 新增数据
     *
     * @param paradiseSignup 实例对象
     * @return 影响行数
     */
    int insert(ParadiseSignup paradiseSignup);

    /**
     * 修改数据
     *
     * @param paradiseSignup 实例对象
     * @return 影响行数
     */
    int update(ParadiseSignup paradiseSignup);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    Map<String, Object> getSignInfo(@Param("id") Integer signupId);

    Map<String, Object> getSignInfo2(@Param("id") Integer signupId);


    int getNum(@Param("date") String date, @Param("paradiseId") Integer paradiseId);

    Map<String, Object> appointment(@Param("paradiseId") Integer paradiseId);

    Map<String, Object> getDetails(@Param("paradiseId") Integer paradiseId);

    List<Map<String, Object>> getAllAct(int status, List<Integer> idlist);
}