package com.weilai9.service.wechat.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.binarywang.wxpay.bean.entpay.EntPayRequest;
import com.github.binarywang.wxpay.bean.entpay.EntPayResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.EntPayService;
import com.github.binarywang.wxpay.service.WxPayService;
import com.weilai9.common.config.auth.JwtTokenUtil;
import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.utils.wechat.*;
import com.weilai9.dao.entity.Customer;
import com.weilai9.dao.entity.CustomerRole;
import com.weilai9.dao.entity.WxUser;
import com.weilai9.dao.mapper.CustomerMapper;
import com.weilai9.dao.mapper.CustomerRoleMapper;
import com.weilai9.dao.mapper.WxUserMapper;
import com.weilai9.service.base.RoleService;
import com.weilai9.service.wechat.WxUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author qyb
 * @since 2020-05-12
 */
@Service
public class WxUserServiceImpl extends ServiceImpl<WxUserMapper, WxUser> implements WxUserService {
    @Resource
    CustomerMapper customerMapper;
    @Resource
    CustomerRoleMapper customerRoleMapper;
    @Resource
    WxUserMapper wxUserMapper;
    @Resource
    RoleService roleService;
    @Resource
    JwtTokenUtil jwtTokenUtil;
    @Resource
    RedisHandle redisHandle;

    @Override
    public Map<String, Object> entPayTest(WxPayService wxService) throws WxPayException {
        EntPayService entPayService = wxService.getEntPayService();
        System.out.println(entPayService);
        String publicKey = entPayService.getPublicKey();

        EntPayRequest request = new EntPayRequest();
        request.setOpenid("oAJQh5S3LnB4mm_dTyuUjeWXT3rc");
        request.setPartnerTradeNo("1990041818180982872");
        request.setCheckName("FORCE_CHECK");
        request.setReUserName("许晋豪");
        request.setAmount(1);
        request.setDescription("企业付款信息ByXJH");
        request.setSpbillCreateIp("110.191.214.51");



        EntPayResult entPayResult = entPayService.entPay(request);
        return null;
    }

    @Override
    public Map<String, Object> WxBusinessLogin(String name, String headUrl, String openId, String unionId, String phone) {

        Map<String, Object> data = new HashMap<>(16);
        QueryWrapper<Customer> wrapper = new QueryWrapper();
        wrapper.eq("customer_phone",phone);
        Customer customer = customerMapper.selectOne(wrapper);

        String s = EmojiFilter.filterEmoji(name);
        if (customer==null){
            //用户信息
            customer = new Customer();
            customer.setCustomerPhone(phone);
            customer.setCustomerName(StrUtil.isEmpty(s) ? "emojiName" : s);
            customer.setAddTime(DateUtil.date());
            customer.setCustomerPassword(BCrypt.hashpw("123456"));
            customer.setCustomerFace(headUrl);
            customerMapper.insert(customer);
            Long customerId = customer.getCustomerId();
            //邀请码
            String sn = JobUtil.getSN(customerId.intValue(), 6);
            customer.setInvitationCode(sn);
            customer.updateById();
           //用户权限
            CustomerRole customerRole = new CustomerRole();
            customerRole.setRoleId(7);
            customerRole.setRoleType(1);
            customerRole.setCustomerId(customerId);
            customerRole.setAddTime(DateUtil.date());
            customerRole.setUpdateUid(customerId);
            customerRoleMapper.insert(customerRole);
            //用户微信信息
            WxUser wxUser = new WxUser();
            wxUser.setPhoneNum(phone)
                    .setNickname(StrUtil.isEmpty(s) ? "emojiName" : s)
                    .setCustomerId(customerId+"")
                    .setHeadImgUrl(headUrl)
                    .setOpenid(openId)
                    .setUnionid(unionId)
                    .insert();
            customer.setInvitationCode(sn);
            customer.updateById();
            String token = getToken(customer);
            redisHandle.set(token, wxUser, SysConst.MINIAPP_TOKEN_EXPIRETIME);
            data.put("wxUser", wxUser);
            data.put("token", token);
        }else {
            Long customerId = customer.getCustomerId();
            QueryWrapper<WxUser> WxUserWP = new QueryWrapper();
            WxUserWP.eq("customer_id",customerId);
            WxUser wxUser = wxUserMapper.selectOne(WxUserWP);
            String token = getToken(customer);
            redisHandle.set(token, wxUser, SysConst.MINIAPP_TOKEN_EXPIRETIME);
            data.put("wxUser", wxUser);
            data.put("token", token);
        }
        return ReturnUtil.returnMap(ApiStatus.SUCCESS,data);
    }

    @Override
    public Map<String, Object> WxUserLogin(String name, String headUrl, String openId, String unionId, String phone) {
        Map<String, Object> data = new HashMap<>(16);
        QueryWrapper<Customer> wrapper = new QueryWrapper();
        wrapper.eq("customer_phone",phone);
        Customer customer = customerMapper.selectOne(wrapper);

        String s = EmojiFilter.filterEmoji(name);
        if (customer==null){
            //用户信息
            customer = new Customer();
            customer.setCustomerPhone(phone);
            customer.setCustomerName(StrUtil.isEmpty(s) ? "emojiName" : s);
            customer.setAddTime(DateUtil.date());
            customer.setCustomerPassword(BCrypt.hashpw("123456"));
            customer.setCustomerFace(headUrl);
            customerMapper.insert(customer);
            Long customerId = customer.getCustomerId();
            //邀请码
            String sn = JobUtil.getSN(customerId.intValue(), 6);
            customer.setInvitationCode(sn);
            customer.updateById();
            //用户权限
            CustomerRole customerRole = new CustomerRole();
            customerRole.setRoleId(6);
            customerRole.setRoleType(0);
            customerRole.setCustomerId(customerId);
            customerRole.setAddTime(DateUtil.date());
            customerRole.setUpdateUid(customerId);
            customerRoleMapper.insert(customerRole);
            //用户微信信息
            WxUser wxUser = new WxUser();
            wxUser.setPhoneNum(phone)
                    .setNickname(StrUtil.isEmpty(s) ? "emojiName" : s)
                    .setCustomerId(customerId+"")
                    .setHeadImgUrl(headUrl)
                    .setOpenid(openId)
                    .setUnionid(unionId)
                    .insert();
            customer.setInvitationCode(sn);
            customer.updateById();
            String token = getToken(customer);
            redisHandle.set(token, wxUser, SysConst.MINIAPP_TOKEN_EXPIRETIME);
            data.put("wxUser", wxUser);
            data.put("token", token);
        }else {
            Long customerId = customer.getCustomerId();
            QueryWrapper<WxUser> WxUserWP = new QueryWrapper();
            WxUserWP.eq("customer_id",customerId);
            WxUser wxUser = wxUserMapper.selectOne(WxUserWP);
            String token = getToken(customer);
            redisHandle.set(token, wxUser, SysConst.MINIAPP_TOKEN_EXPIRETIME);
            data.put("wxUser", wxUser);
            data.put("token", token);
        }
        return ReturnUtil.returnMap(ApiStatus.SUCCESS,data);
    }


    private String getToken(Customer customer) {
        TokenUser tokenUser = new TokenUser(customer);
        //获取权限
        List<String> authorities = roleService.roleNameListByCustomerId(tokenUser.getCustomerId());
        tokenUser.setAuthorities(authorities);
        return jwtTokenUtil.token(tokenUser);
    }
}
