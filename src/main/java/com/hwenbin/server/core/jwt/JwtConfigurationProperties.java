package com.hwenbin.server.core.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Json web token 配置
 *
 * @author hwb
 * @create 2022-03-14
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfigurationProperties {

    /**
     * claim authorities key
     */
    private String claimKeyAuth;

    /**
     * token 前缀
     */
    private String tokenType;

    /**
     * 请求头或请求参数的key
     */
    private String header;

    /**
     * 管理后台过期时间
     */
    private Duration adminExpireTime;

}
