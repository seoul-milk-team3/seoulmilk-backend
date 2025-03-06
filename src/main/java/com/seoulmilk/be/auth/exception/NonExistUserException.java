package com.seoulmilk.be.auth.exception;

import com.seoulmilk.be.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NonExistUserException extends RuntimeException {
    private final ErrorCode errorCode;
}
