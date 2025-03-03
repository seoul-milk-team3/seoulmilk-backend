package com.seoulmilk.be.tax.application.ext;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "clova.ocr")
public record ClovaOcrProperties(
        String secrets,
        List<Integer> templateIds
) {
}
