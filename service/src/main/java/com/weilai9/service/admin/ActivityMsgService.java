package com.weilai9.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.weilai9.common.config.auth.TokenUser;
import com.weilai9.common.constant.Result;
import com.weilai9.dao.entity.ActivityMsg;
import com.weilai9.dao.entity.ActivitySignup;
import com.weilai9.dao.entity.SysDict;
import com.weilai9.dao.vo.activity.AddActivityMsgVO;
import com.weilai9.dao.vo.activity.UpdateActivityMsgVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * (ActivityMsg)表服务接口
 *
 * @author makejava
 * @since 2020-04-26 17:56:00
 */
public interface ActivityMsgService extends IService<ActivityMsg> {

    Result getAll(int page,int size,String introductory);

    Result getActByType(String activityType);

    Result getActType();

    Result addActivity(AddActivityMsgVO vo,Long userId);

    Result delActivity(Integer activityId);

    Result updateActivity(UpdateActivityMsgVO activityMsg);

    Result getActByStore(int page,int size,Long customerId);

    Result getActById(Integer actId);

    Result getActRegistration(Integer current, Integer size, Integer actId);

    Result again(Integer actId);

    Result getRefusedReason(Integer id);

    Result getActByUser(Integer status);

    Result addActType(SysDict obj);


    Result actPay(Integer orderId);

    Result getActIndex();

    Result switchAct(Integer id, Integer status);
}