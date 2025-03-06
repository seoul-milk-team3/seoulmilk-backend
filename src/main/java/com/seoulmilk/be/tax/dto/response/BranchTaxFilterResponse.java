package com.seoulmilk.be.tax.dto.response;

import com.seoulmilk.be.tax.domain.type.PayStatus;
import com.seoulmilk.be.tax.domain.type.ResultType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BranchTaxFilterResponse(
        Long id,
        String issueId,
        ResultType isNormal,
        PayStatus payStatus,
        LocalDateTime createdDateTime
) {

}
