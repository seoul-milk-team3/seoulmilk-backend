package com.seoulmilk.be.tax.dto.response;

import com.seoulmilk.be.tax.domain.type.ResultType;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record OfficeTaxFilterResponse(
        Long id,
        String suId,
        String ipId,
        String transDate,
        String suName,
        String suAddr,
        ResultType isNormal,
        String isValidated,
        LocalDateTime createdDate
) {
}
