package com.seoulmilk.be.tax.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PurpCode {
    RECEIPT("01", "영수"),
    CLAIM("02", "청구"),
    ;

    private final String code;
    private final String content;

    public static PurpCode findByCode(String code) {
        for (PurpCode purpCode : PurpCode.values()) {
            if (purpCode.getCode().equals(code)) {
                return purpCode;
            }
        }
        return null;
    }
}
