package com.seoulmilk.be.tax.dto.response.office;

import lombok.Builder;

import java.util.List;

@Builder
public record OfficeValidateAbnormalTaxResponseList(
        List<OfficeValidateAbnormalTaxResponse> officeAbnormalTaxValidationResponses,
        long totalPageSize
) {
    public static OfficeValidateAbnormalTaxResponseList of(List<OfficeTaxFilterResponse> officeTaxFilterResponseList, long totalPageSize) {
        return OfficeValidateAbnormalTaxResponseList.builder()
                .officeAbnormalTaxValidationResponses(
                        officeTaxFilterResponseList.stream()
                                .map(OfficeValidateAbnormalTaxResponse::from)
                                .toList())
                .totalPageSize(totalPageSize)
                .build();
    }
}

