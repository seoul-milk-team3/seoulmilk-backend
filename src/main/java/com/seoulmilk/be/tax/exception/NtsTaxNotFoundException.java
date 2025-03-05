package com.seoulmilk.be.tax.exception;

import com.seoulmilk.be.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NtsTaxNotFoundException extends RuntimeException {
    private final ErrorCode code;
}
