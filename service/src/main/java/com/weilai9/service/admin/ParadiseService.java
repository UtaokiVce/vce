package com.weilai9.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.Paradise;
import com.weilai9.dao.vo.activity.AddParadiseVO;

import java.util.List;

/**
 * 乐园信息表(Paradise)表服务接口
 *
 * @author makejava
 * @since 2020-05-08 10:09:41
 */
public interface ParadiseService extends IService<Paradise> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Paradise queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<Paradise> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param paradise 实例对象
     * @return 实例对象
     */
    Paradise insert(Paradise paradise);

    /**
     * 修改数据
     *
     * @param paradise 实例对象
     * @return 实例对象
     */
    Paradise update(Paradise paradise);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    Result getParadiseByStore(int page,int size,Long customerId);

    Result getDetail(Integer id);

    Result getParaRegistration(Integer current, Integer size, Integer id);

    Result getAll(Integer current, Integer size,String name);

    Result addParadise(AddParadiseVO obj,Long customerId);

    Result getList();

    Result getDetail2(Integer id);

    Result getMyParadise(Integer status);

    Result getparById(int id);

    Result parPay(Integer orderId);

    Result getParList();

    Result switchPar(Integer id, Integer status);

    Result detele(Integer id);
}