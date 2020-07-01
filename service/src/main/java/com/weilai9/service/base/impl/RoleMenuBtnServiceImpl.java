package com.weilai9.service.base.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weilai9.dao.entity.RoleMenuBtn;
import com.weilai9.dao.mapper.RoleMenuBtnMapper;
import com.weilai9.service.base.RoleMenuBtnService;
import org.springframework.stereotype.Service;

/**
 * (Role)表服务实现类
 *
 * @author china.fuyao@outlook.com
 * @since 2020-03-25 18:19:12
 */
@Service("roleMenuBtnService")
public class RoleMenuBtnServiceImpl extends ServiceImpl<RoleMenuBtnMapper, RoleMenuBtn> implements RoleMenuBtnService {

}