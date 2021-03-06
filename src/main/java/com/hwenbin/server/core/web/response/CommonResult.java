package com.hwenbin.server.core.web.response;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author hwb
 * @create 2022-03-14
 */
@ApiModel(value = "响应结果")
public class CommonResult<T> {

    @ApiModelProperty(value = "状态码")
    private Integer code;

    @ApiModelProperty(value = "消息")
    private String message;

    @ApiModelProperty(value = "数据")
    private T data;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public Integer getCode() {
        return this.code;
    }

    public CommonResult<T> setCode(final Integer code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return this.message;
    }

    public CommonResult<T> setMessage(final String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return this.data;
    }

    public CommonResult<T> setData(final T data) {
        this.data = data;
        return this;
    }

}
