package com.seoulmilk.be.tax.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.seoulmilk.be.tax.domain.NtsTax;
import com.seoulmilk.be.tax.domain.type.PayStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BranchTaxDetailResponse(
        Long id,
        String imageUrl,

        @Schema(description = "승인 번호")
        String issueId,

        @Schema(description = "생성 일자 (yyyy-MM-dd)")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDateTime createdDate,

        @Schema(description = "금액")
        Long grandTotal,

        @Schema(description = "지급 상태")
        PayStatus payStatus,

        @Schema(description = "지급 날짜")
        String payDate
) {
    public static BranchTaxDetailResponse from(NtsTax ntsTax) {
        return BranchTaxDetailResponse.builder()
                .id(ntsTax.getId())
                .imageUrl(ntsTax.getImageUrl())
                .issueId(ntsTax.getIssueId())
                .createdDate(ntsTax.getCreatedDateTime())
                .grandTotal(ntsTax.getGrandTotal())
                .payStatus(ntsTax.getPayStatus())
                .payDate(ntsTax.getPayDate())
                .build();
    }
}
