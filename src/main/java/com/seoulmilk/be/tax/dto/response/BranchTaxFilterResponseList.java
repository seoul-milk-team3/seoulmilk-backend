package com.seoulmilk.be.tax.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record BranchTaxFilterResponseList(
        List<BranchTaxFilterResponse> branchTaxFilterResponses,
        int totalPageSize
) {
    public static BranchTaxFilterResponseList of(
            List<BranchTaxFilterResponse> branchTaxFilterResponses, int totalPageSize) {
        return BranchTaxFilterResponseList.builder()
                .branchTaxFilterResponses(branchTaxFilterResponses)
                .totalPageSize(totalPageSize)
                .build();
    }
}
