package com.seoulmilk.be.global.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {
    SIGN_UP_SUCCESS(201, "회원가입이 성공적으로 완료되었습니다."),
    LOGIN_SUCCESS(200, "로그인이 성공적으로 완료되었습니다."),
    ;


    private final int status;
    private final String message;
}
