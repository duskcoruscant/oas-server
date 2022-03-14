package com.hwenbin.server.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 上下文工具
 *
 * @author hwb
 * @create 2022-03-14
 */
public class ContextUtils {

    /**
     * 获取 request
     *
     * @return request
     */
    public static HttpServletRequest getRequest() {
        final ServletRequestAttributes attributes =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        return attributes == null ? null : attributes.getRequest();
    }

}
