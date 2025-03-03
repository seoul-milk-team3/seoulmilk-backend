package com.seoulmilk.be.tax.dto.request;

import lombok.Builder;

import java.util.List;

@Builder
public record TaxInvoicesSaveRequestList (
        List<TaxInvoicesSaveRequest> requests
){
}
