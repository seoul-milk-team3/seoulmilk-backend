package com.seoulmilk.be.global.config;

import com.seoulmilk.be.tax.infrastructure.ClovaOcrProperties;
import com.seoulmilk.be.taxvalidation.infrastructure.codef.EasyCodefProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {ClovaOcrProperties.class, EasyCodefProvider.class})
public class ConfigurationPropsConfig {
}
