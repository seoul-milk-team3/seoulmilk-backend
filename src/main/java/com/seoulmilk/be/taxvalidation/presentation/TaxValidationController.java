package com.seoulmilk.be.taxvalidation.presentation;

import com.seoulmilk.be.global.dto.SuccessResponse;
import com.seoulmilk.be.taxvalidation.application.TaxValidationService;
import com.seoulmilk.be.taxvalidation.presentation.api.TaxValidationApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.seoulmilk.be.global.dto.SuccessCode.VALIDATE_TAX_INVOICE_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("tax/validation")
public class TaxValidationController implements TaxValidationApi {
    private final TaxValidationService taxValidationService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{taxId}")
    @Override
    public SuccessResponse<String> validateTaxInvoice(@PathVariable final Long taxId) {
        taxValidationService.validateTaxInvoice(taxId);
        return SuccessResponse.of(VALIDATE_TAX_INVOICE_SUCCESS);
    }

}
