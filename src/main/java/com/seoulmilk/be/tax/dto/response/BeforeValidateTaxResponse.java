package com.seoulmilk.be.tax.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BeforeValidateTaxResponse(
        Long id,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDateTime createdDate,
        String transDate,
        String suName
){

    public static BeforeValidateTaxResponse from(OfficeTaxFilterResponse officeTax) {
        return BeforeValidateTaxResponse.builder()
                .id(officeTax.id())
                .createdDate(officeTax.createdDate())
                .transDate(officeTax.transDate())
                .suName(officeTax.suName())
                .build();
    }
}
