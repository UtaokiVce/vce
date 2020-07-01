package com.weilai9.service.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.SysMenu;
import com.weilai9.dao.vo.base.AddMenuVO;
import com.weilai9.dao.vo.base.UpdateMenuVO;

/**
 * 系统菜单表(SysConfig)表服务接口
 *
 * @author china.fuyao@outlook.com
 * @since 2020-03-25 18:29:29
 */
public interface SysMenuService extends IService<SysMenu> {
    /**
     * 新增菜单
     *
     * @param addMenuVo
     * @param customerId
     * @return
     */
    Result addMenu(AddMenuVO addMenuVo, Long customerId);

    /**
     * 更新菜单
     *
     * @param updateMenuVO
     * @param customerId
     * @return
     */
    Result updateMenu(UpdateMenuVO updateMenuVO, Long customerId);

    /**
     * 修改菜单状态
     *
     * @param menuId
     * @param status
     * @param customerId
     * @return
     */
    Result updateStatus(Integer menuId, Integer status, Long customerId);

    /**
     * 删除菜单
     * @param menuId
     * @return
     */
    Result deleteMenu(Integer menuId);
}