package com.weilai9.service.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.SysDict;
import com.weilai9.dao.vo.base.AddDictVO;
import com.weilai9.dao.vo.base.UpdateDictVO;

/**
 * (Interface)表服务接口
 *
 * @author china.fuyao@outlook.com
 * @since 2020-03-26 11:42:31
 */
public interface SysDictService extends IService<SysDict> {
    /**
     * 新增字典
     * @param addDictVO
     * @param customerId
     * @return
     */
    Result addDict(AddDictVO addDictVO, Long customerId);

    /**
     * 修改字典
     * @param updateDictVO
     * @param customerId
     * @return
     */
    Result updateDict(UpdateDictVO updateDictVO, Long customerId);

    /**
     * 删除字典
     * @param dictId
     * @return
     */
    Result deleteDict(Integer dictId);

    /**
     * 根据父级字段code获取所有子级
     * @param dictcode
     * @return
     */
    Result getDictListByparentCode(String dictcode);
}