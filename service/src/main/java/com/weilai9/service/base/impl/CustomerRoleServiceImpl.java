package com.weilai9.service.base.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weilai9.dao.entity.CustomerRole;
import com.weilai9.dao.mapper.CustomerRoleMapper;
import com.weilai9.service.base.CustomerRoleService;
import org.springframework.stereotype.Service;

/**
 * (Role)表服务实现类
 *
 * @author china.fuyao@outlook.com
 * @since 2020-03-25 18:19:12
 */
@Service("customerRoleService")
public class CustomerRoleServiceImpl extends ServiceImpl<CustomerRoleMapper, CustomerRole> implements CustomerRoleService {
}