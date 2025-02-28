package com.seoulmilk.be.global.jwt.exception;

import com.seoulmilk.be.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InvalidTokenException extends RuntimeException {
    private final ErrorCode errorCode;
}
