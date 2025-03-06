package com.seoulmilk.be.tax.dto.response;

import com.seoulmilk.be.tax.domain.NtsTax;
import com.seoulmilk.be.tax.domain.type.Arap;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record OfficeTaxDetailResponse(
        Long id,//id
        Arap arap, //매출매입구분
        String issueDate, //작성일자
        String suId, //공급자 사업자 등록 번호
        String ipId, //공급받는자 사업자 등록번호
        Long chargeTotal, //총 공급가액 합계
        Long grandTotal, //총액(공급가액 + 세액)
        Long taxTotal, //총 세액 합계
        LocalDate createdDate, //생성일
        LocalTime createdTime,//생성시간
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
