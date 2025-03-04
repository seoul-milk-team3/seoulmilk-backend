package com.seoulmilk.be.tax.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ClovaOcrResponse(
        String requestId,
        Images[] images
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Images(
            String name,
            Fields[] fields
    ) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        public record Fields(
                String name,
                String inferText
        ) {

        }
    }
}
