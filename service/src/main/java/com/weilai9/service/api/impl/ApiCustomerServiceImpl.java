package com.weilai9.service.api.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weilai9.common.config.auth.JwtTokenUtil;
import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.*;
import com.weilai9.common.utils.oss.OSSUtil;
import com.weilai9.common.utils.redis.RedisUtil;
import com.weilai9.dao.entity.Customer;
import com.weilai9.dao.entity.CustomerRole;
import com.weilai9.dao.mapper.CustomerMapper;
import com.weilai9.service.api.ApiCustomerService;
import com.weilai9.service.base.CustomerRoleService;
import com.weilai9.service.base.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * (User)表服务实现类
 *
 * @author china.fuyao@outlook.com
 * @since 2020-03-25 18:20:48
 */
@Slf4j
@Service("apiCustomerService")
public class ApiCustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ApiCustomerService {

    @Resource
    RedisUtil redisUtil;
    @Resource
    JwtTokenUtil jwtTokenUtil;
    @Resource
    RoleService roleService;
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
     * 用户账号密码注册
     *
     * @param phone
     * @param password
     * @return
     */
    @Override
    @Transactional
    public Result register(String phone, String password) {
        //用户注册
        Customer customer = new Customer();
        customer.setCustomerPhone(phone);
        customer.setCustomerName(phone);
        customer.setCustomerPassword(BCrypt.hashpw(password));
        //普通用户
        customer.setCustomerType(CustomerConstants.NORMAL_USER);
        customer.setCustomerStatus(EnableConstants.ENABLE);
        Date date = new Date();
        customer.setAddTime(date);
        customer.setUpdateTime(date);
        boolean save = this.save(customer);
        if (save) {
            addNewCustomerByAddUid(customer.getCustomerId());
            //添加角色
            CustomerRole customerRole = new CustomerRole();
            customerRole.setCustomerId(customer.getCustomerId());
            customerRole.setRoleId(CustomerConstants.NORMAL_USER);
            customerRole.setRoleType(CustomerConstants.NORMAL_USER);
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
     * api login
     *
     * @param phone
     * @param password
     * @return
     */
    @Override
    public Result login(String phone, String password) {
        Customer customer = this.getOne(new QueryWrapper<Customer>().eq("customer_phone", phone).eq("customer_status", EnableConstants.ENABLE));
        if (null == customer) {
            return Result.Error("用户不存在");
        }
        //判断密码
        boolean checkPassword = BCrypt.checkpw(password, customer.getCustomerPassword());
        if (!checkPassword) {
            return Result.Error("用户密码错误");
        }
        TokenUser tokenUser = new TokenUser();
        tokenUser.setCustomerId(customer.getCustomerId());
        tokenUser.setCustomerName(customer.getCustomerName());
        tokenUser.setStatus(customer.getCustomerStatus());
        //获取权限
        ArrayList<String> authorities = roleService.roleNameListByCustomerId(tokenUser.getCustomerId());
        tokenUser.setAuthorities(authorities);
        String token = jwtTokenUtil.token(tokenUser);
        Map<String, String> map = new HashMap<>(2);
        map.put("token", token);
        map.put("issuer", TokenConstants.ISSUER_STRING);
        //更新登录时间
        updateLastLoginTime(customer.getCustomerId());
        return new Result(map);
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

    @Override
    public Result uploadImg(MultipartFile file) {
        log.info("文件上传");
        String filename = file.getOriginalFilename();
        System.out.println(filename);
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            String img = OSSUtil.uploadImg(inputStream);
            return new Result(img);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.Error("失败");
        }
    }
}