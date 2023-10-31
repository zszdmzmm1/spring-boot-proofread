package com.auefly.spring.boot.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "backend")
@Configuration("backend")
public class BackendProperties {
    private List<Menus> menus;
    private String name;
}
@Data
class Menus {
    private String name;
    private String url;
}
