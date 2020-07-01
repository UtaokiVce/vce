package com.weilai9.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.weilai9.dao.entity.Customer;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author xjh
 */
public interface CustomerMapper extends BaseMapper<Customer> {

    IPage<Map> getManagerCustomerInfo(IPage page, @Param("customerName") String customerName);
}
