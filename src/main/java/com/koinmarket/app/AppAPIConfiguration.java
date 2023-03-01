package com.koinmarket.app;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix= "coinmarketcap-api")
@Component
public class AppAPIConfiguration {

    @Getter @Setter
    private String url;

    @Getter @Setter
    private String key;

}
