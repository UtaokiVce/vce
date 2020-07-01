package com.weilai9.service.api;


import com.baomidou.mybatisplus.extension.service.IService;
import com.weilai9.dao.entity.Customerqinzuan;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xjh
 * @since 2020-06-03
 */
public interface CustomerqinzuanService extends IService<Customerqinzuan> {

    Map<String, Object> teamTodayInfo();

    Map<String, Object> teamMonthInfo();

    Map<String, Object> teamTotalInfo(Integer year);

    Map<String, Object> qzList();

    Map<String, Object> monthWaitInfo();
}
