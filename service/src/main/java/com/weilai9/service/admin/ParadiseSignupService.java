package com.weilai9.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.ParadiseSignup;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 乐园报名信息表(ParadiseSignup)表服务接口
 *
 * @author makejava
 * @since 2020-05-08 10:09:44
 */
public interface ParadiseSignupService extends IService<ParadiseSignup> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ParadiseSignup queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ParadiseSignup> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param paradiseSignup 实例对象
     * @return 实例对象
     */
    ParadiseSignup insert(ParadiseSignup paradiseSignup);

    /**
     * 修改数据
     *
     * @param paradiseSignup 实例对象
     * @return 实例对象
     */
    ParadiseSignup update(ParadiseSignup paradiseSignup);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    Result addSignup(ParadiseSignup obj);

    Result writeOff(Integer orderId,Integer adlutNum,Integer childNum);

    Result getAvailNumber(Date date, Integer paradiseId);

    Result appointment(Integer paradiseId);

    Map<String, Object> getDetail(Integer actSignId);

    Result ticketChanging(Integer orderId, String newDate);

    Result hexiao(Integer orderId);

    Result getEWM(Integer orderId);

    Result analysisHXM(String hxm,Long customerId);

    Result getAll(int status, TokenUser tokenUser);
}