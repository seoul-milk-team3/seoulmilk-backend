package com.seoulmilk.be.tax.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IpTypeCode {
    BUSINESS_REGISTRATION_NUMBER("01", "사업자등록번호"),
    RESIDENT_REGISTRATION_NUMBER("02", "주민등록번호"),
    FOREIGNER("03", "외국인"),
    ;

    private final String code;
    private final String content;

    public static IpTypeCode findByCode(String code) {
        for (IpTypeCode ipTypeCode : IpTypeCode.values()) {
            if (ipTypeCode.getCode().equals(code)) {
                return ipTypeCode;
            }
        }
        return null;
    }
}
