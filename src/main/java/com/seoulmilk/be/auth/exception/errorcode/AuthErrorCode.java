package com.seoulmilk.be.auth.exception.errorcode;

import com.seoulmilk.be.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    NON_EXIST_EMPLOYEE_ID(HttpStatus.CONFLICT, "The employee ID already exists."),
    NON_EXIST_EMAIL(HttpStatus.CONFLICT, "The email already exists."),
    NON_EXIST_USER(HttpStatus.NOT_FOUND, "The user is not exist."),
    EMAIL_ENCODING_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "Encoding email is unsupported"),
    INVALID_UUID(HttpStatus.BAD_REQUEST, "The UUID is invalid.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
