package com.weilai9.service.base.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weilai9.common.constant.EnableConstants;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.SysDict;
import com.weilai9.dao.mapper.SysDictMapper;
import com.weilai9.dao.vo.base.AddDictVO;
import com.weilai9.dao.vo.base.UpdateDictVO;
import com.weilai9.service.base.SysDictService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 系统接口(SysInterface)表服务实现类
 *
 * @author china.fuyao@outlook.com
 * @since 2020-03-26 11:42:31
 */
@Service("sysDictService")
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService {

    @Resource
    private SysDictMapper sysDictMapper;

    /**
     * 新增字典
     *
     * @param addDictVO
     * @param customerId
     * @return
     */
    @Override
    @Transactional
    public Result addDict(AddDictVO addDictVO, Long customerId) {
        SysDict code = getOne(new QueryWrapper<SysDict>().eq("dict_code", addDictVO.getDictCode()));
        if (null != code) {
            return Result.Error("已存在字典编码");
        }
        SysDict parent = getById(addDictVO.getDictPid());
        Integer dictLevel = 1;
        String fullPath = "";
        if (null != parent) {
            dictLevel = parent.getDictLevel() + 1;
            fullPath = parent.getDictFullPath();
        }
        SysDict sd = new SysDict();
        BeanUtil.copyProperties(addDictVO, sd);
        //
        sd.setDictLevel(dictLevel);
        //
        sd.setDictStatus(EnableConstants.ENABLE);
        sd.setAddUid(customerId);
        sd.setUpdateUid(customerId);
        Date date = new Date();
        sd.setAddTime(date);
        sd.setUpdateTime(date);
        boolean save = this.save(sd);
        if (save) {
            if (StringUtils.isNotBlank(fullPath)) {
                fullPath = fullPath + "," + sd.getDictId();
            } else {
                fullPath = "" + sd.getDictId();
            }
            boolean update = update(new UpdateWrapper<SysDict>().eq("dict_id", sd.getDictId()).set("dict_full_path", fullPath));
            if (update) {
                return Result.OK("操作成功");
            }
        }
        return Result.Error("操作失败");
    }

    /**
     * 修改字典
     *
     * @param updateDictVO
     * @param customerId
     * @return
     */
    @Override
    public Result updateDict(UpdateDictVO updateDictVO, Long customerId) {
        SysDict sd = new SysDict();
        BeanUtil.copyProperties(updateDictVO, sd);
        sd.setUpdateTime(new Date());
        sd.setUpdateUid(customerId);
        boolean update = updateById(sd);
        if (update) {
            return Result.OK("操作成功");
        }
        return Result.Error("操作失败");
    }

    /**
     * 删除字典
     *
     * @param dictId
     * @return
     */
    @Override
    public Result deleteDict(Integer dictId) {
        List<SysDict> pidList = list(new QueryWrapper<SysDict>().eq("dict_pid", dictId));
        if (null != pidList && pidList.size() > 0) {
            return Result.Error("存在子级,请先删除子级");
        }
        boolean remove = removeById(dictId);
        if (remove) {
            return Result.OK("操作成功");
        }
        return Result.Error("操作失败");
    }

    @Override
    public Result getDictListByparentCode(String dictcode){
        SysDict sysDict=sysDictMapper.selectOne(new QueryWrapper<SysDict>().eq("dict_code",dictcode).eq("dict_status",1).last(" limit 1"));
        if(sysDict==null){
            return new Result(new ArrayList());
        }
        List<SysDict> pidList = list(new QueryWrapper<SysDict>().eq("dict_pid", sysDict.getDictId()).eq("dict_status",1));
        if(pidList==null){
            pidList=new ArrayList();
        }
        return new Result(pidList);
    }
}