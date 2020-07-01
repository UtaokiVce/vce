package com.weilai9.service.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.Role;
import com.weilai9.dao.vo.base.AddRoleVO;
import com.weilai9.dao.vo.base.UpdateRoleVO;

import java.util.ArrayList;
import java.util.List;

/**
 * (Role)表服务接口
 *
 * @author china.fuyao@outlook.com
 * @since 2020-03-25 18:19:12
 */
public interface RoleService extends IService<Role> {
    /**
     * 缓存角色
     */
    void redisRole();

    /**
     * 根据用户id 获取用户权限名称列表
     * @param customerId
     * @return
     */
    ArrayList<String> roleNameListByCustomerId(Long customerId);

    /**
     * 根据用户id 获取用户权限名称列表
     * @param customerId
     * @return
     */
    List<Role> roleListByCustomerId(Long customerId);

    /**
     * 角色列表
     * @return
     */
    Result listOfAll();

    /**
     * 角色详情
     * @param roleId
     * @return
     */
    Result roleInfo(Integer roleId);
    /**
     * 新增角色
     * @param addRoleVO
     * @param customerId
     * @return
     */
    Result addRole(AddRoleVO addRoleVO, Long customerId);

    /**
     * 修改角色
     * @param updateRoleVO
     * @param customerId
     * @return
     */
    Result updateRole(UpdateRoleVO updateRoleVO, Long customerId);

    /**
     * 修改状态
     * @param roleId
     * @param status
     * @param customerId
     * @return
     */
    Result updateStatus(Integer roleId, Integer status, Long customerId);

    /**
     * 修改角色接口权限
     * @param roleId
     * @param ifIds
     * @param customerId
     * @return
     */
    Result updateIf(Integer roleId, String ifIds, Long customerId);
}