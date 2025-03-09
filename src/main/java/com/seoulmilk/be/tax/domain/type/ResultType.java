package com.seoulmilk.be.tax.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResultType {
    NORMAL,
    ABNORMAL,
    ALL,
    ;

//    private final String value;
//
//    public static ResultType fromValue(String value) {
//        for (ResultType type : values()) {
//            if (type.value.equals(value)) {
//                return type;
//            }
//        }
//        return ALL;
//    }
}
