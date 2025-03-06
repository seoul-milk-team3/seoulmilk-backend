package com.seoulmilk.be.tax.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.seoulmilk.be.tax.domain.NtsTax;
import com.seoulmilk.be.tax.domain.type.Arap;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record OfficeTaxDetailResponse(
        Long id,
        Arap arap,
        String issueDate,
        String suId,
        String ipId,
        Long chargeTotal,
        Long grandTotal,
        Long taxTotal,
        LocalDate createdDate,
        @JsonFormat(pattern = "HH:mm:ss")
        LocalTime createdTime,
        String imageUrl
) {
    public static OfficeTaxDetailResponse of(NtsTax ntsTax) {
        return OfficeTaxDetailResponse.builder()
                .id(ntsTax.getId())
                .arap(ntsTax.getArap())
                .issueDate(ntsTax.getIssueDate())
                .suId(ntsTax.getSuId())
                .ipId(ntsTax.getIpId())
                .chargeTotal(ntsTax.getChargeTotal())
                .grandTotal(ntsTax.getGrandTotal())
                .taxTotal(ntsTax.getTaxTotal())
                .createdDate(ntsTax.getCreatedDateTime().toLocalDate())
                .createdTime(ntsTax.getCreatedDateTime().toLocalTime())
                .imageUrl(ntsTax.getImageUrl())
                .build();
    }
}
