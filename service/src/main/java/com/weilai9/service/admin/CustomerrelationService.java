package com.weilai9.service.admin;


import com.baomidou.mybatisplus.extension.service.IService;
import com.weilai9.dao.entity.Customerrelation;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xjh
 * @since 2020-05-20
 */
public interface CustomerrelationService extends IService<Customerrelation> {

    Map<String, Object> monthList(Integer pageno, Integer pagesize);

    Map<String, Object> homeInfo();

    Map<String, Object> monthInfo(Integer year, Integer month, Integer pageno, Integer pagesize);

    Map<String, Object> teamList(Integer pageno, Integer pagesize);

    Map<String, Object> todyaInfo(Integer type);

    Map<String, Object> monthInfo(Integer type);

    Map<String, Object> totalInfo(Integer year);
}
