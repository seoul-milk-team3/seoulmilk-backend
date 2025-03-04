package com.seoulmilk.be.tax.dto.response;

import com.seoulmilk.be.tax.domain.type.ResultType;
import lombok.Builder;

@Builder
public record OfficeTaxFilterResponse(
        Long userId,
        String suId,
        String ipId,
        String transDate,
        String suName,
        String suAddr,
        ResultType isNormal
) {
}
