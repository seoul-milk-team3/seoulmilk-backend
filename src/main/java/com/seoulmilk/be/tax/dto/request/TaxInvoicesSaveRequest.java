package com.seoulmilk.be.tax.dto.request;

import com.seoulmilk.be.tax.domain.NtsTax;
import com.seoulmilk.be.tax.domain.type.Arap;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        Map<String, String> fieldMap = request.fields().stream()
                .collect(Collectors.toMap(Field::name, Field::inferText, (existing, replacement) -> existing));

        return NtsTax.builder()
                .suName(fieldMap.getOrDefault("공급자명", "empty"))
                .suAddr(fieldMap.getOrDefault("공급자 주소", "empty"))
                .suId(fieldMap.getOrDefault("공급자 등록번호", "empty"))
                .issueId(fieldMap.getOrDefault("승인번호", "empty").replace("-", ""))
                .chargeTotal(parseLongOrDefault(fieldMap.get("공급가액"), 0L))
                .ipId(fieldMap.getOrDefault("공급받는자 등록번호", "empty"))
                .issueDate(fieldMap.getOrDefault("작성일자", "empty").replace("-", ""))
                .imageUrl(imageUrl)
                //TODO: 추후에는 실제 데이터로 변경해야 함 : 개발에는 필요없는 회사 데이터 이므로 임시로 dummy 값으로 설정
                .taxTotal(100L)
                .grandTotal(100L)
                .ernam("dummy")
                .transDate("dummy")
                .arap(Arap.AP)
                .issueDt("dummy")
                .build();
    }

    private long parseLongOrDefault(String value, long defaultValue) {
        try {
            return value != null ? Long.parseLong(value.replace(",", "")) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}