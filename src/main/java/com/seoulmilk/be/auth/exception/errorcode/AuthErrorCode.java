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
    EXIST_BUSINESS_ID(HttpStatus.CONFLICT, "The business ID already exist."),
    EXIST_USER(HttpStatus.NOT_FOUND, "The user is not exist."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "The user is not found."),
    EMAIL_ENCODING_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "Encoding email is unsupported"),
    INVALID_UUID(HttpStatus.BAD_REQUEST, "The UUID is invalid."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "The password is not valid."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
