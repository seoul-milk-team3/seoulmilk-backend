package com.seoulmilk.be.auth.exception.errorcode;

import com.seoulmilk.be.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    EXIST_EMPLOYEE_ID(HttpStatus.CONFLICT, "The employee ID already exists."),
    EXIST_EMAIL(HttpStatus.CONFLICT, "The email already exists."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
