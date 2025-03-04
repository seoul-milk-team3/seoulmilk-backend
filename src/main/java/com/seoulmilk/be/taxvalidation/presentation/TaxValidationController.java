package com.seoulmilk.be.taxvalidation.presentation;

import com.seoulmilk.be.global.dto.SuccessResponse;
import com.seoulmilk.be.taxvalidation.application.TaxValidationService;
import com.seoulmilk.be.taxvalidation.dto.response.InvoiceVerificationResponse;
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
    @GetMapping("/{taxId}/pre-verified")
    @Override
    public SuccessResponse<String> validateInvoicePreVerified(@PathVariable final Long taxId) {
        taxValidationService.validateInvoicePreVerified(taxId);
        return SuccessResponse.of(VALIDATE_TAX_INVOICE_SUCCESS);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{taxId}/post-verified")
    @Override
    public SuccessResponse<InvoiceVerificationResponse> validateInvoicePostVerified(@PathVariable final Long taxId) {
        InvoiceVerificationResponse response = taxValidationService.validateInvoicePostVerified(taxId);
        taxValidationService.updateNtsTaxIsNormal(taxId, response.getIsNormal());
        return SuccessResponse.of(VALIDATE_TAX_INVOICE_SUCCESS, response);
    }

}
