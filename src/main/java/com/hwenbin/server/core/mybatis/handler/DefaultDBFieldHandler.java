package com.hwenbin.server.core.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.hwenbin.server.core.entity.BaseEntity;
import com.hwenbin.server.util.ContextUtils;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;
import java.util.Objects;

/**
 * 通用参数填充实现类
 *
 * 如果没有显式的对通用参数进行赋值，这里会对通用参数进行填充、赋值
 *
 * @author hwb
 * @create 2022-03-26
 */
public class DefaultDBFieldHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        if (Objects.nonNull(metaObject) && metaObject.getOriginalObject() instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) metaObject.getOriginalObject();

            Date current = new Date();
            // 创建时间为空，则以当前时间为插入时间
            if (Objects.isNull(baseEntity.getCreateTime())) {
                baseEntity.setCreateTime(current);
            }
            // 更新时间为空，则以当前时间为更新时间
            if (Objects.isNull(baseEntity.getUpdateTime())) {
                baseEntity.setUpdateTime(current);
            }

            String name = Objects.requireNonNull(ContextUtils.getRequest()).getUserPrincipal().getName();
            // 当前登录用户不为空
            if (Objects.nonNull(name)) {
                // 创建人为空，则当前登录用户为创建人
                if (Objects.isNull(baseEntity.getCreator())) {
                    baseEntity.setCreator(name);
                }
                // 更新人为空，则当前登录用户为更新人
                if (Objects.isNull(baseEntity.getUpdater())) {
                    baseEntity.setUpdater(name);
                }
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新时间为空，则以当前时间为更新时间
        Object updateTime = getFieldValByName("updateTime", metaObject);
        if (Objects.isNull(updateTime)) {
            setFieldValByName("updateTime", new Date(), metaObject);
        }

        // 当前登录用户不为空，更新人为空，则当前登录用户为更新人
        Object updater = getFieldValByName("updater", metaObject);
        String name = Objects.requireNonNull(ContextUtils.getRequest()).getUserPrincipal().getName();
        if (Objects.nonNull(name) && Objects.isNull(updater)) {
            setFieldValByName("updater", name, metaObject);
        }
    }

}
