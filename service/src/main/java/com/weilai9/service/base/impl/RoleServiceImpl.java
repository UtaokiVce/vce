package com.weilai9.service.base.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weilai9.common.config.exception.BusinessException;
import com.weilai9.common.constant.CustomerConstants;
import com.weilai9.common.constant.EnableConstants;
import com.weilai9.common.constant.RedisKey;
import com.weilai9.common.constant.Result;
import com.weilai9.common.utils.redis.RedisUtil;
import com.weilai9.dao.entity.*;
import com.weilai9.dao.mapper.RoleMapper;
import com.weilai9.dao.vo.base.AddRoleVO;
import com.weilai9.dao.vo.base.UpdateRoleVO;
import com.weilai9.service.base.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * (Role)表服务实现类
 *
 * @author china.fuyao@outlook.com
 * @since 2020-03-25 18:19:12
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    RedisUtil redisUtil;
    @Resource
    CustomerRoleService customerRoleService;
    @Resource
    RoleMenuBtnService roleMenuBtnService;
    @Resource
    SysMenuService sysMenuService;
    @Resource
    SysButtonService sysButtonService;

    /**
     * 缓存角色
     */
    public void redisRole() {
        List<Role> list = this.list(new QueryWrapper<Role>().eq("status", 1));
        for (Role role : list) {
            //获取ids列表
            Set<Integer> set = new HashSet();
            String ifIds = role.getIfIds();
            if (StringUtils.isNotBlank(ifIds)) {
                String[] ids = ifIds.split(",");
                for (String id : ids) {
                    if (StringUtils.isNotBlank(id)) {
                        set.add(Integer.parseInt(id));
                    }
                }
            }
            redisUtil.set(RedisKey.AUTH_ROLE_URL_ID + role.getRoleName(), set);
        }
    }

    /**
     * 根据用户id 获取用户权限名称列表
     *
     * @param customerId
     * @return
     */
    @Override
    public ArrayList<String> roleNameListByCustomerId(Long customerId) {
        List<CustomerRole> customerRoleList = customerRoleService.list(new QueryWrapper<CustomerRole>().eq("customer_id", customerId));
        List<Role> roleList = this.list();
        Map<Integer, String> map = roleList.stream().collect(Collectors.toMap(Role::getRoleId, Role::getRoleName));
        ArrayList<String> data = new ArrayList<>(customerRoleList.size());
        for (CustomerRole customerRole : customerRoleList) {
            if (map.containsKey(customerRole.getRoleId())) {
                data.add(map.get(customerRole.getRoleId()));
            }
        }
        return data;
    }

    /**
     * 根据用户id 获取用户权限名称列表
     *
     * @param customerId
     * @return
     */
    @Override
    public List<Role> roleListByCustomerId(Long customerId) {
        List<CustomerRole> customerRoleList = customerRoleService.list(new QueryWrapper<CustomerRole>().eq("customer_id", customerId));
        List<Role> roleList = this.list();
        Map<Integer, Role> map = roleList.stream().collect(Collectors.toMap(Role::getRoleId, k1 -> k1, (k1, k2) -> k1));
        List<Role> data = new ArrayList<>(customerRoleList.size());
        for (CustomerRole customerRole : customerRoleList) {
            if (map.containsKey(customerRole.getRoleId())) {
                Role role = map.get(customerRole.getRoleId());
                role.setIfIds("");
                data.add(role);
            }
        }
        return data;
    }

    /**
     * 角色列表
     *
     * @return
     */
    @Override
    public Result listOfAll() {
        List<Role> roleList = this.list();
        return new Result(roleList);
    }

    /**
     * 角色详情
     *
     * @param roleId
     * @return
     */
    @Override
    public Result roleInfo(Integer roleId) {
        //角色详情
        Role role = this.getOne(new QueryWrapper<Role>().eq("role_id", roleId));
        if (Objects.isNull(role)) {
            return Result.Error("角色不存在");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("roleName", role.getRoleName());
        //角色菜单
        List<RoleMenuBtn> roleMenuBtnList = roleMenuBtnService.list(new QueryWrapper<RoleMenuBtn>().eq("role_id", roleId));
        //获取菜单列表
        List<SysMenu> menuList = sysMenuService.list(new QueryWrapper<SysMenu>().eq("status", 1));
        //列表转map
        Map<Integer, SysMenu> sysMenuMap = menuList.stream().collect(Collectors.toMap(SysMenu::getMenuId, k -> k, (k1, k2) -> k1));
        //获取按钮列表
        List<SysButton> buttonList = sysButtonService.list(new QueryWrapper<SysButton>().eq("status", 1));
        //列表转map
        Map<Integer, SysButton> buttonMap = buttonList.stream().collect(Collectors.toMap(SysButton::getBtnId, k -> k, (k1, k2) -> k1));
        //
        Map<Integer, Object> ifMap = new HashMap<>(roleMenuBtnList.size());
        for (RoleMenuBtn roleMenuBtn : roleMenuBtnList) {
            //包含菜单
            if (sysMenuMap.containsKey(roleMenuBtn.getMenuId())) {
                //组合菜单详情
                Map<String, Object> menuMap = new HashMap<>(2);
                //菜单按钮列表
                List<SysButton> menuBtnList = new ArrayList<>();
                //菜单按钮组合
                String btnIds = roleMenuBtn.getBtnIds();
                if (StringUtils.isNotBlank(btnIds)) {
                    //获取按钮详情
                    String[] btnIdStr = btnIds.split(",");
                    for (String btnId : btnIdStr) {
                        Integer btn = Integer.valueOf(btnId);
                        if (buttonMap.containsKey(btn)) {
                            SysButton button = buttonMap.get(btn);
                            menuBtnList.add(button);
                        }
                    }
                }
                SysMenu sysMenu = sysMenuMap.get(roleMenuBtn.getMenuId());

                menuMap.put("menuInfo", sysMenu);
                menuMap.put("menuBtnList", menuBtnList);
                ifMap.put(roleMenuBtn.getMenuId(), menuMap);
            }
            map.put("ifMap", ifMap);
        }
        return new Result(map);
    }

    /**
     * 新增角色
     *
     * @param addRoleVO
     * @param customerId
     * @return
     */
    @Override
    @Transactional
    public Result addRole(AddRoleVO addRoleVO, Long customerId) {
        if (Objects.nonNull(addRoleVO)) {
            Role one = this.getOne(new QueryWrapper<Role>().eq("role_name", addRoleVO.getRoleName()));
            if (Objects.isNull(one)) {
                //新增
                Role role = new Role();
                role.setRoleName(addRoleVO.getRoleName());
                if (null == addRoleVO.getRoleType()) {
                    //默认管理角色
                    role.setRoleType(CustomerConstants.ADMIN_USER);
                } else {
                    role.setRoleType(addRoleVO.getRoleType());
                }
                role.setStatus(EnableConstants.ENABLE);
                role.setAddUid(customerId);
                role.setUpdateUid(customerId);
                Date date = new Date();
                role.setAddTime(date);
                role.setUpdateTime(date);
                boolean save = save(role);
                if (save) {
                    Integer roleId = role.getRoleId();
                    Map<String, String> menuBtn = addRoleVO.getMenuBtn();
                    if (null != menuBtn) {
                        List<RoleMenuBtn> rmbList = new ArrayList<>(menuBtn.size());
                        for (String menuId : menuBtn.keySet()) {
                            RoleMenuBtn rmb = new RoleMenuBtn();
                            rmb.setRoleId(roleId);
                            rmb.setMenuId(Integer.valueOf(menuId));
                            rmb.setBtnIds(menuBtn.get(menuId));
                            rmb.setAddUid(customerId);
                            rmb.setUpdateUid(customerId);
                            rmb.setAddTime(date);
                            rmb.setUpdateTime(date);
                            rmbList.add(rmb);
                        }
                        roleMenuBtnService.remove(new QueryWrapper<RoleMenuBtn>().eq("role_id", roleId));
                        if (rmbList.size() > 0) {
                            boolean batch = roleMenuBtnService.saveBatch(rmbList);
                            if (batch) {
                                //重新缓存
                                redisRole();
                                return Result.OK("操作成功");
                            }
                        }
                        throw new BusinessException("addRole失败，事务回滚");
                    }
                    //重新缓存
                    redisRole();
                    return Result.OK("操作成功");
                }
            }
            return Result.Error("角色已存在");
        }
        return Result.Error("参数异常");
    }

    /**
     * 修改角色
     *
     * @param updateRoleVO
     * @param customerId
     * @return
     */
    @Override
    @Transactional
    public Result updateRole(UpdateRoleVO updateRoleVO, Long customerId) {
        if (Objects.nonNull(updateRoleVO)) {
            Role one = this.getOne(new QueryWrapper<Role>().eq("role_id", updateRoleVO.getRoleId()));
            if (Objects.nonNull(one)) {
                //修改角色
                Role role = new Role();
                role.setRoleId(updateRoleVO.getRoleId());
                role.setRoleName(updateRoleVO.getRoleName());
                role.setUpdateUid(customerId);
                Date date = new Date();
                role.setUpdateTime(date);
                boolean save = updateById(role);
                if (save) {
                    //修改菜单
                    Integer roleId = updateRoleVO.getRoleId();
                    Map<String, String> menuBtn = updateRoleVO.getMenuBtn();
                    if (null != menuBtn) {
                        List<RoleMenuBtn> rmbList = new ArrayList<>(menuBtn.size());
                        for (String menuId : menuBtn.keySet()) {
                            RoleMenuBtn rmb = new RoleMenuBtn();
                            rmb.setRoleId(roleId);
                            rmb.setMenuId(Integer.valueOf(menuId));
                            rmb.setBtnIds(menuBtn.get(menuId));
                            rmb.setAddUid(customerId);
                            rmb.setUpdateUid(customerId);
                            rmb.setAddTime(date);
                            rmb.setUpdateTime(date);
                            rmbList.add(rmb);
                        }
                        roleMenuBtnService.remove(new QueryWrapper<RoleMenuBtn>().eq("role_id", roleId));
                        if (rmbList.size() > 0) {
                            boolean batch = roleMenuBtnService.saveBatch(rmbList);
                            if (batch) {
                                //重新缓存
                                redisRole();
                                return Result.OK("操作成功");
                            }
                        }
                        throw new BusinessException("updateRole失败，事务回滚");
                    }
                    //重新缓存
                    redisRole();
                    return Result.OK("操作成功");
                }
            }
            return Result.Error("角色不存在");
        }
        return Result.Error("操作失败");
    }

    /**
     * 修改角色状态
     *
     * @param roleId
     * @param status
     * @param customerId
     * @return
     */
    @Override
    public Result updateStatus(Integer roleId, Integer status, Long customerId) {
        Role role = new Role();
        role.setRoleId(roleId);
        role.setStatus(status);
        role.setUpdateTime(new Date());
        role.setUpdateUid(customerId);
        boolean update = this.updateById(role);
        if (update) {
            //重新缓存
            redisRole();
            return Result.OK("操作成功");
        }
        return Result.Error("操作失败");
    }

    /**
     * 修改角色接口权限
     *
     * @param roleId
     * @param ifIds
     * @param customerId
     * @return
     */
    @Override
    public Result updateIf(Integer roleId, String ifIds, Long customerId) {
        Role role = new Role();
        role.setRoleId(roleId);
        role.setIfIds(ifIds);
        role.setUpdateTime(new Date());
        role.setUpdateUid(customerId);
        boolean update = this.updateById(role);
        if (update) {
            //重新缓存
            redisRole();
            return Result.OK("操作成功");
        }
        return Result.Error("操作失败");
    }
}