package com.seoulmilk.be.tax.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record BeforeValidateTaxResponseList(
        List<BeforeValidateTaxResponse> beforeValidateTaxResponses,
        int totalPageSize
) {
   public static BeforeValidateTaxResponseList of(List<OfficeTaxFilterResponse> officeTaxFilterResponseList, int totalPageSize) {
        return BeforeValidateTaxResponseList.builder()
                .beforeValidateTaxResponses(
                        officeTaxFilterResponseList.stream()
                                .map(BeforeValidateTaxResponse::from)
                                .toList())
                .totalPageSize(totalPageSize)
                .build();
    }
}
