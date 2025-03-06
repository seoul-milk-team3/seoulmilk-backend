package com.seoulmilk.be.global.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {
    SIGN_UP_SUCCESS(201, "회원가입이 성공적으로 완료되었습니다."),
    LOGIN_SUCCESS(200, "로그인이 성공적으로 완료되었습니다."),
    SAVE_TAX_SUCCESS(201, "세금계산서가 성공적으로 저장되었습니다."),
    ANALYZE_TAX_SUCCESS(200, "세금계산서가 성공적으로 분석되었습니다."),
    LIST_BEFRORE_VALIDATE_TAX_SUCCESS(200, "세금계산서 검증 전 리스트 조회가 성공적으로 완료되었습니다."),
    OFFICE_TAX_FILTER_SUCCESS(200, "본사 세금계산서 필터링이 성공적으로 완료되었습니다."),
    VALIDATE_TAX_INVOICE_SUCCESS(200, "세금계산서 검증이 성공적으로 요청되었습니다."),
    ;

    private final int status;
    private final String message;
}
