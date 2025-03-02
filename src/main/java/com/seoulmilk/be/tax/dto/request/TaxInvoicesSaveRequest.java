package com.seoulmilk.be.tax.dto.request;

import com.seoulmilk.be.tax.domain.NtsTax;
import com.seoulmilk.be.tax.domain.type.Arap;

import java.util.List;

public record TaxInvoicesSaveRequest(
        String requestId,
        List<Field> fields
) {
    public record Field(
            String name,
            String inferText
    ) {
    }

    public NtsTax toNtsTax(TaxInvoicesSaveRequest request, String imageUrl) {
        return NtsTax.builder()
                .suId(request.fields().stream()
                        .filter(field -> "공급자 등록번호".equals(field.name()))
                        .map(Field::inferText)
                        .findFirst()
                        .orElse("empty"))
                .issueId(request.fields().stream()
                        .filter(field -> "승인번호".equals(field.name()))
                        .map(Field::inferText)
                        .findFirst()
                        .orElse("empty"))
                .chargeTotal(request.fields().stream()
                        .filter(field -> "공급가액".equals(field.name()))
                        .map(Field::inferText)
                        .mapToLong(Long::parseLong)
                        .findFirst()
                        .orElse(0L))
                .ipId(request.fields().stream()
                        .filter(field -> "공급받는자 등록번호".equals(field.name()))
                        .map(Field::inferText)
                        .findFirst()
                        .orElse("empty"))
                .issueDate(request.fields().stream()
                        .filter(field -> "작성일자".equals(field.name()))
                        .map(Field::inferText)
                        .findFirst()
                        .orElse("empty"))
                .imageUrl(imageUrl)
                //TODO: 추후에는 실제 데이터로 변경해야 함
                .taxTotal(100L)
                .grandTotal(100L)
                .ernam("dummy")
                .transDate("dummy")
                .arap(Arap.AP)
                .issueDt("dummy")
                .build();
    }
}