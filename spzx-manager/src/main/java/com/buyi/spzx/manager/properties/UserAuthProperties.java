package com.buyi.spzx.manager.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@ConfigurationProperties(prefix = "spzx.auth")
public class UserAuthProperties {
    //登录拦截器忽略的路径
    private List<String> noAuthUrls;
}
