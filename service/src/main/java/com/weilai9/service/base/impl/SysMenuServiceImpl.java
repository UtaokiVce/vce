package com.weilai9.service.base.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weilai9.common.constant.EnableConstants;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.RoleMenuBtn;
import com.weilai9.dao.entity.SysMenu;
import com.weilai9.dao.mapper.SysMenuMapper;
import com.weilai9.dao.vo.base.AddMenuVO;
import com.weilai9.dao.vo.base.UpdateMenuVO;
import com.weilai9.service.base.RoleMenuBtnService;
import com.weilai9.service.base.SysMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

/**
 * 系统菜单表(SysMenu)表服务实现类
 *
 * @author china.fuyao@outlook.com
 * @since 2020-03-25 18:29:30
 */
@Service("sysMenuService")
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
    @Resource
    RoleMenuBtnService roleMenuBtnService;

    /**
     * 新增菜单
     *
     * @param addMenuVo
     * @param customerId
     * @return
     */
    @Override
    public Result addMenu(AddMenuVO addMenuVo, Long customerId) {
        SysMenu sm = new SysMenu();
        BeanUtil.copyProperties(addMenuVo, sm);
        sm.setStatus(EnableConstants.ENABLE);
        sm.setAddUid(customerId);
        sm.setUpdateUid(customerId);
        Date date = new Date();
        sm.setAddTime(date);
        sm.setUpdateTime(date);
        boolean save = this.save(sm);
        if (save) {
            return Result.OK("新增成功");
        }
        return Result.Error("新增失败");
    }

    /**
     * 修改菜单
     *
     * @param updateMenuVO
     * @param customerId
     * @return
     */
    @Override
    public Result updateMenu(UpdateMenuVO updateMenuVO, Long customerId) {
        SysMenu sm = new SysMenu();
        BeanUtil.copyProperties(updateMenuVO, sm);
        sm.setUpdateUid(customerId);
        sm.setUpdateTime(new Date());
        boolean update = this.updateById(sm);
        if (update) {
            return Result.OK("更新成功");
        }
        return Result.Error("更新失败");
    }

    /**
     * 修改菜单状态
     *
     * @param menuId
     * @param status
     * @param customerId
     * @return
     */
    @Override
    public Result updateStatus(Integer menuId, Integer status, Long customerId) {
        SysMenu sm = new SysMenu();
        sm.setMenuId(menuId);
        sm.setStatus(status);
        sm.setUpdateUid(customerId);
        sm.setUpdateTime(new Date());
        boolean update = this.updateById(sm);
        if (update) {
            return Result.OK("更新成功");
        }
        return Result.Error("更新失败");
    }

    /**
     * 删除菜单
     *
     * @param menuId
     * @return
     */
    @Override
    @Transactional
    public Result deleteMenu(Integer menuId) {
        //判断菜单是否有下级
        SysMenu father = getOne(new QueryWrapper<SysMenu>().eq("pid", menuId));
        if (Objects.nonNull(father)) {
            return Result.Error("存在下级，请先删除下级菜单");
        }
        //删除菜单本身
        boolean remove = this.removeById(menuId);
        if (remove) {
            //删除角色下菜单
            roleMenuBtnService.remove(new QueryWrapper<RoleMenuBtn>().eq("menu_id", menuId));
            return Result.OK("操作成功");
        }
        return Result.Error("操作失败");
    }
}