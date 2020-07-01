package com.weilai9.service.admin.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.Banner;
import com.weilai9.dao.entity.Store;
import com.weilai9.dao.mapper.BannerDao;
import com.weilai9.dao.mapper.StoreMapper;
import com.weilai9.dao.vo.activity.AddBannerVO;
import com.weilai9.dao.vo.activity.UpdateBannerVO;
import com.weilai9.service.admin.BannerService;
import com.weilai9.service.api.ApiGoodsService;
import org.apache.velocity.runtime.directive.Foreach;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * banner表(Banner)表服务实现类
 *
 * @author makejava
 * @since 2020-06-10 14:19:35
 */
@Service("bannerService")
public class BannerServiceImpl extends ServiceImpl<BannerDao, Banner> implements BannerService {
    @Resource
    private BannerDao bannerDao;

    @Resource
    private ApiGoodsService apiGoodsService;
    @Resource
    private StoreMapper storeMapper;


    /**
     * 通过ID查询单条数据
     *
     * @param bannerId 主键
     * @return 实例对象
     */
    @Override
    public Banner queryById(Integer bannerId) {
        return this.bannerDao.queryById(bannerId);
    }


    /**
     * 修改数据
     *
     * @param banner 实例对象
     * @return 实例对象
     */
    @Override
    public Banner update(Banner banner) {
        this.bannerDao.update(banner);
        return this.queryById(banner.getBannerId());
    }

    /**
     * 通过主键删除数据
     *
     * @param bannerId 主键
     * @return 是否成功
     */
    @Override
    public Result deleteById(Integer bannerId) {

        Banner banner = getById(bannerId);
        if(banner.getState()!=0 || banner.getSort()!=0){
            return Result.Error("开启的banner不允许删除！");
        }

        int i = this.bannerDao.deleteById(bannerId);
        if (i > 0) {
            return Result.OK("删除成功");
        } else {
            return Result.Error("删除失败");
        }
    }

    @Override
    public Result selectList(Integer page, Integer size, Integer type, Integer site,Integer state) {
        Page page1 = new Page(page, size);
        QueryWrapper<Banner> wrapper = new QueryWrapper<>();
        if(state != null){
            wrapper.eq("state",state);
        }
        if (type != null) {
            wrapper.eq("type", type);
        }
        if (site != null) {
            wrapper.eq("site", site);
        }
        wrapper.last(" order by sort desc");
        IPage page2 = page(page1, wrapper);

        return new Result(page2);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addBanner(AddBannerVO vo) {

        Banner banner = new Banner();
        BeanUtil.copyProperties(vo, banner);
      /*  List<Banner> list = list(new QueryWrapper<Banner>().ne("sort", 0));
        list.forEach(bannerOne->{
            bannerOne.setSort(bannerOne.getSort()-1);
            if(bannerOne.getSort() == 0){
                bannerOne.setState(0);
            }
            update(bannerOne);
        });*/
        banner.setSort(0);
        banner.setState(0);
        boolean b = save(banner);
        if (b) {
            return Result.OK("添加成功");
        } else {
            return Result.Error("添加失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateByVO(UpdateBannerVO vo) {
        Banner banner = new Banner();
        BeanUtil.copyProperties(vo, banner);
        boolean b = updateById(banner);
        if (b) {
            return Result.OK("修改成功");
        } else {
            return Result.Error("修改失败");
        }
    }

    @Override
    public Result getDetails(Integer bannerId) {
        Banner banner = getById(bannerId);
        if (banner == null) {
            return Result.Error("未查询到数据");
        }
        //商品
        if (banner.getType() == 1) {
            //商品id
            Integer objectId = banner.getObjectId();
            Result goodsInfo = apiGoodsService.getGoodsInfo(objectId);
            return goodsInfo;
            //商家
        } else if (banner.getType() == 2) {
            Integer objectId = banner.getObjectId();
            Store store = storeMapper.selectById(objectId);
            return new Result(store);
        } else if (banner.getType() == 3) {
            //文本
            String details = getById(bannerId).getTitle();
            return new Result(details);
        }
        return null;

    }

    public void down(List<Banner> list){
        if(list.size()>0){
            list.forEach(banner1 -> {
                banner1.setSort(banner1.getSort()-1);
                updateById(banner1);
            });
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateState(Integer id, Integer state) {
        Banner banner = getById(id);
        //当前数据排序值
        Integer sort = banner.getSort();
        //开启->禁用
        if(state == 0){
            banner.setState(0);
            banner.setSort(0);
            //把比当前排序高的数据状态-1
            List<Banner> list = list(new QueryWrapper<Banner>().gt("sort", sort));
            down(list);
            updateById(banner);
            return Result.OK("修改成功！");
            //禁用 -> 开启
        }else if(state == 1){
            List<Banner> list = list(new QueryWrapper<Banner>().ne("state", 0));
            if(list.size()>=5){
                return Result.Error("只能开启5条数据，请关闭一条后重试！");
            }else{
                //查询缺失排序值
                Integer[] arr = {5,4,3,2,1};
                //排序list
                List<Integer> list1 = Arrays.asList(arr);
                List<Integer> list3 = new ArrayList();
                list3.addAll(list1);
                List<Integer> sortList = baseMapper.getSortList();
                for (Integer s:sortList){
                    list3.remove(s);
                }
                Integer m = list3.get(0);
                //大于m的全部-1
                List<Banner> list2 = list(new QueryWrapper<Banner>().gt("sort", m));
                down(list2);
                banner.setState(1);
                banner.setSort(5);
                updateById(banner);
            }
            return Result.OK("修改成功！");
        }
        return null;
    }

    @Override
    public Result updateBannerSort(Integer id, Integer operation) {
        //当前banner
        Banner banner = getById(id);
        //当前排序
        Integer currSort = banner.getSort();
        //上移 当前sort+1  将上一条数据sort-1
        if(operation == 1){
            if(currSort == 5){
                return Result.Error("操作失败，已经是第一位了！");
            }
            banner.setSort(banner.getSort()+1);
            Banner banner1 = getOne(new QueryWrapper<Banner>().eq("sort", currSort + 1).last(" limit 1"));
            if(banner1 != null){
                banner1.setSort(currSort);
                update(banner1);
            }
            update(banner);
            return Result.OK("操作成功！");

        }else if(operation == 2){
        //下移 当前sort-1  将下一条数据sort+1
            if(currSort == 1){
                return Result.Error("操作失败，已经是最后一位了！");
            }
            banner.setSort(banner.getSort()-1);
            Banner banner1 = getOne(new QueryWrapper<Banner>().eq("sort", currSort - 1).last(" limit 1"));
            if(banner1 != null){
                banner1.setSort(currSort);
                update(banner1);
            }
            update(banner);
            return Result.OK("操作成功！");
        }else if(operation == 3){
            //置顶 当前sort设为5，比他高的-1
            if(currSort == 5){
                return Result.Error("置顶失败，已经是第一位了！");
            }
            banner.setSort(5);
            List<Banner> banners = list(new QueryWrapper<Banner>().gt("sort", currSort));
            if(banners.size()>0){
                banners.forEach(banner2->{
                    banner2.setSort(banner2.getSort()-1);
                    update(banner2);
                });
            }
            update(banner);
            return Result.OK("操作成功！");
        }

        return Result.Error("操作失败");
    }

}