package com.seoulmilk.be.tax.infrastructure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "clova.ocr")
public record ClovaOcrProperties(
        String secrets,
        List<Integer> templateIds
) {
}
