package com.seoulmilk.be.tax.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record OfficeTaxFilterResponseList(
        List<OfficeTaxFilterResponse> officeTaxFilterResponseList,
        int totalPageSize
) {
    public static OfficeTaxFilterResponseList of(List<OfficeTaxFilterResponse> officeTaxFilterResponseList, int totalPageSize) {
        return OfficeTaxFilterResponseList.builder()
                .officeTaxFilterResponseList(officeTaxFilterResponseList)
                .totalPageSize(totalPageSize)
                .build();
    }
}
