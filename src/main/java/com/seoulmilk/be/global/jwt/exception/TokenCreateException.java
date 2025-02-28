package com.seoulmilk.be.global.jwt.exception;

import com.seoulmilk.be.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@Getter
@RequiredArgsConstructor
public class TokenCreateException extends RuntimeException {
    private final ErrorCode errorCode;
}
