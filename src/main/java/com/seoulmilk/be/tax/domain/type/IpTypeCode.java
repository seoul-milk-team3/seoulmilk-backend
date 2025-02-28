package com.seoulmilk.be.tax.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum IpTypeCode {
    BUSINESS_REGISTRATION_NUMBER("01", "사업자등록번호"),
    RESIDENT_REGISTRATION_NUMBER("02", "주민등록번호"),
    FOREIGNER("03", "외국인"),
    ;

    private final String code;
    private final String content;

    private static final Map<String, IpTypeCode> CODE_MAP = new HashMap<>();
    static {
        for (IpTypeCode ipTypeCode : values()) {
            CODE_MAP.put(ipTypeCode.getCode(), ipTypeCode);
        }
    }

    public static Optional<IpTypeCode> findByCode(String code) {
        return Optional.ofNullable(CODE_MAP.get(code));
    }
}
