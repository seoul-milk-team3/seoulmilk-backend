package com.seoulmilk.be.taxvalidation.presentation;

import com.seoulmilk.be.global.dto.SuccessResponse;
import com.seoulmilk.be.taxvalidation.application.TaxValidationService;
import com.seoulmilk.be.taxvalidation.dto.request.InvoiceValidationRequest;
import com.seoulmilk.be.taxvalidation.dto.response.InvoiceVerificationResponse;
import com.seoulmilk.be.taxvalidation.presentation.api.TaxValidationApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.seoulmilk.be.global.dto.SuccessCode.VALIDATE_TAX_INVOICE_SUCCESS;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("tax/validation")
public class TaxValidationController implements TaxValidationApi {
    private final TaxValidationService taxValidationService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/pre-verified/{loginTypeLevel}")
    @Override
    public SuccessResponse<String> validateInvoicePreVerified(
            @RequestBody final List<InvoiceValidationRequest> request, @PathVariable final String loginTypeLevel) {
        taxValidationService.validateInvoicesPreVerified(request, loginTypeLevel);
        return SuccessResponse.of(VALIDATE_TAX_INVOICE_SUCCESS);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/post-verified")
    @Override
    public SuccessResponse<List<InvoiceVerificationResponse>> validateInvoicePostVerified(
            @RequestBody final List<InvoiceValidationRequest> request) {
        List<InvoiceVerificationResponse> responses = taxValidationService.validateInvoicePostVerified(request);
        return SuccessResponse.of(VALIDATE_TAX_INVOICE_SUCCESS, responses);
    }

}
