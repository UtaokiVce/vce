package com.weilai9.service.wechat;


import com.baomidou.mybatisplus.extension.service.IService;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.weilai9.dao.entity.WxUser;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author qyb
 * @since 2020-05-12
 */
public interface WxUserService extends IService<WxUser> {


    Map<String, Object> entPayTest(WxPayService wxService) throws WxPayException;

    Map<String, Object> WxBusinessLogin(String name, String headUrl, String openId, String unionId, String phone);

    Map<String, Object> WxUserLogin(String name, String headUrl, String openId, String unionId, String phone);
}
