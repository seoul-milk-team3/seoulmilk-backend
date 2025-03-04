package com.seoulmilk.be.taxvalidation.exception.errorcode;

import com.seoulmilk.be.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TaxValidationErrorCode implements ErrorCode {
    UNSUPPORTED_ENCODING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Unsupported Encoding Exception occurred."),
    JSON_PROCESSING_ERROR(HttpStatus.BAD_REQUEST, "JSON Processing Exception occurred while processing the Codef API."),
    INTERRUPTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Request was interrupted while processing the Codef API."),
    CODEF_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Codef API Error during first request validation"),
    INVALID_RESPONSE_FORMAT(HttpStatus.INTERNAL_SERVER_ERROR, "")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
