package com.seoulmilk.be.tax.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Getter
public enum AmendCode {
    MISTAKE_CORRECTION("01", "기재사항의 착오·정정"),
    PRICE_CHANGE("02", "공급가액 변동"),
    RETURN("03", "환입"),
    CONTRACT_TERMINATION("04", "계약의 해제"),
    POST_ISSUED_LC("05", "내국신용장 사후 개설"),
    CANCELLATION("06", "취소 발행"),
    ;

    private final String code;
    private final String content;

    private static final Map<String, AmendCode> CODE_MAP = new HashMap<>();
    static {
        for (AmendCode amendCode : values()) {
            CODE_MAP.put(amendCode.getCode(), amendCode);
        }
    }

    public static Optional<AmendCode> findByCode(String code) {
        return Optional.ofNullable(CODE_MAP.get(code));
    }
}
