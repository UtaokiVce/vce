package com.weilai9.service.api;


import com.baomidou.mybatisplus.extension.service.IService;
import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.Customer;
import org.springframework.web.multipart.MultipartFile;

/**
 * (User)表服务接口
 *
 * @author china.fuyao@outlook.com
 * @since 2020-03-25 18:20:47
 */
public interface ApiCustomerService extends IService<Customer> {
    /**
     * 用户账号密码注册
     * @param phone
     * @param password
     * @return
     */
    Result register(String phone, String password);
    /**
     * 用户账号密码登录
     * @param phone
     * @param password
     * @return
     */
    Result login(String phone, String password);

    /**
     * 用户登出
     * @param tokenUser
     * @return
     */
    Result logout(TokenUser tokenUser);

    Result uploadImg(MultipartFile file);
}