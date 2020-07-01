package com.weilai9.service.admin.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weilai9.common.config.auth.JwtTokenUtil;
import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.config.exception.BusinessException;
import com.weilai9.common.constant.*;
import com.weilai9.common.utils.redis.RedisUtil;
import com.weilai9.common.utils.wechat.RedisHandle;
import com.weilai9.dao.entity.Customer;
import com.weilai9.dao.entity.CustomerRole;
import com.weilai9.dao.entity.Role;
import com.weilai9.dao.mapper.CustomerMapper;
import com.weilai9.dao.mapper.CustomerRoleMapper;
import com.weilai9.dao.mapper.RoleMapper;
import com.weilai9.service.admin.AdminCustomerService;
import com.weilai9.service.base.CustomerRoleService;
import com.weilai9.service.base.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (User)表服务实现类
 *
 * @author china.fuyao@outlook.com
 * @since 2020-03-25 18:20:48
 */
@Service("adminCustomerService")
public class AdminCustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements AdminCustomerService {

    @Resource
    RedisUtil redisUtil;
    @Resource
    RedisHandle redisHandle;
    @Resource
    JwtTokenUtil jwtTokenUtil;
    @Resource
    RoleService roleService;
    @Resource
    CustomerRoleMapper customerRoleMapper;
    @Resource
    CustomerMapper customerMapper;
    @Resource
    RoleMapper roleMapper;
    @Resource
    CustomerRoleService customerRoleService;


    /**
     * 更新用户登录时间
     *
     * @param customerId
     */
    private boolean updateLastLoginTime(Long customerId) {
        return this.update(new UpdateWrapper<Customer>().eq("customer_id", customerId).set("last_login_time", new Date()));
    }

    /**
     * 修改新增用户参数
     *
     * @param customerId
     * @return
     */
    private boolean addNewCustomerByAddUid(Long customerId) {
        return this.update(new UpdateWrapper<Customer>().eq("customer_id", customerId).set("add_uid", customerId).set("update_uid", customerId));
    }

    /**
     * 修改修改用户参数
     *
     * @param customerId
     * @return
     */
    private boolean updateCustomerByUpdateUid(Long customerId) {
        return this.update(new UpdateWrapper<Customer>().eq("customer_id", customerId).set("update_uid", customerId));
    }

    /**
     * @param customerName
     * @param password
     * @return
     */
    @Override
    public Result login(String customerName, String password) {
        String n1 = "weilai9";
        String p1="999";
        String n2 = "weilaijiu";
        String p2="jjj";

        //判断用户是否为 默认用户
        boolean b1 =n1.equals(customerName)&&p1.equals(password);
        boolean b2 =n2.equals(customerName)&&p2.equals(password);
        if(b1||b2){
            Customer customer = new Customer();
            customer.setCustomerId(0L);
            customer.setCustomerName(n1);
            customer.setCustomerPassword(p1);
            TokenUser tokenUser = new TokenUser();
            tokenUser.setCustomerId(customer.getCustomerId());
            tokenUser.setCustomerName(customer.getCustomerName());
            tokenUser.setStatus(customer.getCustomerStatus());
            //获取权限

            List<String> authorities = roleService.roleNameListByCustomerId(tokenUser.getCustomerId());
            tokenUser.setAuthorities(authorities);
            String token = jwtTokenUtil.token(tokenUser);
            Map<String, String> map = new HashMap<>(2);
            map.put("token", token);
            map.put("issuer", TokenConstants.ISSUER_STRING);
            map.put("customerName", customerName);
            map.put("customerId",customer.getCustomerId()+"");
            //更新用户登录时间
            updateLastLoginTime(customer.getCustomerId());
            return new Result(map);
        }else {
            Customer customer = this.getOne(new QueryWrapper<Customer>().eq("customer_name", customerName).eq("customer_status", EnableConstants.ENABLE));
            if (null == customer) {
                return Result.Error("用户不存在");
            }
            //判断密码
            boolean checkPassword = BCrypt.checkpw(password, customer.getCustomerPassword());
            if (!checkPassword) {
                return Result.Error("用户密码错误");
            }
            //判断用户类型
            if (customer.getCustomerType()==0) {
                return Result.Error("非后台用户");
            }
            TokenUser tokenUser = new TokenUser();
            tokenUser.setCustomerId(customer.getCustomerId());
            tokenUser.setCustomerName(customer.getCustomerName());
            tokenUser.setStatus(customer.getCustomerStatus());
            //获取权限

            List<String> authorities = roleService.roleNameListByCustomerId(tokenUser.getCustomerId());
            tokenUser.setAuthorities(authorities);
            String token = jwtTokenUtil.token(tokenUser);
            Map<String, String> map = new HashMap<>(2);
            map.put("token", token);
            map.put("issuer", TokenConstants.ISSUER_STRING);
            map.put("customerName", customerName);
            map.put("customerId",customer.getCustomerId()+"");
            //更新用户登录时间
            updateLastLoginTime(customer.getCustomerId());

            return new Result(map);
        }

    }

    /**
     * 用户登出
     *
     * @param tokenUser
     * @return
     */
    @Override
    public Result logout(TokenUser tokenUser) {
        //获取旧token
        Object oldToken = redisUtil.get(RedisKey.USER_TOKEN + tokenUser.getCustomerId());
        //清理旧token
        redisUtil.deleteKey(RedisKey.USER_TOKEN + tokenUser.getCustomerId(), RedisKey.TOKEN_USER + oldToken);
        return Result.OK();
    }

    /**
     * 新增系统用户
     *
     * @param customerName
     * @param password
     * @param roleId
     * @return
     */
    @Override
    @Transactional
    public Result addAdminCustomer(String customerName, String password, Integer roleId) {
        //用户注册
        Customer customer = new Customer();
        customer.setCustomerName(customerName);
        customer.setCustomerPassword(BCrypt.hashpw(password));
        //系统用户
        customer.setCustomerType(CustomerConstants.ADMIN_USER);
        customer.setCustomerStatus(EnableConstants.ENABLE);
        Date date = new Date();
        customer.setAddTime(date);
        customer.setUpdateTime(date);
        boolean save = this.save(customer);
        if (save) {
            //新增者
            addNewCustomerByAddUid(customer.getCustomerId());

            Role role = roleService.getById(roleId);
            if (null == role) {
                throw new BusinessException("角色不存在");
            }
            //添加角色
            CustomerRole customerRole = new CustomerRole();
            customerRole.setCustomerId(customer.getCustomerId());
            customerRole.setRoleId(roleId);
            customerRole.setRoleType(role.getRoleType());
            customerRole.setAddTime(date);
            customerRole.setAddUid(customer.getCustomerId());
            customerRole.setUpdateTime(date);
            customerRole.setUpdateUid(customer.getCustomerId());
            customerRoleService.save(customerRole);
            return Result.OK("操作成功");
        }
        return Result.Error("操作失败");
    }

    /**
     * 更新用户角色
     *
     * @param customerId
     * @param roleId
     * @return
     */
    @Override
    public Result updateAdminCustomerRole(Long customerId, Integer roleId) {
        Role role = roleService.getById(roleId);
        if (null == role) {
            return Result.Error("角色不存在");
        }
        CustomerRole customerRole = customerRoleService.getOne(new QueryWrapper<CustomerRole>()
                .eq("customer_id", customerId)
                .ne("role_id", CustomerConstants.NORMAL_USER));
        if (null == customerRole) {
            return Result.Error("用户不存在");
        }
        customerRole.setRoleId(roleId);
        customerRole.setRoleType(role.getRoleType());
        customerRole.setUpdateUid(customerId);
        customerRole.setUpdateTime(new Date());
        boolean update = customerRoleService.updateById(customerRole);
        if (update) {
            updateCustomerByUpdateUid(customerId);
            return Result.OK("操作成功");
        }
        return Result.Error("操作失败");
    }

    /**
     * 用戶信息
     *
     * @param tokenUser
     * @return
     */
    @Override
    public Result info(TokenUser tokenUser) {
        Map<String, Object> map = new HashMap<>(6);
        map.put("customerId", tokenUser.getCustomerId());
        map.put("customerName", tokenUser.getCustomerName());

    QueryWrapper<CustomerRole> wrapper=new QueryWrapper();
        wrapper.eq("customer_id",tokenUser.getCustomerId());
    CustomerRole customerRole = customerRoleMapper.selectOne(wrapper);
    Integer roleId = customerRole.getRoleId();
    QueryWrapper<Role> roleWrapper = new QueryWrapper();
        roleWrapper.eq("role_id",roleId);
    List<Role> roleList = roleMapper.selectList(roleWrapper);

    //  List<Role> roleList = roleService.roleListByCustomerId(tokenUser.getCustomerId());
        map.put("roleList", roleList);
        return new Result(map);
}

    /**
     * 获取管理用户列表
     *
     * @param current
     * @param size
     * @param customerName
     * @return
     */
    @Override
    public Result managerList(Integer current, Integer size, String customerName) {
        IPage page = new Page(null == current ? 1 : current, null == size ? 10 : size);
        IPage<Map> customerIPage = baseMapper.getManagerCustomerInfo(page, customerName);
        return new Result(customerIPage);
    }

    /**
     * 修改指定用户状态
     *
     * @param customerId
     * @param status
     * @return
     */
    @Override
    public Result updateStatus(Long customerId, Integer status) {
        boolean update = update(new UpdateWrapper<Customer>().eq("customer_id", customerId).set("customer_status", status)
                .set("update_uid", customerId).set("update_time", new Date()));
        if (update) {
            Object o = redisUtil.get(RedisKey.USER_TOKEN + customerId);
            if (null != o) {
                redisUtil.deleteKey(RedisKey.USER_TOKEN + customerId, RedisKey.TOKEN_USER + o);
            }
            return Result.OK("操作成功");
        }
        return Result.Error("操作失败");
    }
}