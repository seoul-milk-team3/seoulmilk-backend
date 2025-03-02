package com.seoulmilk.be.global.jwt.refresh.exception;

import com.seoulmilk.be.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class RefreshTokenNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;
}
