package com.seoulmilk.be.taxvalidation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "세금 계산서 검증 요청 DTO")
public record InvoiceValidationRequest (
        @Schema(description = "세금 계산서 ID")
        Long id
) {
}
