package com.seoulmilk.be.global.exception;

import com.seoulmilk.be.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class FileConvertFailException extends RuntimeException {
  private final ErrorCode errorCode;
}
