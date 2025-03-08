package com.seoulmilk.be.tax.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.seoulmilk.be.tax.domain.type.PayStatus;
import com.seoulmilk.be.tax.domain.type.ResultType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BranchTaxFilterResponse(
        Long id,
        String issueId,
        ResultType isNormal,
        PayStatus payStatus,
        @Schema(description = "생성 일자 (yyyy-MM-dd)")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDateTime createdDate
) {

}
