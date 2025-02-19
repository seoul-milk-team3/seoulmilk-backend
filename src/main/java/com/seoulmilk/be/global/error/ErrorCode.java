package com.seoulmilk.be.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    ;

    private final HttpStatus status;
    private final String errorMessage;
}
