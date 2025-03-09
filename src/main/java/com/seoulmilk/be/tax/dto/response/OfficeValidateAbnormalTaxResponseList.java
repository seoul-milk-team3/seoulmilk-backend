package com.seoulmilk.be.tax.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record OfficeValidateAbnormalTaxResponseList(
        List<OfficeValidateAbnormalTaxResponse> officeAbnormalTaxValidationResponses,
        int totalPageSize
) {
    public static OfficeValidateAbnormalTaxResponseList of(List<OfficeTaxFilterResponse> officeTaxFilterResponseList, int totalPageSize) {
        return OfficeValidateAbnormalTaxResponseList.builder()
                .officeAbnormalTaxValidationResponses(
                        officeTaxFilterResponseList.stream()
                                .map(OfficeValidateAbnormalTaxResponse::from)
                                .toList())
                .totalPageSize(totalPageSize)
                .build();
    }
}

