package com.weilai9.service.admin;


import com.baomidou.mybatisplus.extension.service.IService;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.Banner;
import com.weilai9.dao.vo.activity.AddBannerVO;
import com.weilai9.dao.vo.activity.UpdateBannerVO;

import java.util.List;

/**
 * banner表(Banner)表服务接口
 *
 * @author makejava
 * @since 2020-06-10 14:19:35
 */
public interface BannerService extends IService<Banner> {

    /**
     * 通过ID查询单条数据
     *
     * @param bannerId 主键
     * @return 实例对象
     */
    Banner queryById(Integer bannerId);


    /**
     * 修改数据
     *
     * @param banner 实例对象
     * @return 实例对象
     */
    Banner update(Banner banner);

    /**
     * 通过主键删除数据
     *
     * @param bannerId 主键
     * @return 是否成功
     */
    Result deleteById(Integer bannerId);

    Result selectList(Integer page, Integer size, Integer type, Integer site,Integer state);

    Result addBanner(AddBannerVO vo);

    Result updateByVO(UpdateBannerVO vo);

    Result getDetails(Integer bannerId);

    Result updateState(Integer id, Integer state);

    Result updateBannerSort(Integer id, Integer operation);
}