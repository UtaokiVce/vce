package com.weilai9.service.admin.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.ParadiseRefundApply;
import com.weilai9.dao.mapper.ParadiseRefundApplyDao;
import com.weilai9.service.admin.ParadiseRefundApplyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 乐园退款审核表(ParadiseRefundApply)表服务实现类
 *
 * @author makejava
 * @since 2020-05-08 10:09:44
 */
@Service("paradiseRefundApplyService")
public class ParadiseRefundApplyServiceImpl extends ServiceImpl<ParadiseRefundApplyDao, ParadiseRefundApply> implements ParadiseRefundApplyService {
    @Resource
    private ParadiseRefundApplyDao paradiseRefundApplyDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public ParadiseRefundApply queryById(Integer id) {
        return this.paradiseRefundApplyDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<ParadiseRefundApply> queryAllByLimit(int offset, int limit) {
        return this.paradiseRefundApplyDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param paradiseRefundApply 实例对象
     * @return 实例对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ParadiseRefundApply insert(ParadiseRefundApply paradiseRefundApply) {
        this.paradiseRefundApplyDao.insert(paradiseRefundApply);
        return paradiseRefundApply;
    }

    /**
     * 修改数据
     *
     * @param paradiseRefundApply 实例对象
     * @return 实例对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ParadiseRefundApply update(ParadiseRefundApply paradiseRefundApply) {
        this.paradiseRefundApplyDao.update(paradiseRefundApply);
        return this.queryById(paradiseRefundApply.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.paradiseRefundApplyDao.deleteById(id) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addRefund(ParadiseRefundApply obj) {
        obj.setStatus(0);
        obj.setCreateTime(new Date());
        obj.setUpdateTime(new Date());
        boolean b = save(obj);
        if(b){
        //返回订单信息
            Map<String,Object> map = baseMapper.getInfo(obj.getId());
            return new Result(map);
        }else{
            return Result.Error("添加失败！");
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateRefund(ParadiseRefundApply obj) {
        obj.setUpdateTime(new Date());
        boolean b = updateById(obj);
        if(b){
            //返回订单信息
            Map<String,Object> map = baseMapper.getInfo(obj.getId());
            return new Result(map);
        }else{
            return Result.Error("添加失败！");
        }

    }

    @Override
    public Result getMarketCode(Integer id) {
            String marketCode = baseMapper.getMarketCode(id);
            return new Result(marketCode);

    }
}