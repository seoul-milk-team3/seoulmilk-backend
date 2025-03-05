package com.seoulmilk.be.tax.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResultType {
    NORMAL("1"),
    ABNORMAL("0"),
    ALL(""),
    ;

    private final String value;

    public static ResultType fromString(String value) {
        try {
            return value == null ? ALL : ResultType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ALL;
        }
    }
}
