package com.hwenbin.server.util;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * Url工具
 *
 * @author hwb
 * @create 2022-03-14
 */
public class UrlUtils {

    private UrlUtils() {
    }

    /**
     * 请求的相对路径 /account/list
     *
     * @param request request
     * @return 相对路径
     */
    public static String getMappingUrl(final ServletRequest request) {
        return getMappingUrl((HttpServletRequest) request);
    }

    public static String getMappingUrl(final HttpServletRequest request) {
        return request.getRequestURI().substring(request.getContextPath().length());
    }

}
