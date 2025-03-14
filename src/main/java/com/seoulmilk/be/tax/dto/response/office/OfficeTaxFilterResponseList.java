package com.seoulmilk.be.tax.dto.response.office;

import lombok.Builder;

import java.util.List;

@Builder
public record OfficeTaxFilterResponseList(
        List<OfficeTaxFilterResponse> officeTaxFilterResponseList,
        long totalPageSize
) {
    public static OfficeTaxFilterResponseList of(List<OfficeTaxFilterResponse> officeTaxFilterResponseList, long totalPageSize) {
        return OfficeTaxFilterResponseList.builder()
                .officeTaxFilterResponseList(officeTaxFilterResponseList)
                .totalPageSize(totalPageSize)
                .build();
    }
}
