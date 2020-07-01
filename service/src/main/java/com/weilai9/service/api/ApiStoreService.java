package com.weilai9.service.api;


import com.baomidou.mybatisplus.extension.service.IService;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.Category;
import com.weilai9.dao.entity.Store;

/**
 * (User)表服务接口
 *
 * @author china.fuyao@outlook.com
 * @since 2020-03-25 18:20:47
 */
public interface ApiStoreService extends IService<Store> {

    /**
     *  门店推荐列表
     * @return
     */
    public Result getCommendStoreList();


    /**
     * 后台获取门店推荐列表
     * @return
     */
    Result adminGetCommendStoreList();


    /**
     * 后台推荐门店排序
     * @param id
     * @param operation
     * @return
     */
    Result adminCommendStoreSort(Integer id, Integer operation);


    /**
     * 门店取消推荐
     * @param storeId
     * @return
     */
    Result store2NotRem(Integer storeId);
}