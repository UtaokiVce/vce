package com.weilai9.service.base.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weilai9.common.constant.EnableConstants;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.SysButton;
import com.weilai9.dao.mapper.SysButtonMapper;
import com.weilai9.dao.vo.base.AddButtonVO;
import com.weilai9.dao.vo.base.UpdateButtonVO;
import com.weilai9.service.base.SysButtonService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 系统按钮表(SysButton)表服务实现类
 *
 * @author china.fuyao@outlook.com
 * @since 2020-03-25 18:29:30
 */
@Service("sysButtonService")
public class SysButtonServiceImpl extends ServiceImpl<SysButtonMapper, SysButton> implements SysButtonService {
    /**
     * 新增按钮
     *
     * @param addButtonVO
     * @param customerId
     * @return
     */
    @Override
    public Result addButton(AddButtonVO addButtonVO, Long customerId) {
        SysButton sb = new SysButton();
        BeanUtil.copyProperties(addButtonVO, sb);
        sb.setStatus(EnableConstants.ENABLE);
        sb.setAddUid(customerId);
        sb.setUpdateUid(customerId);
        Date date = new Date();
        sb.setAddTime(date);
        sb.setUpdateTime(date);
        boolean save = this.save(sb);
        if (save) {
            return Result.OK("新增成功");
        }
        return Result.Error("新增失败");
    }

    /**
     * 修改按钮
     *
     * @param updateButtonVO
     * @param customerId
     * @return
     */
    @Override
    public Result updateButton(UpdateButtonVO updateButtonVO, Long customerId) {
        SysButton sb = new SysButton();
        BeanUtil.copyProperties(updateButtonVO, sb);
        sb.setUpdateUid(customerId);
        sb.setUpdateTime(new Date());
        boolean update = this.updateById(sb);
        if (update) {
            return Result.OK("更新成功");
        }
        return Result.Error("更新失败");
    }

    /**
     * 修改按钮状态
     *
     * @param btnId
     * @param status
     * @param customerId
     * @return
     */
    @Override
    public Result updateStatus(Integer btnId, Integer status, Long customerId) {

        SysButton sb = new SysButton();
        sb.setBtnId(btnId);
        sb.setStatus(status);
        sb.setUpdateUid(customerId);
        sb.setUpdateTime(new Date());
        boolean update = this.updateById(sb);
        if (update) {
            return Result.OK("更新成功");
        }
        return Result.Error("更新失败");
    }
}