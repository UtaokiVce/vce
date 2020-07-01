package com.weilai9.common.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 处理新增和更新的基础数据填充，配合BaseEntity和MyBatisPlusConfig使用
 */
@Slf4j
@Component
public class MetaHandler implements MetaObjectHandler {
    /**
     * 新增数据执行
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("addUid", getCustomerId(), metaObject);
        this.setFieldValByName("updateUid", getCustomerId(), metaObject);
        this.setFieldValByName("addTime", new Date(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }

    private Long getCustomerId() {
        return null;
    }

    /**
     * 更新数据执行
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateUid", getCustomerId(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }

}
