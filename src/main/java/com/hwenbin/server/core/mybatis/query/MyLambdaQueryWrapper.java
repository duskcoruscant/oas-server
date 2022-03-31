package com.hwenbin.server.core.mybatis.query;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * 拓展 MyBatis Plus QueryWrapper 类，主要增加如下功能：
 *
 * 1. 拼接条件的方法，增加 xxxIfPresent 方法，用于判断值不存在的时候，不要拼接到条件中。
 *
 * @author hwb
 * @create 2022-03-26
 *
 * @param <T> 数据类型
 */
public class MyLambdaQueryWrapper<T> extends LambdaQueryWrapper<T> {

    public MyLambdaQueryWrapper<T> likeIfPresent(SFunction<T, ?> column, String val) {
        if (StringUtils.hasText(val)) {
            return (MyLambdaQueryWrapper<T>) super.like(column, val);
        }
        return this;
    }

    public MyLambdaQueryWrapper<T> inIfPresent(SFunction<T, ?> column, Collection<?> values) {
        if (!CollectionUtils.isEmpty(values)) {
            return (MyLambdaQueryWrapper<T>) super.in(column, values);
        }
        return this;
    }

    public MyLambdaQueryWrapper<T> inIfPresent(SFunction<T, ?> column, Object... values) {
        if (!ArrayUtils.isEmpty(values)) {
            return (MyLambdaQueryWrapper<T>) super.in(column, values);
        }
        return this;
    }

    public MyLambdaQueryWrapper<T> eqIfPresent(SFunction<T, ?> column, Object val) {
        if (val != null) {
            return (MyLambdaQueryWrapper<T>) super.eq(column, val);
        }
        return this;
    }

    public MyLambdaQueryWrapper<T> neIfPresent(SFunction<T, ?> column, Object val) {
        if (val != null) {
            return (MyLambdaQueryWrapper<T>) super.ne(column, val);
        }
        return this;
    }

    public MyLambdaQueryWrapper<T> gtIfPresent(SFunction<T, ?> column, Object val) {
        if (val != null) {
            return (MyLambdaQueryWrapper<T>) super.gt(column, val);
        }
        return this;
    }

    public MyLambdaQueryWrapper<T> geIfPresent(SFunction<T, ?> column, Object val) {
        if (val != null) {
            return (MyLambdaQueryWrapper<T>) super.ge(column, val);
        }
        return this;
    }

    public MyLambdaQueryWrapper<T> ltIfPresent(SFunction<T, ?> column, Object val) {
        if (val != null) {
            return (MyLambdaQueryWrapper<T>) super.lt(column, val);
        }
        return this;
    }

    public MyLambdaQueryWrapper<T> leIfPresent(SFunction<T, ?> column, Object val) {
        if (val != null) {
            return (MyLambdaQueryWrapper<T>) super.le(column, val);
        }
        return this;
    }

    public MyLambdaQueryWrapper<T> betweenIfPresent(SFunction<T, ?> column, Object val1, Object val2) {
        if (val1 != null && val2 != null) {
            return (MyLambdaQueryWrapper<T>) super.between(column, val1, val2);
        }
        if (val1 != null) {
            return (MyLambdaQueryWrapper<T>) ge(column, val1);
        }
        if (val2 != null) {
            return (MyLambdaQueryWrapper<T>) le(column, val2);
        }
        return this;
    }

    // ========== 重写父类方法，方便链式调用 ==========

    @Override
    public MyLambdaQueryWrapper<T> eq(boolean condition, SFunction<T, ?> column, Object val) {
        super.eq(condition, column, val);
        return this;
    }

    @Override
    public MyLambdaQueryWrapper<T> eq(SFunction<T, ?> column, Object val) {
        super.eq(column, val);
        return this;
    }

    @Override
    public MyLambdaQueryWrapper<T> orderByDesc(SFunction<T, ?> column) {
        super.orderByDesc(true, column);
        return this;
    }

    @Override
    public MyLambdaQueryWrapper<T> last(String lastSql) {
        super.last(lastSql);
        return this;
    }

    @Override
    public MyLambdaQueryWrapper<T> in(SFunction<T, ?> column, Collection<?> coll) {
        super.in(column, coll);
        return this;
    }

}
