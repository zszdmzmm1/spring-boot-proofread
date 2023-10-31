package com.auefly.spring.boot.security.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(BackendProperties.class)
public class BackendConfig {
}
