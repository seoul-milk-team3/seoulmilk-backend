package com.seoulmilk.be.taxvalidation.infrastructure.constants;


public enum CodefResponseCode {
    SUCCESS_RESPONSE("CF-00000"),
    SIMPLE_AUTHENTICATION_RESPONSE("CF-03002"),
    ;

    private final String code;

    CodefResponseCode(String code) {
        this.code = code;
    }

    public boolean isEqual(String code) {
        return this.code.equals(code);
    }
}
