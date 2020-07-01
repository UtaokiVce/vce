package com.weilai9.service.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.SysButton;
import com.weilai9.dao.vo.base.AddButtonVO;
import com.weilai9.dao.vo.base.UpdateButtonVO;

/**
 * 系统按钮表(SysConfig)表服务接口
 *
 * @author china.fuyao@outlook.com
 * @since 2020-03-25 18:29:29
 */
public interface SysButtonService extends IService<SysButton> {
    /**
     * 新增按钮
     * @param addButtonVO
     * @param customerId
     * @return
     */
    Result addButton(AddButtonVO addButtonVO, Long customerId);

    /**
     * 修改按钮
     * @param updateButtonVO
     * @param customerId
     * @return
     */
    Result updateButton(UpdateButtonVO updateButtonVO, Long customerId);

    /**
     * 修改按钮状态
     * @param btnId
     * @param status
     * @param customerId
     * @return
     */
    Result updateStatus(Integer btnId, Integer status, Long customerId);
}