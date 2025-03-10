package com.seoulmilk.be.taxvalidation.dto.response;

import com.seoulmilk.be.tax.domain.type.ResultType;

public record InvoiceVerificationResponse(
        Long ntsTaxId,
        ResultType isNormal
) {
}
