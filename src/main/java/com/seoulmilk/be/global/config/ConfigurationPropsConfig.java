package com.seoulmilk.be.global.config;

import com.seoulmilk.be.tax.application.ext.ClovaOcrProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = ClovaOcrProperties.class)
public class ConfigurationPropsConfig {
}
