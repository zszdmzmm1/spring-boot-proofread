package com.auefly.spring.boot.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "backend")
public class BackendProperties {
    private List<Menus> menus;
    private String name;
}
