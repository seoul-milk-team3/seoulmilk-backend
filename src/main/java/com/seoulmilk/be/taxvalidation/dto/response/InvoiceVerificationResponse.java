package com.seoulmilk.be.taxvalidation.dto.response;

public record InvoiceVerificationResponse(
        Long ntsTaxId,
        String isNormal
) {
}
