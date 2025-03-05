package com.seoulmilk.be.tax.exception.errorcode;

import com.seoulmilk.be.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum NtsTaxErrorCode implements ErrorCode {
    NTS_TAX_NOT_FOUND(HttpStatus.NOT_FOUND, "No NTS tax found"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
