package com.hwenbin.server.util;

import com.hwenbin.server.dto.CustomerUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    /**
     * 获取 customUserDetails
     * @return customUserDetails
     */
    public static CustomerUserDetails getCustomerUserDetails() {
        final Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        return authentication == null ? null : (CustomerUserDetails) authentication.getPrincipal();
    }

}
