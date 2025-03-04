package com.seoulmilk.be.tax.dto.response;

import lombok.Builder;

@Builder
public record OfficeTaxFilterResponse(
        Long userId,
        String suId,
        String ipId,
        String transDate,
        String suName,
        String suAddr,
        String isNormal

) {
}
