package com.weilai9.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.ParadiseRefundApply;

import java.util.List;

/**
 * 乐园退款审核表(ParadiseRefundApply)表服务接口
 *
 * @author makejava
 * @since 2020-05-08 10:09:44
 */
public interface ParadiseRefundApplyService extends IService<ParadiseRefundApply> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ParadiseRefundApply queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<ParadiseRefundApply> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param paradiseRefundApply 实例对象
     * @return 实例对象
     */
    ParadiseRefundApply insert(ParadiseRefundApply paradiseRefundApply);

    /**
     * 修改数据
     *
     * @param paradiseRefundApply 实例对象
     * @return 实例对象
     */
    ParadiseRefundApply update(ParadiseRefundApply paradiseRefundApply);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    Result addRefund(ParadiseRefundApply obj);

    Result updateRefund(ParadiseRefundApply obj);

    Result getMarketCode(Integer id);
}