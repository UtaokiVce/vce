package com.weilai9.service.admin;


import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.Store;
import springfox.documentation.annotations.ApiIgnore;

public interface AdminStoreService {
    /**
     * 添加门店
     * @param store
     * @param tokenUser
     * @return
     */
    Result addStore(Store store, TokenUser tokenUser);

    /**
     * 门店状态禁用启用
     * @param state
     * @param storeId
     * @param tokenUser
     * @return
     */
    Result changeStoreState(Integer state,Integer storeId,TokenUser tokenUser);


    /**
     * 门店商品核销
     * @param
     * @param tokenUser
     * @return
     */
    Result goodsQr(String QrCode,TokenUser tokenUser);

    /**
     * 根据核销码查询商品信息
     * @param QrCode
     * @param tokenUser
     * @return
     */
    Result goodsQrInfo(String QrCode,TokenUser tokenUser);


    /**
     * 获取商店列表
     * @param pageno
     * @param pagesize
     * @param storeName
     * @return
     */
    Result getStoreList(Integer pageno,Integer pagesize,String storeName);


    /**
     * 修改推荐状态
     * @param isRem
     * @param storeId
     * @return
     */
    Result changeRem(Integer isRem,Integer storeId);
}