package com.weilai9.service.admin.impl;

import com.weilai9.common.constant.Result;
import com.weilai9.dao.mapper.ActivityMsgDao;
import com.weilai9.service.admin.IndexService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class IndexServiceImpl implements IndexService {

    @Resource
    private ActivityMsgDao activityMsgDao;

    @Override
    public Result getBanner() {
        List<Map<String,Object>> map = activityMsgDao.getBanner();
        return new Result(map);
    }
}
