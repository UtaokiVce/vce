package com.weilai9.service.admin;


import com.baomidou.mybatisplus.extension.service.IService;
import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.Customer;

/**
 * (User)表服务接口
 *
 * @author china.fuyao@outlook.com
 * @since 2020-03-25 18:20:47
 */
public interface AdminCustomerService extends IService<Customer> {
    /**
     * 用户账号密码登录
     *
     * @param customerName
     * @param password
     * @return
     */
    Result login(String customerName, String password);

    /**
     * 用户登出
     *
     * @param tokenUser
     * @return
     */
    Result logout(TokenUser tokenUser);

    /**
     * 新增系统用户
     *
     * @param customerName
     * @param password
     * @param roleId
     * @return
     */
    Result addAdminCustomer(String customerName, String password, Integer roleId);

    /**
     * 修改系統用戶角色
     *
     * @param customerId
     * @param roleId
     * @return
     */
    Result updateAdminCustomerRole(Long customerId, Integer roleId);

    /**
     * 用戶信息
     * @param tokenUser
     * @return
     */
    Result info(TokenUser tokenUser);

    /**
     * 获取管理用户列表
     *
     * @param current
     * @param size
     * @param customerName
     * @return
     */
    Result managerList(Integer current, Integer size, String customerName);

    /**
     * 修改指定用户状态
     * @param customerId
     * @param status
     * @return
     */
    Result updateStatus(Long customerId, Integer status);
}