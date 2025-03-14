package com.seoulmilk.be.tax.dto.response.office;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record OfficeValidateAbnormalTaxResponse(
        Long id,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDateTime createdDate,
        String transDate,
        String suName
) {

    public static OfficeValidateAbnormalTaxResponse from(OfficeTaxFilterResponse officeTax) {
        return OfficeValidateAbnormalTaxResponse.builder()
                .id(officeTax.id())
                .createdDate(officeTax.createdDate())
                .transDate(officeTax.transDate())
                .suName(officeTax.suName())
                .build();
    }
}

