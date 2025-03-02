package com.seoulmilk.be.global.jwt.exception;

import com.seoulmilk.be.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum JwtErrorCode implements ErrorCode {
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Token is invalid"),
    TOKEN_CREATE_ERROR(HttpStatus.UNAUTHORIZED, "Error processing JSON")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
