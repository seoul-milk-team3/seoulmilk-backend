package com.seoulmilk.be.taxvalidation.exception.errorcode;

import com.seoulmilk.be.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TaxValidationErrorCode implements ErrorCode {
    JSON_PROCESSING_ERROR(HttpStatus.BAD_REQUEST, "JSON Processing Exception occurred while processing the Codef API."),
    CODEF_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Codef API Error during first factory validation."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
