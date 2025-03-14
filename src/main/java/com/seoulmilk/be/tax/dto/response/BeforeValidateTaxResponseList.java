package com.seoulmilk.be.tax.dto.response;

import com.seoulmilk.be.tax.dto.response.office.OfficeTaxFilterResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record BeforeValidateTaxResponseList(
        List<BeforeValidateTaxResponse> beforeValidateTaxResponses,
        long totalPageSize
) {
   public static BeforeValidateTaxResponseList of(List<OfficeTaxFilterResponse> officeTaxFilterResponseList, long totalPageSize) {
        return BeforeValidateTaxResponseList.builder()
                .beforeValidateTaxResponses(
                        officeTaxFilterResponseList.stream()
                                .map(BeforeValidateTaxResponse::from)
                                .toList())
                .totalPageSize(totalPageSize)
                .build();
    }
}
