package com.seoulmilk.be.global.jwt.refresh.exception;

import com.seoulmilk.be.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RefreshErrorCode implements ErrorCode {
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "Refresh token not found"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
