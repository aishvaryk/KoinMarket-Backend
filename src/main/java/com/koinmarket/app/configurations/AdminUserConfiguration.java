package com.koinmarket.app.configurations;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter @Setter
@ConfigurationProperties(prefix= "admin")
@Component
public class AdminUserConfiguration {
    private String username;
    private String password;
}
