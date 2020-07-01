package com.weilai9.service.admin;


import com.baomidou.mybatisplus.extension.service.IService;
import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.ParadiseSignup;
import com.weilai9.dao.entity.ShippingAddress;
import com.weilai9.dao.vo.activity.AddShippingAddressVO;
import com.weilai9.dao.vo.activity.UpdateShippingAddressVO;

import java.util.List;
import java.util.Map;

/**
 * (ShippingAddress)表服务接口
 *
 * @author makejava
 * @since 2020-05-13 11:35:24
 */
public interface ShippingAddressService extends IService<ShippingAddress> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ShippingAddress queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ShippingAddress> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param shippingAddress 实例对象
     * @return 实例对象
     */
    ShippingAddress insert(ShippingAddress shippingAddress);

    /**
     * 修改数据
     *
     * @param shippingAddress 实例对象
     * @return 实例对象
     */
    ShippingAddress update(ShippingAddress shippingAddress);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    Result addAddress(AddShippingAddressVO obj);

    Result updateAddress(UpdateShippingAddressVO vo);

    Result getMyAddress();

    Result deleteAddress(Integer id);

    Result get(Integer id);

    Result getAddDefault(Integer id);
}