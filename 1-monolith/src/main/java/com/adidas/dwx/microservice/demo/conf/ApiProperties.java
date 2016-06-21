package com.adidas.dwx.microservice.demo.conf;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("app.weather.api")
public class ApiProperties {

    private String key;

}