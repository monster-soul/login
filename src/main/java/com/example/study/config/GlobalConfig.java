package com.example.study.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class GlobalConfig {

    @Value("${study.login.excludeUrl}")
    private String[] excludeUrl;
}
