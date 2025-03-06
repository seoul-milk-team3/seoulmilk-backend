package com.seoulmilk.be.global.exception;

import com.seoulmilk.be.auth.exception.ExistUserException;
import com.seoulmilk.be.global.exception.errorcode.ErrorCode;
import com.seoulmilk.be.global.exception.errorcode.GlobalErrorCode;
import com.seoulmilk.be.global.exception.errorcode.UserNotFoundException;
import com.seoulmilk.be.global.exception.response.ErrorResponse;
import com.seoulmilk.be.tax.exception.UnauthorizedTaxUserException;
import com.seoulmilk.be.taxvalidation.exception.TaxValidationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger("ErrorLogger");

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException e,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        return handleExceptionInternal(e);
    }

    @ExceptionHandler(FileConvertFailException.class)
    public ResponseEntity<Object> handleFileConvertFail(FileConvertFailException e, HttpServletRequest request) {
        log.info(String.valueOf(e.getErrorCode()), request);
        return handleExceptionInternal(e.getErrorCode());
    }

    @ExceptionHandler(ExistUserException.class)
    public ResponseEntity<Object> handleExistUserException(ExistUserException e, HttpServletRequest request) {
        log.info(String.valueOf(e.getErrorCode()), request);
        return handleExceptionInternal(e.getErrorCode());
    }

    @ExceptionHandler(TaxValidationException.class)
    public ResponseEntity<Object> handleTaxValidationException(TaxValidationException e, HttpServletRequest request) {
        log.info(String.valueOf(e.getErrorCode()), request);
        return handleExceptionInternal(e.getErrorCode());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException e, HttpServletRequest request) {
        log.info(String.valueOf(e.getErrorCode()), request);
        return handleExceptionInternal(e.getErrorCode());
    }

    @ExceptionHandler(UnauthorizedTaxUserException.class)
    public ResponseEntity<Object> handleUnauthorizedTaxUserException(UnauthorizedTaxUserException e, HttpServletRequest request) {
        log.info(String.valueOf(e.getErrorCode()), request);
        return handleExceptionInternal(e.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllException(Exception e, HttpServletRequest request) {
        return handleExceptionInternal(GlobalErrorCode.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode));
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .isSuccess(false)
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .results(new ErrorResponse.ValidationErrors(null))
                .build();
    }

    private ResponseEntity<Object> handleExceptionInternal(BindException e) {
        return ResponseEntity.status(GlobalErrorCode.INVALID_PARAMETER.getHttpStatus())
                .body(makeErrorResponse(e));
    }

    private ErrorResponse makeErrorResponse(BindException e) {
        final List<ErrorResponse.ValidationError> validationErrorList = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(ErrorResponse.ValidationError::from)
                .toList();

        return ErrorResponse.builder()
                .isSuccess(false)
                .code(GlobalErrorCode.INVALID_PARAMETER.name())
                .message(GlobalErrorCode.INVALID_PARAMETER.getMessage())
                .results(new ErrorResponse.ValidationErrors(validationErrorList))
                .build();
    }
}