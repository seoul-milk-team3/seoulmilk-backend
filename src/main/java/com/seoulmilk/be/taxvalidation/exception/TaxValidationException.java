package com.seoulmilk.be.taxvalidation.exception;

import com.seoulmilk.be.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TaxValidationException extends RuntimeException {
    private final ErrorCode errorCode;
}
