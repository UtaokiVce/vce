package com.weilai9.service.admin.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.common.utils.wechat.JobUtil;
import com.weilai9.common.utils.wechat.RedisHandle;
import com.weilai9.common.utils.wechat.ReturnUtil;
import com.weilai9.dao.entity.ShippingAddress;
import com.weilai9.dao.entity.WxUser;
import com.weilai9.dao.mapper.ShippingAddressDao;
import com.weilai9.dao.vo.activity.AddShippingAddressVO;
import com.weilai9.dao.vo.activity.UpdateShippingAddressVO;
import com.weilai9.service.admin.ShippingAddressService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * (ShippingAddress)表服务实现类
 *
 * @author makejava
 * @since 2020-05-13 11:35:25
 */
@Service("shippingAddressService")
public class ShippingAddressServiceImpl extends ServiceImpl<ShippingAddressDao, ShippingAddress> implements ShippingAddressService {
    @Resource
    private ShippingAddressDao shippingAddressDao;

    @Resource
    private HttpServletRequest request;
    @Resource
    private RedisHandle redisHandle;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public ShippingAddress queryById(Integer id) {
        return getById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<ShippingAddress> queryAllByLimit(int offset, int limit) {
        return this.shippingAddressDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param shippingAddress 实例对象
     * @return 实例对象
     */
    @Override
    public ShippingAddress insert(ShippingAddress shippingAddress) {
        this.shippingAddressDao.insert(shippingAddress);
        return shippingAddress;
    }

    /**
     * 修改数据
     *
     * @param shippingAddress 实例对象
     * @return 实例对象
     */
    @Override
    public ShippingAddress update(ShippingAddress shippingAddress) {
        this.shippingAddressDao.update(shippingAddress);
        return this.queryById(shippingAddress.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.shippingAddressDao.deleteById(id) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addAddress(AddShippingAddressVO obj) {

        String token = JobUtil.getAuthorizationToken(request);
        if(StrUtil.isBlank(token)){
            return Result.Error("token无效");
        }
        WxUser user = (WxUser) redisHandle.get(token);
        String customerId = user.getCustomerId();

        //地址拆为省市区
        String[] site = obj.getSite();

        ShippingAddress shippingAddress = new ShippingAddress();
        BeanUtil.copyProperties(obj,shippingAddress);
        List<ShippingAddress> list = list(new QueryWrapper<ShippingAddress>().eq("user_id", customerId));

        if(obj.getIsDefault() == null){
            shippingAddress.setIsDefault(0);
        }

        if(list.size()==0){
            //第一次设置，设为默认地址
            shippingAddress.setIsDefault(1);
        }
        shippingAddress.setProvince(site[0]);
        shippingAddress.setCity(site[1]);
        shippingAddress.setDistrict(site[2]);
        shippingAddress.setUserId(Integer.parseInt(customerId));
        shippingAddress.setUpdateTime(new Date());
        shippingAddress.setCreateTime(new Date());
        insert(shippingAddress);
        //int b = baseMapper.insert(shippingAddress);
//        if(b){
//            return Result.OK("添加成功");
//        }else{
//            return Result.Error("添加失败");
//        }
        return null;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateAddress(UpdateShippingAddressVO vo) {

        ShippingAddress obj = getById(vo.getId());
        if(vo.getIsDefault() !=null && vo.getIsDefault()==1){
            //取消之前的默认地址
            ShippingAddress one = getOne(new QueryWrapper<ShippingAddress>().eq("user_id", obj.getUserId())
                    .eq("is_default", 1).last("limit 1"));
            if(one != null){
                one.setIsDefault(0);
                update(one);
            }
        }
        //将默认的改为非默认，修改最近添加的为默认
        if(vo.getIsDefault() !=null && vo.getIsDefault()==0 && obj.getIsDefault()==1){
            //查询最近创建的数据
            ShippingAddress one = getOne(new QueryWrapper<ShippingAddress>().eq("user_id", obj.getUserId())
                    .eq("is_default", 0).last("limit 1").orderByDesc("create_time"));
            if(one != null){
                one.setIsDefault(1);
                update(one);
            }

        }
        BeanUtil.copyProperties(vo,obj);
        obj.setUpdateTime(new Date());
        boolean b = updateById(obj);
        if(b){
            return Result.OK("修改成功");
        }else{
            return Result.Error("修改失败");
        }
    }

    @Override
    public Result getMyAddress() {
        String token = JobUtil.getAuthorizationToken(request);
        if(StrUtil.isBlank(token)){
            return Result.Error("token无效");
        }
        WxUser user = (WxUser) redisHandle.get(token);
        String customerId = user.getCustomerId();
        List<ShippingAddress> list = list(new QueryWrapper<ShippingAddress>().eq("user_id", customerId));
        return new Result(list);
    }



    @Override
    public Result getAddDefault(Integer id) {
        String token = JobUtil.getAuthorizationToken(request);
        if(StrUtil.isBlank(token)){
            return Result.Error("token无效");
        }
        WxUser user = (WxUser) redisHandle.get(token);
        String customerId = user.getCustomerId();
        QueryWrapper<ShippingAddress> wrapper = new QueryWrapper<>();
        if(!(id == null || id ==0)){
            //查单个
            wrapper.eq("id",id);
        }else{
            //查默认
            wrapper.eq("is_default", 1);
        }
        ShippingAddress obj = getOne(wrapper.eq("user_id",customerId).last(" limit 1"));
        return new Result(obj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result deleteAddress(Integer id) {

        ShippingAddress obj = getById(id);
        //删除了默认，设置最近添加的为默认
        if(obj != null && obj.getIsDefault()==1){
            ShippingAddress one = getOne(new QueryWrapper<ShippingAddress>().eq("user_id", obj.getUserId())
                    .eq("is_default", 0).last("limit 1").orderByDesc("create_time"));
            if(one != null){
                one.setIsDefault(1);
                update(one);
            }
        }
        boolean b = removeById(obj.getId());
        if(b){
            return Result.OK("删除成功");
        }else{
            return Result.Error("删除失败");
        }
    }

    @Override
    public Result get(Integer id) {
        ShippingAddress address = getById(id);
        return new Result(address);
    }

}