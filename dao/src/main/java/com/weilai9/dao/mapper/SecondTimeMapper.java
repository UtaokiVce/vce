package com.weilai9.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weilai9.dao.entity.SecondTime;

import java.util.List;
import java.util.Map;


public interface SecondTimeMapper extends BaseMapper<SecondTime> {
    List<Map<String,Object>> getSecendTimeList();
}