package com.seoulmilk.be.tax.exception;

import com.seoulmilk.be.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UnauthorizedTaxUserException extends RuntimeException {
    private final ErrorCode errorCode;
}
